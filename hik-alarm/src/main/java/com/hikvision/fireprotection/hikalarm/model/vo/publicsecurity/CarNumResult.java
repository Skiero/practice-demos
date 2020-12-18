package com.hikvision.fireprotection.hikalarm.model.vo.publicsecurity;

import java.util.List;

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
