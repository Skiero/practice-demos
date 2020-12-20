package com.hikvision.fireprotection.alarm.common.exception;

/**
 * 异常接口
 *
 * @author wangjinchang5
 * @date 2020/12/18 20:20
 * @since 1.0.100
 */
public interface IException {
    /**
     * 获取code
     *
     * @return code
     */
    String getCode();

    /**
     * 获取message
     *
     * @return message
     */
    String getMsg();
}
