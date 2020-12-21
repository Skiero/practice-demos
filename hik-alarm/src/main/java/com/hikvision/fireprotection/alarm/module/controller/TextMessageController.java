package com.hikvision.fireprotection.alarm.module.controller;

import com.hikvision.fireprotection.alarm.model.dto.AlarmEventDTO;
import com.hikvision.fireprotection.alarm.model.request.AlarmPageQuery;
import com.hikvision.fireprotection.alarm.model.vo.AlarmDetailVO;
import com.hikvision.fireprotection.alarm.model.vo.PageData;
import com.hikvision.fireprotection.alarm.model.vo.ServerResponse;
import com.hikvision.fireprotection.alarm.module.service.AlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

/**
 * 短信控制层
 *
 * @author wangjinchang5
 * @date 2020/12/18 16:30
 * @since 1.0.100
 */
@Api(tags = "短信模块")
@RestController
@RequestMapping("/text_msg/v1")
public class TextMessageController {
    @Resource
    private AlarmService alarmService;

    @ApiOperation(value = "分页查询报警信息", notes = "根据查询条件，分页查询报警信息")
    @PostMapping("/alarm_page")
    public ServerResponse<PageData<AlarmDetailVO>> queryAlarmInfoPage(@Validated @RequestBody AlarmPageQuery query) {
        PageData<AlarmDetailVO> page = alarmService.queryAlarmDetailPage(query);
        return ServerResponse.success(page);
    }

    @ApiOperation(value = "模拟触发报警报警")
    @GetMapping("/alarm_mock")
    public void mockAlarm() {
        AlarmEventDTO alarmEventDTO = new AlarmEventDTO();
        alarmEventDTO.setAlarmId(UUID.randomUUID().toString());
        alarmEventDTO.setAlarmName("模拟报警-" + RandomUtils.nextInt(10, 99));
        alarmEventDTO.setAlarmPosition("海康");
        alarmEventDTO.setAlarmTime(new Date());
        alarmEventDTO.setAlarmType("通道占用");
        alarmEventDTO.setCarNum("浙A" + RandomUtils.nextInt(10000, 99999));
        alarmService.handleAlarmEvent(Collections.singletonList(alarmEventDTO));
    }
}
