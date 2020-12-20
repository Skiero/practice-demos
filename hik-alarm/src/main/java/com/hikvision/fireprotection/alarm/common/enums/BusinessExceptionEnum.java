package com.hikvision.fireprotection.alarm.common.enums;

import com.hikvision.fireprotection.alarm.common.exception.IException;

/**
 * 业务异常枚举
 *
 * @author wangjinchang5
 * @date 2020/12/18 20:30
 * @since 1.0.100
 */
public enum BusinessExceptionEnum implements IException {
    /*** 未知错误 */
    UNEXPECTED_ERROR("10001", "unexpected error"),
    /*** 系统错误 */
    SYSTEM_ERROR("10002", "System error"),
    /*** 非法参数 */
    INVALID_PARAMETER("10003", "Invalid parameter"),
    ;

    BusinessExceptionEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private final String code;
    private final String msg;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
