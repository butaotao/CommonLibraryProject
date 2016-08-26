package com.dachen.teleconference.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.common.utils.Logger;
import com.dachen.common.utils.ToastUtil;
import com.dachen.common.utils.UIHelper;
import com.dachen.imsdk.entity.GroupInfo2Bean;
import com.dachen.teleconference.AgoraManager;
import com.dachen.teleconference.MyAgoraAPICallBack;
import com.dachen.teleconference.MyRtcEngineEventHandler;
import com.dachen.teleconference.R;
import com.dachen.teleconference.adapter.UserAdapter;
import com.dachen.teleconference.bean.CreatePhoneMeetingResponse;
import com.dachen.teleconference.bean.GetMediaDynamicKeyResponse;
import com.dachen.teleconference.bean.GetSigningKeyResponse;
import com.dachen.teleconference.common.BaseActivity;
import com.dachen.teleconference.http.HttpCommClient;
import com.dachen.teleconference.views.CallMeetingMemberDialog;
import com.dachen.teleconference.views.FloatingView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import io.agora.rtc.IRtcEngineEventHandler;

/**
 * 会议界面
 *
 * @author gzhuo
 * @date 2016/8/17
 */
public class MeetingActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MeetingActivity.class.getSimpleName();
    private static final String INTENT_EXTRA_GROUP_USER_LIST = "user_list";
    private static final String INTENT_EXTRA_USER_ID = "user_id";
    private static final String INTENT_EXTRA_GROUP_ID = "group_id";
    private static final String INTENT_EXTRA_TOKEN = "token";
    private static final int GET_SIGNNING_KEY = 1001;
    private static final int GET_MEDIADYNAMIC_KEY = 1002;
    private static final int CREATE_PHONE_MEETING = 1003;
    private RecyclerView mRecyclerView;
    private TextView mLeftBtn;
    private TextView mTitle;
    private TextView mRightBtn;
    private ImageView mSpeakerIv;
    private ImageView mHangIv;
    private ImageView mMutIv;
    private boolean isSpeakerOn = true;
    private boolean isMutOn = false;
    private UserAdapter mAdapter;
    private List<GroupInfo2Bean.Data.UserInfo> mUserInfos = new ArrayList<>();
    private String mUserId;

    private static final String vendorKey = "86c6c121ff444021a5152b0a791aefd3";
    public static final String signKey = "b9aec320141347c6b64226cb7e901d23";
    private String mChannelId;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_SIGNNING_KEY:
                    if (msg.arg1 == 1) {
                        if (msg.obj != null) {
                            GetSigningKeyResponse res = (GetSigningKeyResponse) msg.obj;
                            String data = res.getData();
                            AgoraManager.getInstance(MeetingActivity.this).loginAgora(mUserId, data, vendorKey);
                        }
                    } else {
                        UIHelper.ToastMessage(MeetingActivity.this, (String) msg.obj);
                    }

                    break;

                case CREATE_PHONE_MEETING:
                    if (msg.arg1 == 1) {
                        if (msg.obj != null) {
                            CreatePhoneMeetingResponse res = (CreatePhoneMeetingResponse) msg.obj;
                            mChannelId = res.getData();
                            HttpCommClient.getInstance().getMediaDynamicKey(mContext, mHandler, GET_MEDIADYNAMIC_KEY, mChannelId,
                                    mUserId, "3600");
                        }
                    } else {
                        UIHelper.ToastMessage(MeetingActivity.this, (String) msg.obj);
                    }
                    break;

                case GET_MEDIADYNAMIC_KEY:
                    if (msg.arg1 == 1) {
                        if (msg.obj != null) {
                            AgoraManager.getInstance(MeetingActivity.this).joinChannel(mChannelId,
                                    ((GetMediaDynamicKeyResponse) msg.obj).getData(), Integer.parseInt(mUserId));
                        }
                    } else {
                        UIHelper.ToastMessage(MeetingActivity.this, (String) msg.obj);
                    }
                    break;

            }


        }
    };
    private String mToken;
    private String mGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        initVariables();

        initView();

        initAgoraConfigure();

        loginAndjoinChannel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AgoraManager.getInstance(MeetingActivity.this).logoutAgora();
    }

    private void initView() {
        mLeftBtn = (TextView) findViewById(R.id.left_btn);
        mTitle = (TextView) findViewById(R.id.title);
        mRightBtn = (TextView) findViewById(R.id.right_btn);
        mSpeakerIv = (ImageView) findViewById(R.id.speaker_iv);
        mHangIv = (ImageView) findViewById(R.id.hang_iv);
        mMutIv = (ImageView) findViewById(R.id.mut_iv);
        mRecyclerView = (RecyclerView) findViewById(R.id.room_view);


        mLeftBtn.setText("隐藏");
        mTitle.setText("电话会议");
        mRightBtn.setText("全部静音");

        mLeftBtn.setOnClickListener(this);
        mRightBtn.setOnClickListener(this);
        mSpeakerIv.setOnClickListener(this);
        mHangIv.setOnClickListener(this);
        mMutIv.setOnClickListener(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(MeetingActivity.this, 4));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new GridDividerItemDecoration(2, 3));

        mAdapter = new UserAdapter(MeetingActivity.this, mUserInfos, mUserId);
        mAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (position == 0) {
                    ToastUtil.showToast(MeetingActivity.this, "添加成员");
                    return;
                }
                GroupInfo2Bean.Data.UserInfo userInfo = mUserInfos.get(position - 1);
                CallMeetingMemberDialog callMeetingMemberDialog = new CallMeetingMemberDialog(MeetingActivity.this, userInfo.name,
                        userInfo.pic);
                callMeetingMemberDialog.show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);

    }

    private void hide() {
        FloatingView floatingView = new FloatingView(this);
        floatingView.show();

    }

    /**
     * 登录并加入房间
     */
    private void loginAndjoinChannel() {
        HttpCommClient.getInstance().getSigningKey(this, mHandler, GET_SIGNNING_KEY, mUserId, "3600");
    }

    private void initVariables() {
        Intent intent = getIntent();
        mToken = intent.getStringExtra(INTENT_EXTRA_TOKEN);
        List<GroupInfo2Bean.Data.UserInfo> userInfos = (List<GroupInfo2Bean.Data.UserInfo>) intent.getSerializableExtra(
                INTENT_EXTRA_GROUP_USER_LIST);
        mUserId = intent.getStringExtra(INTENT_EXTRA_USER_ID);
        mGroupId = intent.getStringExtra(INTENT_EXTRA_GROUP_ID);
        for (GroupInfo2Bean.Data.UserInfo info : userInfos) {
            if (info.id.equals(mUserId)) {
                mUserInfos.clear();
                mUserInfos.add(info);
            }
        }
        for (GroupInfo2Bean.Data.UserInfo info : userInfos) {
            if (!info.id.equals(mUserId)) {
                mUserInfos.add(info);
            }
        }
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
            ImageLoader.getInstance().init(config);
        }
    }


    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.left_btn) {
            hide();
        } else if (i == R.id.right_btn) {
            ToastUtil.showToast(MeetingActivity.this, "全部静音");
        } else if (i == R.id.speaker_iv) {
            setSpeaker();
        } else if (i == R.id.hang_iv) {
            AgoraManager.getInstance(mContext).leaveChannel("1001");
        } else if (i == R.id.mut_iv) {
            setMut();
        }

    }

    private void setSpeaker() {
        if (isSpeakerOn) {
            isSpeakerOn = false;
            mSpeakerIv.setImageResource(R.drawable.speaker_close);
        } else {
            isSpeakerOn = true;
            mSpeakerIv.setImageResource(R.drawable.speaker_on);
        }
        AgoraManager.getInstance(mContext).setEnableSpeakerphone(isSpeakerOn);
    }

    private void setMut() {
        if (isMutOn) {
            isMutOn = false;
            mMutIv.setImageResource(R.drawable.mut_close);
        } else {
            isMutOn = true;
            mMutIv.setImageResource(R.drawable.mut_on);
        }
        AgoraManager.getInstance(mContext).muteLocalAudioStream(isMutOn);
    }


    public static void openUI(Context context, String token, String userId, String groupId,
                              ArrayList<GroupInfo2Bean.Data.UserInfo> userList) {
        Intent intent = new Intent(context, MeetingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_EXTRA_TOKEN, token);
        intent.putExtra(INTENT_EXTRA_USER_ID, userId);
        intent.putExtra(INTENT_EXTRA_GROUP_ID, groupId);
        intent.putExtra(INTENT_EXTRA_GROUP_USER_LIST, userList);
        context.startActivity(intent);
    }

    private void initAgoraConfigure() {
        AgoraManager.getInstance(this).initAgora(vendorKey);
        AgoraManager.getInstance(this).getEventHandlerMgr().addRtcEngineEventHandler(new MyRtcEngineEventHandler() {
            @Override
            public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
                ToastUtil.showToast(mContext,
                        "onChannelJoined" + "---" + "channel--" + channel + "uid--" + uid + "elapsed--" + elapsed);

            }

            @Override
            public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {

            }

            @Override
            public void onWarning(int warn) {

            }

            @Override
            public void onError(int err) {

            }

            @Override
            public void onApiCallExecuted(String api, int error) {

            }

            @Override
            public void onCameraReady() {

            }

            @Override
            public void onVideoStopped() {

            }

            @Override
            public void onAudioQuality(int uid, int quality, short delay, short lost) {

            }

            @Override
            public void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats) {

            }

            @Override
            public void onRtcStats(IRtcEngineEventHandler.RtcStats stats) {

            }

            @Override
            public void onAudioVolumeIndication(IRtcEngineEventHandler.AudioVolumeInfo[] speakers, int totalVolume) {
                updateSpeakers(speakers);
            }

            @Override
            public void onNetworkQuality(int quality) {

            }

            @Override
            public void onUserJoined(int uid, int elapsed) {

            }

            @Override
            public void onUserOffline(int uid, int reason) {

            }

            @Override
            public void onUserMuteAudio(int uid, boolean muted) {

            }

            @Override
            public void onUserMuteVideo(int uid, boolean muted) {

            }

            @Override
            public void onUserEnableVideo(int uid, boolean enabled) {

            }

            @Override
            public void onLocalVideoStat(int sentBitrate, int sentFrameRate) {

            }

            @Override
            public void onRemoteVideoStat(int uid, int delay, int receivedBitrate, int receivedFrameRate) {

            }

            @Override
            public void onRemoteVideoStats(IRtcEngineEventHandler.RemoteVideoStats stats) {

            }

            @Override
            public void onLocalVideoStats(IRtcEngineEventHandler.LocalVideoStats stats) {

            }

            @Override
            public void onFirstRemoteVideoFrame(int uid, int width, int height, int elapsed) {

            }

            @Override
            public void onFirstLocalVideoFrame(int width, int height, int elapsed) {

            }

            @Override
            public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {

            }

            @Override
            public void onConnectionLost() {

            }

            @Override
            public void onConnectionInterrupted() {

            }

            @Override
            public void onRefreshRecordingServiceStatus(int status) {

            }
        });

        AgoraManager.getInstance(this).getAgoraAPICallBack().addAgoraAPICallBack(new MyAgoraAPICallBack() {
            @Override
            public void onReconnecting(int nretry) {

            }

            @Override
            public void onReconnected(int fd) {

            }

            @Override
            public void onLoginSuccess(int uid, int fd) {
                ToastUtil.showToast(mContext, "登录成功");
                HttpCommClient.getInstance().createPhoneMeeting(mContext, mHandler, CREATE_PHONE_MEETING, mToken, mUserId,
                        mGroupId);

            }

            @Override
            public void onLogout(int ecode) {

            }

            @Override
            public void onLoginFailed(int ecode) {

            }

            @Override
            public void onChannelJoined(String channelID) {
                ToastUtil.showToast(mContext, "onChannelJoined" + "---" + "channelID" + channelID);
            }

            @Override
            public void onChannelJoinFailed(String channelID, int ecode) {

            }

            @Override
            public void onChannelLeaved(String channelID, int ecode) {

            }

            @Override
            public void onChannelUserJoined(String account, int uid) {

            }

            @Override
            public void onChannelUserLeaved(String account, int uid) {

            }

            @Override
            public void onChannelUserList(String[] accounts, int[] uids) {

            }

            @Override
            public void onChannelQueryUserNumResult(String channelID, int ecode, int num) {

            }

            @Override
            public void onChannelAttrUpdated(String channelID, String name, String value, String type) {

            }

            @Override
            public void onInviteReceived(String channelID, String account, int uid) {

            }

            @Override
            public void onInviteReceivedByPeer(String channelID, String account, int uid) {

            }

            @Override
            public void onInviteAcceptedByPeer(String channelID, String account, int uid) {

            }

            @Override
            public void onInviteRefusedByPeer(String channelID, String account, int uid) {

            }

            @Override
            public void onInviteFailed(String channelID, String account, int uid, int ecode) {

            }

            @Override
            public void onInviteEndByPeer(String channelID, String account, int uid) {

            }

            @Override
            public void onInviteEndByMyself(String channelID, String account, int uid) {

            }

            @Override
            public void onMessageSendError(String messageID, int ecode) {

            }

            @Override
            public void onMessageSendSuccess(String messageID) {

            }

            @Override
            public void onMessageAppReceived(String msg) {

            }

            @Override
            public void onMessageInstantReceive(String account, int uid, String msg) {

            }

            @Override
            public void onMessageChannelReceive(String channelID, String account, int uid, String msg) {

            }

            @Override
            public void onLog(String txt) {

            }

            @Override
            public void onInvokeRet(String name, int ofu, String reason, String resp) {

            }

            @Override
            public void onMsg(String from, String t, String msg) {

            }

            @Override
            public void onUserAttrResult(String account, String name, String value) {

            }

            @Override
            public void onUserAttrAllResult(String account, String value) {

            }

            @Override
            public void onError(String name, int ecode, String desc) {

            }
        });
    }

    private void updateSpeakers(IRtcEngineEventHandler.AudioVolumeInfo[] speakers) {

        for (IRtcEngineEventHandler.AudioVolumeInfo speaker : speakers) {
            Logger.d("MeetingActivity", "speaker--" + speaker.uid + "voice--" + speaker.volume);
            for (int i = 0; i < mUserInfos.size(); i++) {
                GroupInfo2Bean.Data.UserInfo userInfo = mUserInfos.get(i);
                if ((speaker.uid + "").equals(userInfo.id)) {
                    userInfo.volume = speaker.volume;
                    mAdapter.notifyItemChanged(i + 1);
                }
            }
        }

    }
}
