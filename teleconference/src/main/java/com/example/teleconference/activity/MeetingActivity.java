package com.example.teleconference.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.dachen.common.utils.Logger;
import com.example.teleconference.AgoraManager;
import com.example.teleconference.R;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

/**
 * 会议界面
 *
 * @author gzhuo
 * @date 2016/8/17
 */
public class MeetingActivity extends Activity {
    private static final String TAG = MeetingActivity.class.getSimpleName();
    private String mVendorKey;
    private String mDynamicKey;
    private String mChannelId;//房间号
    private String mUserId;
    private AgoraManager mAgoraManager;
    private RtcEngine mRtcEngine;
    private RtcEngineEventHandler mRtcEngineEventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        initVariables();

        initRtcEngine();

        joinChannel();
    }

    /**
     * 加入房间
     */
    private void joinChannel() {
        mRtcEngine.joinChannel(mDynamicKey, mChannelId, "", Integer.parseInt(mUserId));
    }

    private void initVariables() {
        Intent intent = getIntent();
        mVendorKey = intent.getStringExtra("vendor_key");
        mDynamicKey = intent.getStringExtra("dynamic_key");
        mChannelId = intent.getStringExtra("channel_id");
        mUserId = intent.getStringExtra("user_id");
    }

    private void initRtcEngine() {
        mAgoraManager = AgoraManager.getInstance(this);
        mAgoraManager.createRtcEngine(mVendorKey);
        mRtcEngine = mAgoraManager.getRtcEngine();
        mRtcEngineEventHandler = new RtcEngineEventHandler();
        mAgoraManager.getEventHandlerMgr().addRtcEngineEventHandler(mRtcEngineEventHandler);
    }

    /**
     * agora事件回调
     */
    class RtcEngineEventHandler extends IRtcEngineEventHandler {

        @Override
        public void onUserJoined(int uid, int elapsed) {
            Logger.d(TAG, "onUserJoined uid = " + uid);
        }

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            Logger.d(TAG, "onJoinChannelSuccess uid = " + uid);
        }

        @Override
        public void onLeaveChannel(RtcStats stats) {
            Logger.d(TAG, "onLeaveChannel");
        }
    }
}
