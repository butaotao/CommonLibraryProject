package com.dachen.incomelibrary.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.dachen.incomelibrary.R;
import com.dachen.incomelibrary.utils.Constants;
import com.dachen.incomelibrary.utils.Logger;
import com.dachen.incomelibrary.utils.UserInfo;

public class BaseActivity extends Activity {
    protected View mLoadingView;
    private AnimationDrawable mAnimationDrawable;
    public ProgressDialog mDialog;
    protected static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        MActivityManager.getInstance().pushActivity(this);
        initProgressDialog();
        this.context = getApplicationContext();
        getHistoryIp();
    }

    private void getHistoryIp() {
        String keyNet = UserInfo.getInstance(this).getKeyNet();
        if (!TextUtils.isEmpty(keyNet)) {
            if (keyNet.equals("120.24.94.126")) {
                Constants.changeIp("120.24.94.126");
            } else if (keyNet.equals("112.74.208.140")) {
                Constants.changeIp("112.74.208.140");
            } else if (keyNet.equals("192.168.3.7")) {
                Constants.changeIp("192.168.3.7");
            }
        }
        Logger.d("KEYNET",keyNet+Constants.IP);
    }

    public void showLoadingDialog() {

        if (null != mDialog) {
            mDialog.show();
        }
    }

    public void closeLoadingDialog() {

        if (null != mDialog) {
            mDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        MActivityManager.getInstance().popActivity(this);
    }

    private void initProgressDialog() {
        mDialog = new ProgressDialog(this, R.style.IMDialog);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setMessage("正在加载");
    }
}
