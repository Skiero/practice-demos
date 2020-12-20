package com.hikvision.fireprotection.alarm.module.service;

import com.hikvision.fireprotection.alarm.model.dto.AlarmEventDTO;
import com.hikvision.fireprotection.alarm.model.request.AlarmPageQuery;
import com.hikvision.fireprotection.alarm.model.vo.AlarmDetailVO;
import com.hikvision.fireprotection.alarm.model.vo.PageData;

import java.util.List;

/**
 * 报警服务
 *
 * @author wangjinchang5
 * @date 2020/12/18 16:20
 * @since 1.0.100
 */
public interface AlarmService {

    PageData<AlarmDetailVO> queryAlarmDetailPage(AlarmPageQuery query);

    void handleAlarmEvent(List<AlarmEventDTO> alarmDTOList);
}
