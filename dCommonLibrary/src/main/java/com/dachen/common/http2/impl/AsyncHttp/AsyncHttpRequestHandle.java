package com.dachen.common.http2.impl.AsyncHttp;

import com.dachen.common.http2.handle.AbsHandle;
import com.dachen.common.http2.handle.AbsRequestHandle;
import com.dachen.common.http2.request.AbsRequst;
import com.loopj.android.http.RequestHandle;

/**
 * AsyncHttp请求reuqst的持有，可取消任务
 * Created by pqixi on 2016/9/9 0009.
 */
public class AsyncHttpRequestHandle extends AbsRequestHandle<RequestHandle> {

    public AsyncHttpRequestHandle() {
    }

    public AsyncHttpRequestHandle(String URL, AbsRequst requst, AbsHandle mResultHandle, RequestHandle mRealRequestHandle) {
        super(URL, requst, mResultHandle, mRealRequestHandle);
    }

    @Override
    public void cancel() {
        RequestHandle requestHandle = getRealRequestHandle();
        if (requestHandle != null) {
            requestHandle.cancel(true);
        }
    }
}
