package com.hikvision.fireprotection.hikalarm.model.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 报警分页查询条件
 *
 * @author wangjinchang5
 * @date 2020/12/18 17:48
 * @since 1.0.100
 */
@Data
public class AlarmPageQuery {
    /*** 当前页码 */
    @NotNull(message = "The pageNo cannot be empty.")
    @Min(value = 1L, message = "The minimum of pageNo is 1.")
    private Integer pageNo;
    /*** 页面大小 */
    @NotNull(message = "The pageSize cannot be empty.")
    @Range(min = 1L, max = 1000L, message = "The range of  pageSize  is 1-1000.")
    private Integer pageSize;
    /*** 车牌号 */
    private String carNum;
    /*** 联系人 */
    private String contactName;
    /*** 手机号码 */
    private String contactPhone;
    /*** 报警周期，单位是天 */
    @NotNull(message = "The alarmPeriod cannot be empty.")
    @Range(min = 1, message = "The minimum of period is 1.")
    private Long alarmPeriod;
}
