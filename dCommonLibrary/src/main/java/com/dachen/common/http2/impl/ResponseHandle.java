package com.dachen.common.http2.impl;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.dachen.common.http2.handle.AbsHandle;
import com.dachen.common.http2.result.AbsResult;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

/**
 * Created by pqixi on 2016/9/8 0008.
 */
public class ResponseHandle extends AsyncHttpResponseHandler {
    private AbsHandle mHandle;

    public ResponseHandle(AbsHandle mHandle) {
        this.mHandle = mHandle;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Log.d("monkey", "onSuccess: responseBody = " + new String(responseBody));
        Object result = null;
        try {
            result = JSON.parseObject(responseBody, mHandle.getResultClass());
        } catch (Exception e) {

        }
        mHandle.handleResponse((AbsResult) result);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        Log.d("monkey", "onFailure: responseBody = " + new String(responseBody));
        Object result = null;
        try {
            result = JSON.parseObject(responseBody, mHandle.getResultClass());
        } catch (Exception e) {

        }

        mHandle.handleResponse((AbsResult) result);
    }
}
