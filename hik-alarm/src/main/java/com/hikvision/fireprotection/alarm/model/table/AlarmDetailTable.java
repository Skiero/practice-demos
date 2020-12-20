package com.hikvision.fireprotection.alarm.model.table;

import com.baomidou.mybatisplus.annotation.*;
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
public class AlarmDetailTable {
    /*** ID */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /*** 原始id */
    private String alarmId;
    /*** 名称 */
    private String alarmName;
    /*** 位置 */
    private String alarmPosition;
    /*** 报警时间 */
    private Date alarmTime;
    /*** 报警类型 */
    private String alarmType;
    /*** 车牌号 */
    private String carNum;
    /*** 联系人 */
    private String contactName;
    /*** 手机号码 */
    private String contactPhone;
    /*** 通知状态：1=已通知；2=未通知；3=通知失败 */
    private Integer notifyStatus;
    /*** 通知时间 */
    private Date notifyTime;
    /*** 备注 */
    private String remark;
    /*** 通道名称 */
    private String sensorName;
    /*** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /*** 更新时间 */
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
}
