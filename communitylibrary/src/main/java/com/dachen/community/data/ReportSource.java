package com.dachen.community.data;

import com.dachen.common.http2.result.BaseResult;
import com.dachen.community.data.bean.ReportRequest;

/**
 * Created by pqixi on 2016/9/12 0012.
 */
public interface ReportSource {

    interface CallBack {
        void onCallBack(BaseResult result);
    }

    void report(ReportRequest request, CallBack callBack);
}
