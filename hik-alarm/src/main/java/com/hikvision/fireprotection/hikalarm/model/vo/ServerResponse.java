package com.hikvision.fireprotection.hikalarm.model.vo;

import com.hikvision.fireprotection.hikalarm.common.exception.IException;
import lombok.Data;

/**
 * 服务响应
 *
 * @author wangjinchang5
 * @date 2020/12/18 17:30
 * @since 1.0.100
 */
@Data
public class ServerResponse<D> {
    private String code;
    private String msg;
    private D date;

    public ServerResponse() {
    }

    public ServerResponse(D date) {
        this.code = "0";
        this.msg = "success";
        this.date = date;
    }

    public ServerResponse(IException iException) {
        this.code = iException.getCode();
        this.msg = iException.getMsg();
    }
}
