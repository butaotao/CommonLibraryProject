package com.dachen.teleconference.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dachen.imsdk.db.po.ChatGroupPo;
import com.dachen.teleconference.views.OtherMeetingIncomingDialog;

public class ChatAlertActivity extends Activity {
    private static final String INTENT_EXTRA_CREATER_NAME = "creater_name";
    private static final String INTENT_EXTRA_CREATERPIC = "creater_pic";
    private static final String INTENT_EXTRA_CHANNEL_ID = "channel_id";


    private static ChatAlertActivity instance;
    private ChatGroupPo po;
    private String mCreaterName;
    private String mCreaterPic;
    private String mChannelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (instance != null) {
            instance.finish();
        }
        instance = this;
        mCreaterName = getIntent().getStringExtra(INTENT_EXTRA_CREATER_NAME);
        mCreaterPic = getIntent().getStringExtra(INTENT_EXTRA_CREATERPIC);
        mChannelId = getIntent().getStringExtra(INTENT_EXTRA_CHANNEL_ID);
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
                finish();
            }

            @Override
            public void onIgnore() {
                finish();
            }
        });
        otherMeetingIncomingDialog.show();
    }

    public static void openUI(Context context, String createrName, String createrPic, String channelId) {
        Intent i = new Intent(context, ChatAlertActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(INTENT_EXTRA_CREATER_NAME, createrName);
        i.putExtra(INTENT_EXTRA_CREATERPIC, createrPic);
        i.putExtra(INTENT_EXTRA_CHANNEL_ID, channelId);
        context.startActivity(i);
    }
}
