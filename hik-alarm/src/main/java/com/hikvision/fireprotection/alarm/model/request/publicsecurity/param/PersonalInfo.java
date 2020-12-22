package com.hikvision.fireprotection.alarm.model.request.publicsecurity.param;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 车牌号查询接口的调用人信息
 *
 * @author wangjinchang5
 * @date 2020/12/20 16:00
 * @since 1.0.100
 */
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
