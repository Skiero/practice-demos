package com.hikvision.fireprotection.hikalarm.module.service;

import com.hikvision.fireprotection.hikalarm.model.dto.HikAlarmDTO;
import com.hikvision.fireprotection.hikalarm.model.request.AlarmPageQuery;
import com.hikvision.fireprotection.hikalarm.model.vo.AlarmDetailVO;
import com.hikvision.fireprotection.hikalarm.model.vo.PageData;

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
    void handleAlarmEvent(List<HikAlarmDTO> alarmDTOList);
}
