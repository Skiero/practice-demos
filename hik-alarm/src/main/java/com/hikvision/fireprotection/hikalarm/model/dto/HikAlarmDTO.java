package com.hikvision.fireprotection.hikalarm.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 海康报警报文
 *
 * @author wangjinchang5
 * @date 2020/12/18 16:00
 * @since 1.0.100
 */
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
    private Date alarmTime;
    /*** 报警类型 */
    private String alarmType;
    /*** 车牌号 */
    private String carNum;
    /*** 通道名称 */
    private String sensorName;
}
