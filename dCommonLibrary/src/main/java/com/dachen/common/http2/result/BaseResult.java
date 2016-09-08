package com.dachen.common.http2.result;

import java.io.Serializable;

/**
 * 网络请求结果的基础类，默认包含code和msg字段，其他字段继承按接口定义添加
 * Created by pqixi on 2016/9/8 0008.
 */
public class BaseResult implements AbsResult, Serializable {
    private String resultCode;
    private String resultMsg;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "resultCode='" + resultCode + '\'' +
                ", resultMsg='" + resultMsg + '\'' +
                '}';
    }
}
