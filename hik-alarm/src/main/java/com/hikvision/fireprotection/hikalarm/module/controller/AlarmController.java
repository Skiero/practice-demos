package com.hikvision.fireprotection.hikalarm.module.controller;

import com.hikvision.fireprotection.hikalarm.model.request.AlarmPageQuery;
import com.hikvision.fireprotection.hikalarm.model.vo.AlarmDetailVO;
import com.hikvision.fireprotection.hikalarm.model.vo.PageData;
import com.hikvision.fireprotection.hikalarm.model.vo.ServerResponse;
import com.hikvision.fireprotection.hikalarm.module.service.AlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
}
