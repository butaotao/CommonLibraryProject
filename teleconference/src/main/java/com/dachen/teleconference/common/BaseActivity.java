package com.dachen.teleconference.common;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.dachen.common.AppManager;
import com.dachen.imsdk.net.ImPolling;
import com.dachen.teleconference.http.Constants;
import com.dachen.teleconference.utils.MeetingInfo;


/**
 * 基础UI
 *
 * @author yehj
 */
public class BaseActivity extends FragmentActivity {

    public Context mContext;
    public ProgressDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        AppManager.getAppManager().addActivity(this);
        initProgressDialog();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
        getHistoryIp();
    }

    private void getHistoryIp() {
        String keyNet = MeetingInfo.getInstance(this).getKeyNet();
        if (!TextUtils.isEmpty(keyNet)) {
            if (keyNet.equals("120.24.94.126")) {
                Constants.changeIp("120.24.94.126");
            } else if (keyNet.equals("112.74.208.140")) {
                Constants.changeIp("112.74.208.140");
            } else if (keyNet.equals("192.168.3.7")) {
                Constants.changeIp("192.168.3.7");
            } else if (keyNet.equals("xg.mediportal.com.cn")) {
                Constants.changeIp("xg.mediportal.com.cn");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImPolling.getInstance().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ImPolling.getInstance().onPause();
    }

    private void initProgressDialog() {
        mDialog = new ProgressDialog(this, com.dachen.common.R.style.IMDialog);
        //		mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setMessage("正在加载");
    }

}
