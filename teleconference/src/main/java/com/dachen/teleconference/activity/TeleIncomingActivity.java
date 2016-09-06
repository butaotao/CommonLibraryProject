package com.dachen.teleconference.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.common.media.SoundPlayer;
import com.dachen.common.utils.ToastUtil;
import com.dachen.teleconference.AgoraManager;
import com.dachen.teleconference.CreateOrJoinMeetingCallBack;
import com.dachen.teleconference.MediaMessage;
import com.dachen.teleconference.MeetingBusinessCallBack;
import com.dachen.teleconference.R;
import com.dachen.teleconference.constants.ImageLoaderConfig;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 接收到电话邀请界面
 * Created by TianWei on 2016/8/23.
 */
public class TeleIncomingActivity extends Activity implements View.OnClickListener {

    private static final String INTENT_EXTRA_CREATE_ID = "creater_id";
    private static final String INTENT_EXTRA_GROUP_ID = "group_id";
    private static final String INTENT_EXTRA_CHANNEL_ID = "channel_id";
    private static final String INTENT_EXTRA_CREATE_NAME = "create_name";
    private static final String INTENT_EXTRA_CREATE_PIC = "create_pic";
    private static final String INTENT_EXTRA_TOKEN = "token";
    private static final String INTENT_EXTRA_USER_ID = "user_id";
    private SoundPlayer mSoundPlayer;
    private ImageView mRefuseIv;
    private ImageView mResponseIv;
    private String mCreateName;
    private String mCreatePic;
    private TextView mNameTv;
    private ImageView mHeadImageIv;
    private String mToken;
    private String mGroupId;
    private String mCreateID;
    private String mChannelID;
    private String mUserId;
    private int reckonTime = 60;
    private static MeetingBusinessCallBack meetingBusinessCallBack;

    private Handler mReckonHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0x1) {
                reckonTime--;
                if (reckonTime < 0) {
                    mReckonHandler.sendEmptyMessage(0x2);
                } else {
                    mReckonHandler.sendEmptyMessageDelayed(0x1, 1000);
                }
            } else if (msg.what == 0x2) {// 60秒结束
                mSoundPlayer.stop();
                finish();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
        initView();
        initSound();
        mReckonHandler.sendEmptyMessage(0x1);
    }

    private void initVariables() {
        Intent intent = getIntent();
        mToken = intent.getStringExtra(INTENT_EXTRA_TOKEN);
        mCreateName = intent.getStringExtra(INTENT_EXTRA_CREATE_NAME);
        mCreatePic = intent.getStringExtra(INTENT_EXTRA_CREATE_PIC);
        mGroupId = intent.getStringExtra(INTENT_EXTRA_GROUP_ID);
        mCreateID = intent.getStringExtra(INTENT_EXTRA_CREATE_ID);
        mChannelID = intent.getStringExtra(INTENT_EXTRA_CHANNEL_ID);
        mUserId = intent.getStringExtra(INTENT_EXTRA_USER_ID);
    }


    private void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );

        setContentView(R.layout.activity_tele_incoming);
        mNameTv = (TextView) findViewById(R.id.name);
        mHeadImageIv = (ImageView) findViewById(R.id.head_image);


        mRefuseIv = (ImageView) findViewById(R.id.refuse_tele_call);
        mResponseIv = (ImageView) findViewById(R.id.response_tele_call);
        mRefuseIv.setOnClickListener(this);
        mResponseIv.setOnClickListener(this);
        mNameTv.setText(mCreateName);
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
            ImageLoader.getInstance().init(config);
        }
        ImageLoader.getInstance().displayImage(mCreatePic, mHeadImageIv, ImageLoaderConfig.mCircleImageOptions);

    }

    private void initSound() {
        mSoundPlayer = new SoundPlayer(this);
        mSoundPlayer.play(getSoundUri());
    }


    public static void openUI(Context context, String token, String channelId, String userId, String createrId, String
            createrName, String createrPic, String groupId, MeetingBusinessCallBack meetingBusinessCallBack) {
        TeleIncomingActivity.meetingBusinessCallBack = meetingBusinessCallBack;
        Intent intent = new Intent(context, TeleIncomingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_EXTRA_TOKEN, token);
        intent.putExtra(INTENT_EXTRA_CHANNEL_ID, channelId);
        intent.putExtra(INTENT_EXTRA_USER_ID, userId);
        intent.putExtra(INTENT_EXTRA_CREATE_ID, createrId);
        intent.putExtra(INTENT_EXTRA_CREATE_NAME, createrName);
        intent.putExtra(INTENT_EXTRA_CREATE_PIC, createrPic);
        intent.putExtra(INTENT_EXTRA_GROUP_ID, groupId);
        context.startActivity(intent);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Uri getSoundUri() {
        return Uri.parse("android.resource://" + getPackageName() + "/raw/" + "video_call_incoming");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.refuse_tele_call) {
            mSoundPlayer.stop();
            AgoraManager.getInstance(TeleIncomingActivity.this).channelInviteRefuse(mChannelID, mCreateID);
            AgoraManager.getInstance(TeleIncomingActivity.this).messageChannelSend(mChannelID, MediaMessage.INVITE_REFUSE,
                    mUserId);
            finish();
        } else if (id == R.id.response_tele_call) {
            mSoundPlayer.stop();
            MeetingOpenHelper.getInstance(TeleIncomingActivity.this).joinMeeting(mToken, mUserId, mGroupId,mChannelID,
                    new CreateOrJoinMeetingCallBack() {
                        @Override
                        public void createOrJoinMeetingSuccess(String channelId) {
                            MeetingActivity.openUI(TeleIncomingActivity.this, mToken, mUserId, mCreateID, mGroupId, mChannelID,
                                    false, meetingBusinessCallBack);
                            finish();
                        }

                        @Override
                        public void createOrJoinMeetingFailed(String failMessage) {
                            ToastUtil.showToast(TeleIncomingActivity.this, failMessage);
                            finish();
                        }
                    });

        }
    }
}
