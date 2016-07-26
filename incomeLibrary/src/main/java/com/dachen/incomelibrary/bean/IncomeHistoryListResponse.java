package com.dachen.incomelibrary.bean;

/**
 * Created by TianWei on 2016/1/13.
 */
public class IncomeHistoryListResponse extends BaseResponse {
    private static final long serialVersionUID = 3855048671814115031L;
    private IncomeHistoryListData data;

    public IncomeHistoryListData getData() {
        return data;
    }

    public void setData(IncomeHistoryListData data) {
        this.data = data;
    }
}
