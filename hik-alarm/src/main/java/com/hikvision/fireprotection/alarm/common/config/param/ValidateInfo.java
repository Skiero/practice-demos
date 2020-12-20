package com.hikvision.fireprotection.alarm.common.config.param;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 车牌号查询接口的责任人信息
 *
 * @author wangjinchang5
 * @date 2020/12/20 16:00
 * @since 1.0.100
 */
@ConfigurationProperties(prefix = "validate")
@Data
public class ValidateInfo {
    /*** 责任人姓名 */
    private String zrrxm;
    /*** 责任人手机号码 */
    private String zrrsjhm;
    /*** 责任单位编码 */
    private String zrdw;
    /*** 应用名称 */
    private String yymc;
    /*** 服务名称 */
    private String fwmc;
    /*** 应用编号 */
    private String yybh;
    /*** 责任人警号 */
    private String zrrjh;
    /*** 责任单位名称 */
    private String zrdwmc;
    /*** 服务编号 */
    private String fwbh;
    /*** 责任人公民身份号码 */
    private String zrrgmsfhm;
}
