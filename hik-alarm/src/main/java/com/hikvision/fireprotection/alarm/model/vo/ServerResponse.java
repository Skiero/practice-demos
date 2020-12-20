package com.hikvision.fireprotection.alarm.model.vo;

import com.hikvision.fireprotection.alarm.common.exception.IException;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 服务响应
 *
 * @author wangjinchang5
 * @date 2020/12/18 17:30
 * @since 1.0.100
 */
public class ServerResponse<D> implements Serializable {
    private static final long serialVersionUID = -8922777085732062419L;
    private transient final String successCode = "0";

    @Getter
    @Setter
    private String code;
    @Getter
    @Setter
    private String msg;
    @Getter
    @Setter
    private D data;

    /**
     * constructor
     */
    public ServerResponse() {
        this.code = successCode;
        this.msg = "success";
    }

    public ServerResponse(IException iException) {
        this.code = iException.getCode();
        this.msg = iException.getMsg();
    }

    /**
     * method
     */
    public static <D> ServerResponse<D> success(D data) {
        ServerResponse<D> response = new ServerResponse<>();
        response.setData(data);
        return response;
    }

    public static <D> ServerResponse<D> error(IException iException) {
        return new ServerResponse<D>(iException);
    }

    public boolean failed() {
        return successCode.equals(this.code);
    }

    /**
     * toString
     */
    @Override
    public String toString() {
        return "ServerResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
