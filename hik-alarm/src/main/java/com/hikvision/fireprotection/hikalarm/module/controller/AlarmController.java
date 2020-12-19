package com.hikvision.fireprotection.hikalarm.module.controller;

import com.google.common.collect.Lists;
import com.hikvision.fireprotection.hikalarm.model.dto.HikAlarmDTO;
import com.hikvision.fireprotection.hikalarm.model.request.AlarmPageQuery;
import com.hikvision.fireprotection.hikalarm.model.vo.AlarmDetailVO;
import com.hikvision.fireprotection.hikalarm.model.vo.PageData;
import com.hikvision.fireprotection.hikalarm.model.vo.ServerResponse;
import com.hikvision.fireprotection.hikalarm.module.service.AlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * 报警控制层
 *
 * @author wangjinchang5
 * @date 2020/12/18 16:30
 * @since 1.0.100
 */
@Api(tags = "报警模块")
@RestController
@RequestMapping("/alarm/v1")
public class AlarmController {
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
        HikAlarmDTO hikAlarmDTO = new HikAlarmDTO();
        hikAlarmDTO.setAlarmId(UUID.randomUUID().toString());
        hikAlarmDTO.setAlarmName("模拟报警-" + RandomUtils.nextInt(10, 99));
        hikAlarmDTO.setAlarmPosition("海康");
        hikAlarmDTO.setAlarmTime(new Date());
        hikAlarmDTO.setAlarmType("通道占用");
        hikAlarmDTO.setCarNum("浙A" + RandomUtils.nextInt(10000, 99999));
        alarmService.handleAlarmEvent(Lists.newArrayList(hikAlarmDTO));
    }

}
