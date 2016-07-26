package com.dachen.common.toolbox;

/**
 * Created by Mcp on 2016/2/19.
 */
public interface OnCommonRequestListener {
    boolean onTokenErr();
    boolean onUpdateVersionErr(String msg);
}
