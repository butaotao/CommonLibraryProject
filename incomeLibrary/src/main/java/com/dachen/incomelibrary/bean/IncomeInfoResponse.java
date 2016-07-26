package com.dachen.incomelibrary.bean;

/**
 * Created by TianWei on 2016/1/12.
 */
public class IncomeInfoResponse extends BaseResponse {

    private IncomeInfoData data;

    public IncomeInfoData getData() {
        return data;
    }

    public void setData(IncomeInfoData data) {
        this.data = data;
    }
}
