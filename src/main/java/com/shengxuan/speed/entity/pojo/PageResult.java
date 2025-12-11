package com.shengxuan.speed.entity.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果集
 */
public class PageResult implements Serializable {
    /**
     *
     */
    private long total;

    /**
     * 当前分页记录数
     */
    private long pageSize;

    /**
     * 当前页
     */
    private long pageNo;

    /**
     * 分页内容
     */
    private List list;

    public PageResult(long total, long pageSize, long pageNo, List list) {
        this.total = total;
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.list = list;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPageNo() {
        return pageNo;
    }

    public void setPageNo(long pageNo) {
        this.pageNo = pageNo;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }
}
