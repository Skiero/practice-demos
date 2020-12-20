package com.hikvision.fireprotection.alarm.common.exception;

/**
 * 业务异常
 *
 * @author wangjinchang5
 * @date 2020/12/18 20:20
 * @since 1.0.100
 */
public class BusinessException extends RuntimeException implements IException {
    private static final long serialVersionUID = -2552302020161876732L;

    private final IException iException;

    public BusinessException(IException iException) {
        this.iException = iException;
    }

    @Override
    public String getCode() {
        return iException.getCode();
    }

    @Override
    public String getMsg() {
        return iException.getMsg();
    }
}
