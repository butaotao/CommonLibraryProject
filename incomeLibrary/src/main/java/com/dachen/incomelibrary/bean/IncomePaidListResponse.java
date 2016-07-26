package com.dachen.incomelibrary.bean;

/**
 * Created by TianWei on 2016/1/14.
 */
public class IncomePaidListResponse extends BaseResponse {
    private static final long serialVersionUID = 4640250272623122422L;
    private IncomePaidListData data;

    public IncomePaidListData getData() {
        return data;
    }

    public void setData(IncomePaidListData data) {
        this.data = data;
    }
}
