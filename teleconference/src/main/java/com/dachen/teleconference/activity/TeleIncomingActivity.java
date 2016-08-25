package com.dachen.teleconference.activity;

import android.app.Activity;
import android.os.Bundle;

import com.dachen.teleconference.R;

/**
 * 接收到电话邀请界面
 * Created by TianWei on 2016/8/23.
 */
public class TeleIncomingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_tele_incoming);
    }
}
