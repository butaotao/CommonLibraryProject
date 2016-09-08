package com.dachen.common.http2.impl;

import android.content.Context;

import com.dachen.common.http2.handle.AbsHandle;
import com.dachen.common.http2.handle.ResponseHandle;
import com.dachen.common.http2.request.AbsRequst;
import com.dachen.common.http2.interfaces.AbsHttpClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.Map;

/**
 * Created by pqixi on 2016/9/8 0008.
 */
public class AsyncHttpClientImpl implements AbsHttpClient {

    private Context context;

    public AsyncHttpClientImpl(Context context) {
        this.context = context;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void post(String URL, AbsRequst request, AbsHandle handle) {
        Map<String, String> map = request.toMap();
        RequestParams params = new RequestParams();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                params.put(entry.getKey(), entry.getValue());
            }
        }
        new AsyncHttpClient().post(URL, params, new ResponseHandle(handle));
    }

    @Override
    public void get(String URL, AbsRequst request, AbsHandle handle) {

    }
}
