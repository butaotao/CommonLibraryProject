package com.dachen.common.http2;


import com.dachen.common.http2.impl.AsyncHttpClientImpl;
import com.dachen.common.http2.interfaces.AbsHttpClient;

/**
 * 网络请求管理类
 * Created by pqixi on 2016/9/8 0008.
 */
public class HttpClientManager {

    private static AbsHttpClient mClient;

    public static void setAbsHttpClient(AbsHttpClient client) {
        mClient = client;
    }

    public static AbsHttpClient getClient() {
        if (mClient == null) {
            synchronized (AbsHttpClient.class) {
                if (mClient == null)
                    mClient = new AsyncHttpClientImpl(null);
            }
        }
        return mClient;
    }
}
