package com.hikvision.fireprotection.alarm.model.request.publicsecurity;

import lombok.Data;

/**
 * 发送短信的参数
 *
 * @author wangjinchang5
 * @date 2020/12/20 16:00
 * @since 1.0.100
 */
@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
@Data
public class TextMsgParam {
    /*** 业务类别：01=发短信；02=已发送短信查询；03=查询回复 */
    private String BizType;
    /*** 用户名 */
    private String LoginName;
    /*** 用户密码 */
    private String Password;
    /*** 接收手机号码：1->单个手机号码，例如DestMobile=13666661234；2->多个手机号码，请用半角逗号间隔，例如DestMobile=13666661234,13688881234 */
    private String DestMobile;
    /*** 短信发送内容，最大不超过500字符。短信内容超多70字符，系统会自动分多条 */
    private String SMSContent;
}
