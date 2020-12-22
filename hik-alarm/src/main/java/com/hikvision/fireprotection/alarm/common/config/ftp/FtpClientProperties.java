package com.hikvision.fireprotection.alarm.common.config.ftp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * FtpClientProperties
 *
 * @author wangjinchang5
 * @date 2020/12/21 15:00
 * @since 1.0.100
 */
@ConfigurationProperties(prefix = "hik.config.ftp")
@Data
public class FtpClientProperties {
    /*** 编码字符，默认是UTF-8 */
    private String encoding = "UTF-8";
    /*** 主机 */
    private String hostname;
    /*** 登录密码 */
    private String password;
    /*** 工作目录 */
    private String pathname;
    /*** 端口 */
    private Integer port;
    /*** 登录账号 */
    private String username;
}
