package com.hikvision.fireprotection.hikalarm.model.vo;

import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author wangjinchang5
 * @date 2020/12/18 17:30
 * @since TODO
 */
@Data
public class PageData<E> {
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
}
