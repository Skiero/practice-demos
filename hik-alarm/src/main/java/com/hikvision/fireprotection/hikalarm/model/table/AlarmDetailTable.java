package com.hikvision.fireprotection.hikalarm.model.table;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * 报警详情表
 *
 * @author wangjinchang5
 * @date 2020/12/18 16:10
 * @since 1.0.100
 */
@TableName(value = "tb_alarm_detail")
@Data
@ApiModel
public class AlarmDetailTable {
    private String id;
    private String alarmName;
    private Date alarmTime;
    private String alarmPosition;
    private String carNum;
    private String contactName;
    private String contactPhone;
    private char notifyStatus;
    private Date notifyTime;
    private String remark;
    private Date createTime;
    private Date updateTime;
}
