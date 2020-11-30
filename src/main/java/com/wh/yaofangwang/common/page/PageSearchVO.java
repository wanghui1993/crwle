
package com.wh.yaofangwang.common.page;

/**
 * 分页查询类的父类
 *
 */
public class PageSearchVO {
    /**
     * 当前页，默认第一页
     */
    private int pageIndex = 1;
    /**
     * 每页记录数，默认全局变量
     */
    private int pageSize = 10;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
