package com.dachen.teleconference.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dachen.common.utils.ToastUtil;
import com.dachen.imsdk.db.po.ChatGroupPo;
import com.dachen.teleconference.AgoraManager;
import com.dachen.teleconference.CreateOrJoinMeetingCallBack;
import com.dachen.teleconference.MeetingBusinessCallBack;
import com.dachen.teleconference.views.OtherMeetingIncomingDialog;

public class ChatAlertActivity extends Activity {
    private static final String INTENT_EXTRA_CREATER_NAME = "creater_name";
    private static final String INTENT_EXTRA_CREATERPIC = "creater_pic";
    private static final String INTENT_EXTRA_CHANNEL_ID = "channel_id";
    private static final String INTENT_EXTRA_CREATER_ID = "creater_id";
    private static final String INTENT_EXTRA_TOKEN = "token";
    private static final String INTENT_EXTRA_USER_ID = "user_id";
    private static final String INTENT_EXTRA_GROUP_ID = "group_id";


    private static ChatAlertActivity instance;
    private ChatGroupPo po;
    private String mCreaterName;
    private String mCreaterPic;
    private String mChannelID;
    private String mCreaterID;
    private String mToken;
    private String mUserId;
    private String mGroupId;
    private static MeetingBusinessCallBack meetingBusinessCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (instance != null) {
            instance.finish();
        }
        instance = this;
        mCreaterID = getIntent().getStringExtra(INTENT_EXTRA_CREATER_ID);
        mCreaterName = getIntent().getStringExtra(INTENT_EXTRA_CREATER_NAME);
        mCreaterPic = getIntent().getStringExtra(INTENT_EXTRA_CREATERPIC);
        mChannelID = getIntent().getStringExtra(INTENT_EXTRA_CHANNEL_ID);
        mToken = getIntent().getStringExtra(INTENT_EXTRA_TOKEN);
        mUserId = getIntent().getStringExtra(INTENT_EXTRA_USER_ID);
        mGroupId = getIntent().getStringExtra(INTENT_EXTRA_GROUP_ID);
        showDialog();
    }

    @Override
    public void finish() {
        super.finish();
        instance = null;
    }

    private void showDialog() {
        OtherMeetingIncomingDialog otherMeetingIncomingDialog = new OtherMeetingIncomingDialog(ChatAlertActivity.this,
                mCreaterName, mCreaterPic, new OtherMeetingIncomingDialog.OtherMeetingIncomingListener() {
            @Override
            public void onAnswerThePhone() {
                answerThePhone();
            }

            @Override
            public void onIgnore() {
                AgoraManager.getInstance(ChatAlertActivity.this).channelInviteRefuse(mChannelID, mCreaterID);
                finish();
            }
        });
        otherMeetingIncomingDialog.show();
    }

    private void answerThePhone() {

        MeetingOpenHelper.getInstance(ChatAlertActivity.this).joinMeeting(mToken, mUserId, mGroupId, mChannelID,
                new CreateOrJoinMeetingCallBack() {
                    @Override
                    public void createOrJoinMeetingSuccess(String channelId) {
                        MeetingActivity.openUI(ChatAlertActivity.this, mToken, mUserId, mCreaterID, mGroupId, mChannelID,
                                false, meetingBusinessCallBack);
                        finish();
                    }

                    @Override
                    public void createOrJoinMeetingFailed(String failMessage) {
                        ToastUtil.showToast(ChatAlertActivity.this, failMessage);
                        finish();
                    }
                });


    }

    public static void openUI(Context context, String createrId, String createrName, String createrPic, String channelId,
                              String token, String userId, String groupId, MeetingBusinessCallBack meetingBusinessCallBack) {
        ChatAlertActivity.meetingBusinessCallBack = meetingBusinessCallBack;
        Intent i = new Intent(context, ChatAlertActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(INTENT_EXTRA_CREATER_ID, createrId);
        i.putExtra(INTENT_EXTRA_CREATER_NAME, createrName);
        i.putExtra(INTENT_EXTRA_CREATERPIC, createrPic);
        i.putExtra(INTENT_EXTRA_CHANNEL_ID, channelId);
        i.putExtra(INTENT_EXTRA_TOKEN, token);
        i.putExtra(INTENT_EXTRA_USER_ID, userId);
        i.putExtra(INTENT_EXTRA_GROUP_ID, groupId);
        context.startActivity(i);
    }
}
