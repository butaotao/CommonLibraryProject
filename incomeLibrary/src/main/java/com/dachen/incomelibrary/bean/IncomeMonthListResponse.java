package com.dachen.incomelibrary.bean;

/**
 * Created by TianWei on 2016/1/13.
 */
public class IncomeMonthListResponse extends BaseResponse {
    private static final long serialVersionUID = 629829225407952940L;
    private IncomeMonthListData data;

    public IncomeMonthListData getData() {
        return data;
    }

    public void setData(IncomeMonthListData data) {
        this.data = data;
    }
}
