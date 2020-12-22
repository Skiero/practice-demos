package com.hikvision.fireprotection.alarm.common.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.hikvision.fireprotection.alarm.model.dto.AlarmEventDTO;
import com.hikvision.fireprotection.alarm.module.service.AlarmService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 定时轮询FTP服务器上的报警事件
 *
 * @author wangjinchang5
 * @date 2020/12/21 16:00
 * @since 1.0.100
 */
@ConditionalOnBean(FTPClient.class)
@Component
@Slf4j
public class PollFtpSchedule {

    @Value("${hik.config.ftp.pathname}")
    private String pathname;

    @Resource
    private FTPClient ftpClient;

    @Resource
    private AlarmService alarmService;

    /**
     * 如果删除失败，把文件路径存放在该Set<String>中
     */
    private final Set<String> deleteFailedFilePath = new CopyOnWriteArraySet<>();

    @Scheduled(cron = "${hik.schedule.poll_ftp.cron:0/10 * * * * ?}")
    public void pollFtpClientAndProcessEvent() {
        try {
            // 1 获取约定目录下的所有文件路径
            Set<String> latestFtpFilePath = findLatestFtpFilePath();
            latestFtpFilePath.removeAll(deleteFailedFilePath);
            if (!deleteFailedFilePath.isEmpty()) {
                log.warn("Delete failed file path set is {}.", JSON.toJSONString(deleteFailedFilePath));
            }
            if (latestFtpFilePath.isEmpty()) {
                return;
            } else {
                log.debug("Find latest file path set is {}.", JSON.toJSONString(latestFtpFilePath));
            }

            // 2 根据文件路径读取文本内容
            List<AlarmEventDTO> alarmEventDTOList = readFtpFile(latestFtpFilePath);
            // 3 处理报警事件
            Set<String> processedFilePaths = alarmService.processAlarmEvent(alarmEventDTOList);
            // 4 阅后即焚
            deleteFtpFile(processedFilePaths);
        } catch (Exception e) {
            log.error("Failed to poll ftpClient and process event.", e);
        }
    }

    private Set<String> findLatestFtpFilePath() {
        // 使用默认的系统自动检测机制，获取当前工作目录的文件信息列表。
        FTPFile[] fileArray;
        try {
            fileArray = ftpClient.listFiles();
        } catch (IOException e) {
            log.error("Failed to find latest files.The pathname is {}.", pathname);
            return new HashSet<>(0);
        }
        if (fileArray == null || fileArray.length == 0) {
            return new HashSet<>(0);
        }

        Set<String> filePathSet = new HashSet<>(fileArray.length);

        for (FTPFile ftpFile : fileArray) {
            if (ftpFile.isFile()) {
                filePathSet.add(pathname + ftpFile.getName());
            }
        }
        return filePathSet;
    }

    private List<AlarmEventDTO> readFtpFile(Set<String> filePaths) {
        // 一个目录下可能有多个文本，一个文本可能有多个报警事件，初始化时在文件数量的基础上+16
        List<AlarmEventDTO> alarmEventDTOList = new ArrayList<>((filePaths.size() + 16));

        filePaths.forEach(filePath -> {
            List<AlarmEventDTO> eventDTOList;
            try {
                eventDTOList = readFtpFile(filePath);
            } catch (IOException e) {
                log.error("Failed to retrieve file stream.The filePath is {}.", filePath, e);
                return;
            } catch (JSONException e) {
                return;
            }
            alarmEventDTOList.addAll(eventDTOList);
        });

        return alarmEventDTOList;
    }

    private List<AlarmEventDTO> readFtpFile(String filePath) throws IOException {
        StringBuilder sb;
        // 返回一个InputStream，可以从中读取服务器中的命名文件。
        try (InputStream is = ftpClient.retrieveFileStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            // 从FTP服务器获取答复并返回整数答复代码。调用此方法后，可以通过getReplyString 或 访问实际的回复文本getReplyStrings 。仅在实现自己的FTP客户端或需要从FTP服务器获取辅助响应时，才使用此方法。
            ftpClient.getReply();
        }

        List<AlarmEventDTO> alarmEventDTOList;
        try {
            alarmEventDTOList = JSON.parseArray(sb.toString(), AlarmEventDTO.class);
        } catch (JSONException e) {
            log.error("Failed to parse json to object.The json is {}.", sb.toString(), e);
            throw e;
        }

        alarmEventDTOList.forEach(alarmEventDTO -> alarmEventDTO.setFilePath(filePath));

        return alarmEventDTOList;
    }

    private void deleteFtpFile(Set<String> filePaths) {
        if (filePaths == null || filePaths.isEmpty()) {
            return;
        }

        filePaths.forEach(filePath -> {
            boolean delete;
            try {
                // 删除FTP服务器上的文件。如果成功完成，则为True，否则为false。
                delete = ftpClient.deleteFile(filePath);
            } catch (IOException e) {
                delete = false;
                deleteFailedFilePath.add(filePath);
                log.error("Failed to delete file.A IOException has occurred.FilePath is {}.", filePath, e);
            }
            if (!delete) {
                log.error("Failed to delete file.The filePath is {}.", filePath);
            }
        });
    }
}
