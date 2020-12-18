package com.hikvision.fireprotection.hikalarm.common.enums;

import com.hikvision.fireprotection.hikalarm.common.exception.IException;

/**
 * 业务异常枚举
 *
 * @author wangjinchang5
 * @date 2020/12/18 20:30
 * @since 1.0.100
 */
public enum BusinessExceptionEnum implements IException {
    /**
     * 系统错误
     */
    SYSTEM_ERROR("10010", "System error"),
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
