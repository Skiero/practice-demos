package com.hikvision.fireprotection.alarm.model.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

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
    @Range(min = 1L, max = 1000L, message = "The range of pageSize is 1-1000.")
    private Integer pageSize;
    /*** 车牌号 */
    private String carNum;
    /*** 联系人 */
    private String contactName;
    /*** 手机号码 */
    private String contactPhone;
    /*** 通知状态：1=已通知；2=未通知；3=通知失败 */
    private Integer notifyStatus;
    /*** 报警起始时间 */
    private Date startTime;
    /*** 报警结束时间 */
    private Date endTime;
}
