package com.dachen.teleconference.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.dachen.common.utils.Logger;
import com.dachen.teleconference.AgoraManager;
import com.dachen.teleconference.bean.User;
import com.dachen.teleconference.common.BaseActivity;
import com.dachen.teleconference.views.FloatingView;
import com.dachen.teleconference.views.RoomView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import com.dachen.teleconference.R;

/**
 * 会议界面
 *
 * @author gzhuo
 * @date 2016/8/17
 */
public class MeetingActivity extends BaseActivity {
    private static final String TAG = MeetingActivity.class.getSimpleName();
    private String mVendorKey;
    private String mDynamicKey;
    private String mChannelId;//房间号
    private String mUserId;
    private AgoraManager mAgoraManager;
    private RtcEngine mRtcEngine;
    private RtcEngineEventHandler mRtcEngineEventHandler;
    private RoomView mRoomView;
    private TextView mLeftBtn;
    private TextView mTitle;
    private TextView mRightBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        initVariables();

        initView();

        initRtcEngine();

        joinChannel();
    }

    private void initView() {
        mLeftBtn = (TextView) findViewById(R.id.left_btn);
        mLeftBtn.setText("隐藏");
        mLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText("电话会议");
        mRightBtn = (TextView) findViewById(R.id.right_btn);
        mRightBtn.setText("全部静音");

        mRoomView = (RoomView) findViewById(R.id.roomView);

        List<List<User>> data = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            List<User> userList = new ArrayList<>();
            for(int j = 0; j < 10; j++){
                User user = new User();
                user.id = (i+ 1) *(j + 1) + "";
                user.name = "王宝强 " + (i+ 1) *(j + 1);
                user.head = "http://h.hiphotos.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=1904b984fdfaaf5190ee89eded3dff8b/aec379310a55b3193cdb93d743a98226cffc1775.jpg";
                userList.add(user);
            }
            data.add(userList);
        }

        mRoomView.setData(data);
    }

    private void hide() {
        FloatingView floatingView = new FloatingView(this);
        floatingView.show();

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

        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
            ImageLoader.getInstance().init(config);
        }
    }

    private void initRtcEngine() {
        /*mAgoraManager = AgoraManager.getInstance(this);
        mAgoraManager.createRtcEngine(mVendorKey);
        mRtcEngine = mAgoraManager.getRtcEngine();
        mRtcEngineEventHandler = new RtcEngineEventHandler();
        mAgoraManager.getEventHandlerMgr().addRtcEngineEventHandler(mRtcEngineEventHandler);*/
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
