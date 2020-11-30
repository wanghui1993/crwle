
package com.wh.yaofangwang.common.page;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回Json的分页对象
 *
 * @param <T> 泛型类
 */
public class PageVO<T> {
    /**
     * 记录数
     */
    private int count;

    /**
     * 当前页
     */
    private int currentPageNum;

    /**
     * 每页条数
     */
    private int pageSize;

    /**
     * 记录集
     */
    private List<T> records = new ArrayList<>();


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCurrentPageNum() {
        return currentPageNum;
    }

    public void setCurrentPageNum(int currentPageNum) {
        this.currentPageNum = currentPageNum;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public PageVO() {
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


    public PageVO(int count, int currentPageNum, int pageSize, List<T> records) {
        this.count = count;
        this.currentPageNum = currentPageNum;
        this.pageSize = pageSize;
        this.records = records;
    }
}
