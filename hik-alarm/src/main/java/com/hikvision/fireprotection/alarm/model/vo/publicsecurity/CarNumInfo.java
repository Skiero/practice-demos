package com.hikvision.fireprotection.alarm.model.vo.publicsecurity;

import lombok.Data;

/**
 * 车牌号信息
 *
 * @author wangjinchang5
 * @date 2020/12/18 23:00
 * @since 1.0.100
 */
@SuppressWarnings({"SpellCheckingInspection", "AlibabaLowerCamelCaseVariableNaming"})
@Data
public class CarNumInfo {
    /*** 号牌种类 */
    private String HPZL;
    /*** 身份证号码 */
    private String SFZHM;
    /*** 机动车状态 */
    private String ZT;
    /***  联系电话 */
    private String LXDH;
    /*** '苏'号牌号码 */
    private String HPHM2;
}
