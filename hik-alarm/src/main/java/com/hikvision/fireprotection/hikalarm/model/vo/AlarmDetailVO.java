package com.hikvision.fireprotection.hikalarm.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO
 *
 * @author wangjinchang5
 * @date 2020/12/18 16:10
 * @since TODO
 */
@Data
public class AlarmDetailVO implements Serializable {
    private static final long serialVersionUID = 3251097183255209006L;
    private String id;
    private String alarmName;
    private Date alarmTime;
    private String alarmPosition;
    private String carNum;
    private String contactName;
    private String contactPhone;
}