package com.hikvision.fireprotection.alarm.common.config.ftp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetAddress;

/**
 * FtpClientConfiguration
 *
 * @author wangjinchang5
 * @date 2020/12/21 15:00
 * @see <a href="http://commons.apache.org/proper/commons-net/javadocs/api-1.4.1/org/apache/commons/net/ftp/FTPClient.html">FTPClient</a>
 * @since 1.0.100
 */
@Configuration
@ConditionalOnClass(FTPClient.class)
@EnableConfigurationProperties(FtpClientProperties.class)
@Slf4j
public class FtpClientConfiguration {

    private final FtpClientProperties ftpClientProperties;

    @Autowired
    public FtpClientConfiguration(FtpClientProperties ftpClientProperties) {
        this.ftpClientProperties = ftpClientProperties;
    }

    @Bean(destroyMethod = "disconnect")
    @ConditionalOnMissingBean(FTPClient.class)
    public FTPClient ftpClient() throws IOException {
        String hostname = ftpClientProperties.getHostname();
        Integer port = ftpClientProperties.getPort();

        // 您应该记住，如果客户端空闲时间超过给定时间段（通常为900秒），则FTP服务器可能会选择过早关闭连接。
        FTPClient ftpClient = new FTPClient();
        try {
            // 在指定的端口打开一个连接到远程主机的套接字，该套接字从当前主机在系统分配的端口开始。
            ftpClient.connect(hostname, port);
        } catch (IOException e) {
            log.error("Failed to connect to the FTPClient.", e);
            throw e;
        }
        // 尝试连接后，应检查回复码以进行验证
        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            log.error("FTP server refused to connect.ReplyCode is {}.", ftpClient.getReplyCode());
            throw new IOException("FTP server refused to connect.");
        }

        try {
            // 设置要传输的文件类型。这应该是一个 FTP.ASCII_FILE_TYPE ， FTP.IMAGE_FILE_TYPE 等等。当你想改变类型的文件类型，只需要设置。更改后，新类型将保持有效，直到您再次对其进行更改。默认文件类型 FTP.ASCII_FILE_TYPE 。
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        } catch (IOException e) {
            log.error("Failed to set file type.", e);
            throw e;
        }

        try {
            // 将当前数据连接模式设置为 ACTIVE_REMOTE_DATA_CONNECTION 。仅对服务器到服务器的数据传输使用此方法。此方法向服务器发出PORT命令，指示该服务器应连接到的其他服务器和端口以进行数据传输。您必须在每次服务器到服务器的传输尝试之前调用此方法。
            ftpClient.enterRemoteActiveMode(InetAddress.getByName(hostname), port);
        } catch (IOException e) {
            log.error("Failed to set remote active mode.", e);
            throw e;
        }

        try {
            // 使用提供的用户名和密码登录到FTP服务器。
            ftpClient.login(ftpClientProperties.getUsername(), ftpClientProperties.getPassword());
        } catch (IOException e) {
            log.error("Failed to login FTPClient.", e);
            throw e;
        }

        // 启用或禁用验证，以确保参与数据连接的远程主机与控制连接所连接的主机相同。默认为启用验证。无论FTPClient当前是否连接，您都可以随时设置此值。
        ftpClient.setRemoteVerificationEnabled(false);
        // 设置FTP控件连接使用的字符编码。某些FTP服务器要求以非ASCII编码（如UTF-8）发布命令，以便可以指定具有多字节字符表示形式（例如Big 8）的文件名。
        ftpClient.setControlEncoding(ftpClientProperties.getEncoding());
        // 更改FTP会话的当前工作目录。
        ftpClient.changeWorkingDirectory(ftpClientProperties.getPathname());

        return ftpClient;
    }
}
