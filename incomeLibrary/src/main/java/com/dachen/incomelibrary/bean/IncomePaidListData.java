package com.dachen.incomelibrary.bean;

import java.util.List;

/**
 * Created by TianWei on 2016/1/13.
 */
public class IncomePaidListData extends BaseModel {

    private static final long serialVersionUID = 2220660464604987603L;
    private List<IncomePaidListDetails> pageData;
    private int pageCount;
    private int pageIndex;
    private int pageSize;
    private int start;
    private int total;

    public List<IncomePaidListDetails> getPageData() {
        return pageData;
    }

    public void setPageData(List<IncomePaidListDetails> pageData) {
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


}
