package com.hikvision.fireprotection.alarm.common.enums;

/**
 * 通知状态枚举
 *
 * @author wangjinchang5
 * @date 2020/12/20 15:00
 * @since 1.0.100
 */
public enum NotifyStatus {
    ALREADY_NOTIFIED(1, "已通知"),
    NOT_NOTIFIED(2, "未通知"),
    FAILED_NOTIFIED(3, "通知失败"),
    UNKNOWN_STATUS(0, "未知状态"),
    ;

    NotifyStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public final Integer code;
    public final String message;

    public static String getMsgByCode(Integer code) {
        for (NotifyStatus notifyStatus : values()) {
            if (notifyStatus.code.equals(code)) {
                return notifyStatus.message;
            }
        }
        return UNKNOWN_STATUS.message;
    }
}
