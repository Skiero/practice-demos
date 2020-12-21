package com.hikvision.fireprotection.alarm.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 报警详情
 *
 * @author wangjinchang5
 * @date 2020/12/18 17:00
 * @since 1.0.100
 */
@Data
public class AlarmDetailVO implements Serializable {
    private static final long serialVersionUID = 3251097183255209006L;
    /*** ID */
    private String id;
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
    /*** 通知状态 */
    private Integer notifyStatus;
    /*** 通知时间 */
    private Date notifyTime;
    /*** 备注 */
    private String remark;
    /*** 通道名称 */
    private String sensorName;
}
