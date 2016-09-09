package com.dachen.common.http2;


import com.dachen.common.http2.handle.AbsHandle;
import com.dachen.common.http2.impl.AsyncHttp.AsyncHttpClientImpl;
import com.dachen.common.http2.interfaces.AbsHttpClient;
import com.dachen.common.http2.handle.AbsRequestHandle;
import com.dachen.common.http2.request.AbsRequst;

/**
 * 网络请求管理类
 * Created by pqixi on 2016/9/8 0008.
 */
public final class HttpRequestWrap {

    private static AbsHttpClient mClient;

    public static void setAbsHttpClient(AbsHttpClient client) {
        if (client == null) {
            return;
        }
        if (mClient != null) {
            mClient.resetRealClient();
        }
        mClient = client;
    }

    private HttpRequestWrap() {

    }

    private static AbsHttpClient getClient() {
        if (mClient == null) {
            synchronized (AbsHttpClient.class) {
                if (mClient == null)
                    mClient = new AsyncHttpClientImpl(null);
            }
        }
        return mClient;
    }

    public static AbsRequestHandle post(String URL, AbsRequst request, AbsHandle handle) {
        return getClient().post(URL, request, handle);
    }

    public static AbsRequestHandle get(String URL, AbsRequst request, AbsHandle handle) {
        return getClient().get(URL, request, handle);
    }

    public static void cancelAll() {
        getClient().cancelAll();
    }

    ;
}
