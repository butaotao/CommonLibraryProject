package com.dachen.common.http2.impl;

import android.content.Context;

import com.dachen.common.http.HttpsSupportCompat;
import com.dachen.common.http2.handle.AbsHandle;
import com.dachen.common.http2.request.AbsRequst;
import com.dachen.common.http2.interfaces.AbsHttpClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.Map;

/**
 * AsyncHttp框架的封装，实现自身的网络请求接口
 * Created by pqixi on 2016/9/8 0008.
 */
public class AsyncHttpClientImpl implements AbsHttpClient {

    //实际的请求Asynchttp对象,单例模式
    private static AsyncHttpClient mRealClient;
    private Context context;

    public AsyncHttpClientImpl(Context context) {
        this.context = context;
        if (mRealClient == null) {
            synchronized (AsyncHttpClientImpl.class) {
                if (mRealClient == null) {
                    mRealClient = new AsyncHttpClient();
                    new HttpsSupportCompat(this.context).compatAsyncHttp(mRealClient.getHttpClient());
                }
            }
        }
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void post(String URL, AbsRequst request, AbsHandle handle) {
        mRealClient.post(URL, getRequestParams(request), new ResponseHandle(handle));
    }

    @Override
    public void get(String URL, AbsRequst request, AbsHandle handle) {

        mRealClient.get(URL, getRequestParams(request), new ResponseHandle(handle));
    }

    @Override
    public void resetRealClient() {
        mRealClient = null;//置空对象，防止内存泄漏
    }

    /**
     * 装换参数
     *
     * @param request
     * @return
     */
    private RequestParams getRequestParams(AbsRequst request) {
        if (request == null)
            return null;

        Map<String, String> map = request.toMap();
        if (map == null)
            return null;
        RequestParams params = new RequestParams();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            params.put(entry.getKey(), entry.getValue());
        }
        return params;
    }
}
