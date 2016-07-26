package com.dachen.incomelibrary.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/1/12.
 */
public class IncomeDetailsData extends BaseModel {

    private static final long serialVersionUID = 8928629308533888438L;
    private List<IncomeDetails> pageData;
    private int pageCount;
    private int pageIndex;
    private int pageSize;
    private int start;
    private int total;

    public List<IncomeDetails> getPageData() {
        return pageData;
    }

    public void setPageData(List<IncomeDetails> pageData) {
        this.pageData = pageData;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

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


}
