package com.hikvision.fireprotection.alarm.common.constant;

/**
 * URL常量
 *
 * @author wangjinchang5
 * @date 2020/12/18 20:40
 * @since 1.0.100
 */
public interface UrlConstant {
    /*** 查询车牌信息URL POST请求，只需要参数 */
    String VEHICLE_INFORMATION = "http://50.16.212.226:30281/api/event/services/onlineBatchSearch";
    /**** 发送短信URL GET请求，只需要参数 */
    String SEND_TEXT_MSG = "http://dxpt.gat.js/SMSOutServlet";
}
