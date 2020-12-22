package com.hikvision.fireprotection.alarm.model.request.publicsecurity;

import com.hikvision.fireprotection.alarm.model.request.publicsecurity.param.PersonalInfo;
import com.hikvision.fireprotection.alarm.model.request.publicsecurity.param.ValidateInfo;
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

    public CarNumParam(RealInfo realInfo, Required required, Validate validate) {
        this.realInfo = realInfo;
        this.required = required;
        this.validate = validate;
    }

    @SuppressWarnings("SpellCheckingInspection")
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

        public RealInfo() {
        }

        public RealInfo(String dyrlxfs, String dyrgmsfhm, String dyrdwbm, String dyyy,
                        String dyrxm, String dyrip, String dyrdwmc) {
            this.dyrlxfs = dyrlxfs;
            this.dyrgmsfhm = dyrgmsfhm;
            this.dyrdwbm = dyrdwbm;
            this.dyyy = dyyy;
            this.dyrxm = dyrxm;
            this.dyrip = dyrip;
            this.dyrdwmc = dyrdwmc;
        }

        public static RealInfo build(PersonalInfo personalInfo) {
            return new RealInfo(personalInfo.getDyrlxfs(),
                    personalInfo.getDyrgmsfhm(),
                    personalInfo.getDyrdwbm(),
                    personalInfo.getDyyy(),
                    personalInfo.getDyrxm(),
                    personalInfo.getDyrip(),
                    personalInfo.getDyrdwmc());
        }
    }

    @Data
    public static class Required {
        /*** 统计总数：0=不统计；1=统计 */
        private String cnt;
        /*** 参数类型，以逗号隔开 */
        private String requiredItems;
        /*** 存放批量查询的字段，以key/value形式赋值 */
        private List<String> fields;

        public Required(String cnt, String requiredItems, List<String> fields) {
            this.cnt = cnt;
            this.requiredItems = requiredItems;
            this.fields = fields;
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
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

        public Validate() {
        }

        public Validate(String zrrxm, String zrrsjhm, String zrdw, String yymc, String fwmc,
                        String yybh, String zrrjh, String zrdwmc, String fwbh, String zrrgmsfhm) {
            this.zrrxm = zrrxm;
            this.zrrsjhm = zrrsjhm;
            this.zrdw = zrdw;
            this.yymc = yymc;
            this.fwmc = fwmc;
            this.yybh = yybh;
            this.zrrjh = zrrjh;
            this.zrdwmc = zrdwmc;
            this.fwbh = fwbh;
            this.zrrgmsfhm = zrrgmsfhm;
        }

        public static Validate build(ValidateInfo validateInfo) {
            return new Validate(validateInfo.getZrrxm(),
                    validateInfo.getZrrsjhm(),
                    validateInfo.getZrdw(),
                    validateInfo.getYymc(),
                    validateInfo.getFwmc(),
                    validateInfo.getYybh(),
                    validateInfo.getZrrjh(),
                    validateInfo.getZrdwmc(),
                    validateInfo.getFwbh(),
                    validateInfo.getZrrgmsfhm());
        }
    }
}
