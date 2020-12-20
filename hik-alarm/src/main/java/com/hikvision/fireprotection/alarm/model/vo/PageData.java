package com.hikvision.fireprotection.alarm.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页数据
 *
 * @author wangjinchang5
 * @date 2020/12/18 17:30
 * @since 1.0.100
 */
@Data
public class PageData<E> implements Serializable {
    private static final long serialVersionUID = -6020388282600445828L;

    /*** 当前页码 */
    private Integer pageNo;
    /*** 页面大小 */
    private Integer pageSize;
    /*** 总条数 */
    private Long total;
    /*** 列表 */
    private List<E> list;
    /*** 总页码 */
    private Long totalPage;

    public PageData(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = 0L;
        this.totalPage = 0L;
    }

    public PageData<E> setTotal(Long total, Long totalPage) {
        this.total = total;
        this.totalPage = totalPage;
        return this;
    }
}
