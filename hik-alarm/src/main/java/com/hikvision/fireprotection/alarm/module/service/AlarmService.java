package com.hikvision.fireprotection.alarm.module.service;

import com.hikvision.fireprotection.alarm.model.dto.AlarmEventDTO;
import com.hikvision.fireprotection.alarm.model.request.AlarmPageQuery;
import com.hikvision.fireprotection.alarm.model.vo.AlarmDetailVO;
import com.hikvision.fireprotection.alarm.model.vo.PageData;

import java.util.List;
import java.util.Set;

/**
 * 报警服务
 *
 * @author wangjinchang5
 * @date 2020/12/18 16:20
 * @since 1.0.100
 */
public interface AlarmService {

    /**
     * 分页查询报警详情
     *
     * @param query 查询条件
     * @return 分页信息
     */
    PageData<AlarmDetailVO> queryAlarmDetailPage(AlarmPageQuery query);

    /**
     * 处理报警事件
     *
     * @param alarmEventDTOList 报警事件列表
     * @return 以保存的报警事件的文件路径
     */
    Set<String> processAlarmEvent(List<AlarmEventDTO> alarmEventDTOList);
}
