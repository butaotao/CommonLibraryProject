package com.dachen.common.http2.handle;

import com.dachen.common.http2.handle.AbsHandle;
import com.dachen.common.http2.request.AbsRequst;

import java.lang.ref.WeakReference;

/**
 * Created by pqixi on 2016/9/9 0009.
 *
 * @param <T> 请求以后返回的renquest对象的封装
 */
public abstract class AbsRequestHandle<T> {
    private String URL = null;
    private WeakReference<AbsRequst> request = null;
    private WeakReference<AbsHandle> mResultHandle = null;

    protected T mRealRequestHandle;

    public abstract void cancel();//   取消任务

    public AbsRequestHandle() {

    }

    public AbsRequestHandle(String URL, AbsRequst requst, AbsHandle mResultHandle, T mRealRequestHandle) {
        this.URL = URL;
        this.request = new WeakReference<AbsRequst>(requst);
        this.mResultHandle = new WeakReference<AbsHandle>(mResultHandle);
        this.mRealRequestHandle = mRealRequestHandle;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public AbsRequst getRequest() {
        if (request != null) {
            return request.get();
        }
        return null;
    }


    public AbsHandle getHandle() {
        if (mResultHandle != null) {
            return mResultHandle.get();
        }
        return null;
    }

    public void setRequest(AbsRequst request) {
        this.request = new WeakReference<AbsRequst>(request);
    }

    public void setHandle(AbsHandle handle) {
        this.mResultHandle = new WeakReference<AbsHandle>(handle);
    }

    public T getmRealRequestHandle() {
        return mRealRequestHandle;
    }

    public void setmRealRequestHandle(T mRealRequestHandle) {
        this.mRealRequestHandle = mRealRequestHandle;
    }
}
