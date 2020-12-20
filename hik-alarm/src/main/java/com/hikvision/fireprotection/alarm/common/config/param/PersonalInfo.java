package com.hikvision.fireprotection.alarm.common.config.param;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties(prefix = "personnel")
@Data
public class PersonalInfo {
    /*** 调用人联系方式 */
    private String dyrlxfs;
    /*** 调用人公民身份号码 */
    private String dyrgmsfhm;
    /*** 调用人单位编码 */
    private String dyrdwbm;
    /*** 调用原因 */
    private String dyyy;
    /*** 调用人姓名 */
    private String dyrxm;
    /*** 调用人ip */
    private String dyrip;
    /*** 调用人单位名称 */
    private String dyrdwmc;
}
