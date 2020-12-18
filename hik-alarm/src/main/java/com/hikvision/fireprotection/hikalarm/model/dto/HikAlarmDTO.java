package com.hikvision.fireprotection.hikalarm.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class HikAlarmDTO implements Serializable {
    private static final long serialVersionUID = -7649845341179344246L;
    /*** 报警id */
    private String alarmId;
    /*** 报警名称 */
    private String alarmName;
    /*** 报警位置 */
    private String alarmPosition;
    /*** 报警时间 */
    private String alarmTime;
    /*** 报警类型 */
    private String alarmType;
    /*** 车牌号 */
    private String carNum;
    /*** 通道名称 */
    private String sensorName;
}
