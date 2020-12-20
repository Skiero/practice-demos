package com.hikvision.fireprotection.alarm.model.request.publicsecurity;

import lombok.Data;

import java.util.List;

/**
 * 查询车辆的参数
 *
 * @author wangjinchang5
 * @date 2020/12/20 17:00
 * @since 1.0.100
 */
@Data
public class CarNumParam {
    /*** 调用人信息 */
    private RealInfo realInfo;
    /*** 业务参数 */
    private Required required;
    /*** 认证参数 */
    private Validate validate;

    public CarNumParam() {
    }

    public CarNumParam(RealInfo realInfo, Validate validate) {
        this.realInfo = realInfo;
        this.validate = validate;
    }

    @Data
    public static class RealInfo {
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

    @Data
    public static class Required {
        /*** 统计总数：0=不统计；1=统计 */
        private String cnt;
        /*** 参数类型，以逗号隔开 */
        private String requiredItems;
        /*** 存放批量查询的字段，以key/value形式赋值 */
        private List<String> fields;
    }

    @Data
    public static class Validate {
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
}
