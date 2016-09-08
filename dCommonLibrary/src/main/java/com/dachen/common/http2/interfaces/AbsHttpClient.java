package com.dachen.common.http2.interfaces;

import android.content.Context;

import com.dachen.common.http2.handle.AbsHandle;
import com.dachen.common.http2.request.AbsRequst;

/**
 * Created by pqixi on 2016/9/8 0008.
 */
public interface AbsHttpClient {
    Context getContext();
    void post(String URL, AbsRequst request, AbsHandle handle);
    void get(String URL, AbsRequst request, AbsHandle handle);
}
