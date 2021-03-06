package com.hikvision.fireprotection.alarm.model.vo.publicsecurity;

import lombok.Data;

/**
 * 短信发送响应
 *
 * @author wangjinchang5
 * @date 2020/12/18 23:00
 * @since 1.0.100
 */
@Data
public class TextMsgResult {
    /*** 业务类别：01=发短信；02=已发送短信查询；03=查询回复 */
    private String bizType;
    /*** 响应码：0=发生成功*/
    private String responseCode;
    /**
     * 备注：
     * 0=成功；
     * -1=用户名、密码错误；
     * -2=没有需要发送的手机号码；
     * -3=要发送的手机号码不准确；
     * -4=发送内容为空
     * -100=已超出最大群发人数
     * -200=已超出本月短信最大发送条数
     * -500=系统异常，请联系系统管理员
     */
    private String comment;
}
