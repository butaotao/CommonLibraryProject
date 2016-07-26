package com.dachen.incomelibrary.bean;

import java.util.List;

/**
 * Created by TianWei on 2016/1/13.
 */
public class IncomeHistoryListData extends BaseModel {

    private static final long serialVersionUID = 6305587500661895748L;
    private List<IncomeHistoryListDetails> pageData;
    private int pageCount;
    private int pageIndex;
    private int pageSize;
    private int start;
    private int total;

    public List<IncomeHistoryListDetails> getPageData() {
        return pageData;
    }

    public void setPageData(List<IncomeHistoryListDetails> pageData) {
        this.pageData = pageData;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "IncomeHistoryListData{" +
                "pageData=" + pageData +
                ", pageCount=" + pageCount +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                ", start=" + start +
                ", total=" + total +
                '}';
    }
}
