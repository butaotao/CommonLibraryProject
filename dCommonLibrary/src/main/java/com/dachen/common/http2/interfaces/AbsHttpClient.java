package com.dachen.common.http2.interfaces;

import android.content.Context;

import com.dachen.common.http2.handle.AbsHandle;
import com.dachen.common.http2.request.AbsRequst;

/**
 * 网络请求客户端接口，实现gei和post方法
 * Created by pqixi on 2016/9/8 0008.
 */
public interface AbsHttpClient {
    Context getContext();

    void post(String URL, AbsRequst request, AbsHandle handle);

    void get(String URL, AbsRequst request, AbsHandle handle);

    void cancel(String URL, AbsRequst request, AbsHandle handle);

    void resetRealClient();//重置内部的对象,防止切换实现类以后内存泄漏
}
