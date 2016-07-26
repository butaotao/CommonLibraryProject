package com.dachen.common.async;

/**
 * Created by Mcp on 2016/6/6.
 */
public interface SimpleResultListener {
    void onSuccess();
    void onError(String msg);
}
