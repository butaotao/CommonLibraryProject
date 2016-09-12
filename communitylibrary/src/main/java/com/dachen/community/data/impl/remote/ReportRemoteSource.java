package com.dachen.community.data.impl.remote;

import com.dachen.common.http2.HttpRequestWrap;
import com.dachen.common.http2.handle.AbsHandle;
import com.dachen.common.http2.result.BaseResult;
import com.dachen.community.data.ReportSource;
import com.dachen.community.data.bean.ReportRequest;

/**
 * Created by pqixi on 2016/9/12 0012.
 */
public class ReportRemoteSource implements ReportSource {
    @Override
    public void report(ReportRequest request, final CallBack callBack) {
        if (request == null) {
            if (callBack != null) {
                callBack.onCallBack(null);//请求失败
            }
            return;
        }
        HttpRequestWrap.post(request.getUrl(), request, new AbsHandle<BaseResult>() {
            @Override
            public void handleResponse(BaseResult absResult) {
                if (callBack != null)
                    callBack.onCallBack(absResult);
            }

            @Override
            public Class<BaseResult> getResultClass() {
                return BaseResult.class;
            }
        });
    }
}
