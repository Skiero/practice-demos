package com.hikvision.fireprotection.hikalarm.model.vo.publicsecurity;

import lombok.Data;

import java.util.List;

/**
 * 车牌号查询响应
 *
 * @author wangjinchang5
 * @date 2020/12/18 23:00
 * @since 1.0.100
 */
@Data
public class CarNumResult {
    /*** 总数 */
    private String total;
    /*** 结果集 */
    private List<CarNumInfo> results;
    /*** 状态：succeed=成功；error=失败 */
    private String status;
    /*** 执行时间 */
    private String executionTime;
    /*** 相关异常 */
    private String exception;
    /*** 阶段时间 */
    private String periodTime;
}
