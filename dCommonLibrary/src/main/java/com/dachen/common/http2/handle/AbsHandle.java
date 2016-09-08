package com.dachen.common.http2.handle;

import com.dachen.common.http2.result.AbsResult;

/**
 * Created by pqixi on 2016/9/8 0008.
 */
public interface AbsHandle<T extends AbsResult> {
    void handleResponse(T absResult);
    Class<T> getResultClass();
}
