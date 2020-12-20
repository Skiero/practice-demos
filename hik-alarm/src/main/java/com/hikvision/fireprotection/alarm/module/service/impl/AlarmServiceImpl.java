package com.hikvision.fireprotection.alarm.module.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hikvision.fireprotection.alarm.common.utils.MapperUtil;
import com.hikvision.fireprotection.alarm.model.dto.AlarmEventDTO;
import com.hikvision.fireprotection.alarm.model.request.AlarmPageQuery;
import com.hikvision.fireprotection.alarm.model.table.AlarmDetailTable;
import com.hikvision.fireprotection.alarm.model.vo.AlarmDetailVO;
import com.hikvision.fireprotection.alarm.model.vo.PageData;
import com.hikvision.fireprotection.alarm.model.vo.publicsecurity.CarNumResult;
import com.hikvision.fireprotection.alarm.model.vo.publicsecurity.TextMsgResult;
import com.hikvision.fireprotection.alarm.module.mapper.AlarmDetailMapper;
import com.hikvision.fireprotection.alarm.module.service.AlarmService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 报警服务
 *
 * @author wangjinchang5
 * @date 2020/12/18 16:20
 * @since 1.0.100
 */
@Service
@Slf4j
public class AlarmServiceImpl implements AlarmService {
    @Resource
    private AlarmDetailMapper alarmDetailMapper;

    @Override
    public PageData<AlarmDetailVO> queryAlarmDetailPage(AlarmPageQuery query) {
        PageData<AlarmDetailVO> pageData = new PageData<>(query.getPageNo(), query.getPageSize());

        QueryWrapper<AlarmDetailTable> queryWrapper = new QueryWrapper<>();
        // 车牌号模糊查询
        if (StringUtils.isNotEmpty(query.getCarNum())) {
            queryWrapper.like("car_num", query.getContactPhone());
        }
        // 联系人模糊查询
        if (StringUtils.isNotEmpty(query.getContactName())) {
            queryWrapper.like("contact_name", query.getContactPhone());
        }
        // 手机号码模糊查询
        if (StringUtils.isNotEmpty(query.getContactPhone())) {
            queryWrapper.like("contact_phone", query.getContactPhone());
        }

        Date endTime = query.getEndTime() == null
                ? new Date()
                : query.getEndTime();

        Date startTime = query.getStartTime() == null || query.getStartTime().after(endTime)
                ? Date.from(
                endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        .minusMonths(1)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant())
                : query.getStartTime();

        queryWrapper.between("alarm_time", startTime, endTime);

        Page<AlarmDetailTable> selectPage =
                alarmDetailMapper.selectPage(new Page<>(query.getPageNo(), query.getPageSize()), queryWrapper);

        List<AlarmDetailVO> alarmDetailVOList;
        List<AlarmDetailTable> records = selectPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            alarmDetailVOList = Collections.emptyList();
        } else {
            alarmDetailVOList = MapperUtil.tableConvertToVOList(records);
        }

        pageData.setTotal(selectPage.getTotal(), selectPage.getPages())
                .setList(alarmDetailVOList);

        return pageData;
    }

    @Override
    public void handleAlarmEvent(List<AlarmEventDTO> alarmDTOList) {
        if (CollectionUtils.isEmpty(alarmDTOList)) {
            log.warn("");
            return;
        }

        alarmDTOList.forEach(alarmDTO -> {
            // 根据车牌号查询联系人
            CarNumResult carNumResult = queryContactInformation(alarmDTO);
            // 给联系人下发短信
            TextMsgResult textMsgResult = sendTextMessage(alarmDTO);
            // 将结果保存到数据库
            AlarmDetailTable table = new AlarmDetailTable();
            BeanUtils.copyProperties(alarmDTO, table);
            table.setNotifyStatus('0');
            alarmDetailMapper.insert(table);
        });
    }

    private CarNumResult queryContactInformation(AlarmEventDTO alarmDTO) {
        return new CarNumResult();
    }

    private TextMsgResult sendTextMessage(AlarmEventDTO alarmDTO) {
        return new TextMsgResult();
    }
}
