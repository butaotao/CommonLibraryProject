package com.dachen.teleconference.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dachen.common.utils.Logger;
import com.dachen.common.utils.UIHelper;
import com.dachen.common.widget.CustomDialog;
import com.dachen.imsdk.db.dao.ChatGroupDao;
import com.dachen.imsdk.db.po.ChatGroupPo;
import com.dachen.imsdk.entity.GroupInfo2Bean;
import com.dachen.teleconference.AgoraManager;
import com.dachen.teleconference.MediaMessage;
import com.dachen.teleconference.MeetingBusinessCallBack;
import com.dachen.teleconference.MyAgoraAPICallBack;
import com.dachen.teleconference.MyRtcEngineEventHandler;
import com.dachen.teleconference.R;
import com.dachen.teleconference.adapter.MessageListAdapter;
import com.dachen.teleconference.adapter.UserAdapter;
import com.dachen.teleconference.bean.ChannelMemberStatusBean;
import com.dachen.teleconference.bean.DelayMeetingMsgBean;
import com.dachen.teleconference.bean.ImMeetingBean;
import com.dachen.teleconference.bean.MeetingStatus;
import com.dachen.teleconference.bean.event.ChatGroupEvent;
import com.dachen.teleconference.common.BaseActivity;
import com.dachen.teleconference.http.HttpCommClient;
import com.dachen.teleconference.utils.MeetingInfo;
import com.dachen.teleconference.views.CallMeetingMemberDialog;
import com.dachen.teleconference.views.FloatingView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot1.event.EventBus;
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
    private static final String INTENT_EXTRA_CHANNEL_ID = "channel_id";
    private static final int GET_SIGNNING_KEY = 1001;
    private static final int GET_MEDIADYNAMIC_KEY = 1002;
    private static final int CREATE_PHONE_MEETING = 1003;
    private static final String INTENT_EXTRA_CREATE_ID = "create_id";
    private static final int DISMISS_CONF = 1004;
    private static final int VOIP_CALL = 1005;
    private static final String INTENT_EXTRA_IS_CREATOR = "is_creator";
    private static final int DELAY_CONF = 1006;
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
    private List<GroupInfo2Bean.Data.UserInfo> mUserInfos = new ArrayList<>();//IM成员list
    private List<GroupInfo2Bean.Data.UserInfo> mChannelUserList = new ArrayList<>();//频道成员list
    private List<String> mMessageData = new ArrayList<>();//频道消息list
    private Map<GroupInfo2Bean.Data.UserInfo, String> mShowDialogUserMap = new HashMap<>();//自动弹出拨叫界面框的用户Map
    private ListView mMessageListView;
    private String mUserId;
    private String mToken;
    private String mGroupId;
    private static final String vendorKey = "dad9d47a486645bea56d5f9a892bc4c2";
    public static final String signKey = "edf0d436d2f24276bac3c5ee1b30647d";
    private String mChannelId;
    private boolean isSponsor;
    private String mCreateId;
    private String mCreateName;
    private static MeetingBusinessCallBack meetingBusinessCallBack;
    public static final int REQUEST_CODE_UPDATE_GROUP = 10001;
    private MessageListAdapter mMessageListAdapter;
    private long mStartTime;
    private int timeCount;
    private boolean isAllMut = false;
    private TextView mTimeTv;
    private int mMeetingTime;
    private int mMinTime;
    private int mSecTime;
    private ProgressDialog mEndMeetingDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DISMISS_CONF:
                    if (msg.arg1 == 1) {
                        leaveChannel();
                        AgoraManager.getInstance(mContext).messageChannelSend(mChannelId, MediaMessage.MEETING_EDN, "");

                    } else {
                        UIHelper.ToastMessage(MeetingActivity.this, (String) msg.obj);
                    }
                    break;

                case VOIP_CALL:
                    if (msg.arg1 == 1) {

                    } else {
                        UIHelper.ToastMessage(MeetingActivity.this, (String) msg.obj);
                    }
                    break;
                case DELAY_CONF:
                    if (msg.arg1 == 1) {

                    } else {
                        UIHelper.ToastMessage(MeetingActivity.this, (String) msg.obj);
                    }
                    break;

                case 0x1:
                    if (timeCount != 0) {
                        timeCount++;
                        mMeetingTime += 1;
                        setTime();
                        mHandler.sendEmptyMessageDelayed(0x1, 1000);
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        initVariables();

        initView();

        initAgoraConfigure();

        EventBus.getDefault().register(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeCount = 0;
        EventBus.getDefault().unregister(this);
        AgoraManager.getInstance(this).getEventHandlerMgr().removeRtcEngineEventHandler(mMyRtcEngineEventHandler);
        AgoraManager.getInstance(this).getAgoraAPICallBack().removeAgoraAPICallBack(mMyAgoraAPICallBack);
    }


    /**
     * 初始化参数
     */
    private void initVariables() {
        Intent intent = getIntent();
        mToken = intent.getStringExtra(INTENT_EXTRA_TOKEN);
        mUserId = intent.getStringExtra(INTENT_EXTRA_USER_ID);
        mGroupId = intent.getStringExtra(INTENT_EXTRA_GROUP_ID);
        mCreateId = intent.getStringExtra(INTENT_EXTRA_CREATE_ID);
        mChannelId = getIntent().getStringExtra(INTENT_EXTRA_CHANNEL_ID);
        if (TextUtils.isEmpty(mCreateId)) {
            mCreateId = mUserId;
        }
        ChatGroupDao dao = new ChatGroupDao();
        ChatGroupPo po = dao.queryForId(mGroupId);
        String meeting = po.meeting;
        ImMeetingBean imMeetingBean = JSON.parseObject(meeting, ImMeetingBean.class);

        if (imMeetingBean != null) {
            if ("1".equals(imMeetingBean.getConfStatus())) {
                mStartTime = imMeetingBean.getCreateTime();
                long l = System.currentTimeMillis();
                if (l > mStartTime) {
                    mMeetingTime = (int) (l - mStartTime) / 1000;
                }
            } else {
                mStartTime = System.currentTimeMillis();
                mMeetingTime = 0;
            }
        }

        String groupUsers = po.groupUsers;
        List<GroupInfo2Bean.Data.UserInfo> userInfos = JSON.parseArray(groupUsers,
                GroupInfo2Bean.Data.UserInfo.class);

        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
            ImageLoader.getInstance().init(config);
        }

        if (userInfos == null) {
            return;
        }

        /**
         * 会议发起人排第一位
         */
        for (GroupInfo2Bean.Data.UserInfo info : userInfos) {
            if (info.id.equals(mCreateId)) {
                mUserInfos.clear();
                info.netOnLine = true;
                mUserInfos.add(info);
                mCreateName = info.name;
                mChannelUserList.clear();
                mChannelUserList.add(info);
            }
        }

        for (GroupInfo2Bean.Data.UserInfo info : userInfos) {
            if (mCreateId != mUserId) {
                if (info.id.equals(mUserId)) {
                    info.netOnLine = true;
                    mUserInfos.add(info);
                    mChannelUserList.add(info);
                }
            }
        }
        for (GroupInfo2Bean.Data.UserInfo info : userInfos) {
            if (!info.id.equals(mUserId) && !info.id.equals(mCreateId)) {
                mUserInfos.add(info);
            }
        }


    }

    /**
     * 初始化view
     */
    private void initView() {
        mEndMeetingDialog = new ProgressDialog(this, R.style.IMDialog);
        mEndMeetingDialog.setMessage("正在结束会议");
        mEndMeetingDialog.setCanceledOnTouchOutside(false);
        mLeftBtn = (TextView) findViewById(R.id.left_btn);
        mTitle = (TextView) findViewById(R.id.title);
        mRightBtn = (TextView) findViewById(R.id.right_btn);
        mSpeakerIv = (ImageView) findViewById(R.id.speaker_iv);
        mHangIv = (ImageView) findViewById(R.id.hang_iv);
        mMutIv = (ImageView) findViewById(R.id.mut_iv);
        mTimeTv = (TextView) findViewById(R.id.time);
        mRecyclerView = (RecyclerView) findViewById(R.id.room_view);
        mMessageListView = (ListView) findViewById(R.id.message_list_view);
        mMessageListAdapter = new MessageListAdapter(mContext, mMessageData);
        mMessageListView.setAdapter(mMessageListAdapter);

        mLeftBtn.setText("隐藏");
        mTitle.setText(mCreateName + "的电话会议" + "（" + mUserInfos.size() + "）");
        mRightBtn.setText("全部静音");

        setTime();
        timeCount++;
        mHandler.sendEmptyMessageDelayed(0x1, 1000);


        mLeftBtn.setOnClickListener(this);
        mRightBtn.setOnClickListener(this);
        mSpeakerIv.setOnClickListener(this);
        mHangIv.setOnClickListener(this);
        mMutIv.setOnClickListener(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(MeetingActivity.this, 4));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new UserAdapter(MeetingActivity.this, mUserInfos, mCreateId);
        mAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (position == 0) {
                    meetingBusinessCallBack.addPersonIntoMeeting(MeetingActivity.this, mGroupId);
                    return;
                }
                final GroupInfo2Bean.Data.UserInfo userInfo = mUserInfos.get(position - 1);
                //重新邀请未加入人员;
                if (isSponsor && !userInfo.netOnLine) {
                    CallMeetingMemberDialog callMeetingMemberDialog = new CallMeetingMemberDialog(MeetingActivity.this,
                            userInfo.name, userInfo.pic, new CallMeetingMemberDialog.CallMeetingListener() {
                        @Override
                        public void onNetCall() {
                            HttpCommClient.getInstance().voipCall(mContext, mHandler, VOIP_CALL, userInfo.id, mGroupId,
                                    mChannelId);
                        }

                        @Override
                        public void onPhoneCall() {

                        }
                    });
                    callMeetingMemberDialog.show();
                }

            }
        });
        mRecyclerView.setAdapter(mAdapter);

        if (getIntent().getBooleanExtra(INTENT_EXTRA_IS_CREATOR, false)) {//邀请者
            mMessageData.add("呼叫中~");
            mMessageListAdapter.notifyDataSetChanged();
            isSponsor = true;
            mRightBtn.setVisibility(View.VISIBLE);
            //            mLeftBtn.setVisibility(View.GONE);
        } else {//参会者
            //            mLeftBtn.setVisibility(View.VISIBLE);
            isSponsor = false;
            mChannelId = getIntent().getStringExtra(INTENT_EXTRA_CHANNEL_ID);
            mRightBtn.setVisibility(View.GONE);
        }


    }


    /**
     * 初始化Agora设置
     */
    private void initAgoraConfigure() {
        //        AgoraManager.getInstance(this).initAgora(vendorKey);
        AgoraManager.getInstance(this).getEventHandlerMgr().addRtcEngineEventHandler(mMyRtcEngineEventHandler);
        AgoraManager.getInstance(this).getAgoraAPICallBack().addAgoraAPICallBack(mMyAgoraAPICallBack);
    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.left_btn) {
            // hide();
            finish();
        } else if (i == R.id.right_btn) {
            if (isAllMut) {
                mRightBtn.setText("全部静音");
                for (GroupInfo2Bean.Data.UserInfo info : mUserInfos) {
                    if (info.id != mCreateId) {
                        AgoraManager.getInstance(mContext).messageInstantSend(info.id, 0, MediaMessage.ALL_MUT_CANCEL, "");
                    }
                }
                mMessageData.add(mCreateName + "解除全员静音");
                mMessageListAdapter.notifyDataSetChanged();
                isAllMut = false;
            } else {
                mRightBtn.setText("取消静音");
                for (GroupInfo2Bean.Data.UserInfo info : mUserInfos) {
                    if (info.id != mCreateId) {
                        AgoraManager.getInstance(mContext).messageInstantSend(info.id, 0, MediaMessage.ALL_MUT_ON, "");
                    }
                }
                mMessageData.add(mCreateName + "开启全员静音");
                mMessageListAdapter.notifyDataSetChanged();
                isAllMut = true;
            }
        } else if (i == R.id.speaker_iv) {
            setSpeaker();
        } else if (i == R.id.hang_iv) {
            if (isSponsor) {
                showSponsorDialog();
            } else {
                showFinishDialog();
            }
        } else if (i == R.id.mut_iv) {
            setMut();
        }

    }

    private void hide() {
        FloatingView floatingView = new FloatingView(this);
        floatingView.show();

    }

    /**
     * 发起人结束会议
     */
    private void showSponsorDialog() {
        CustomDialog hintDialog = new CustomDialog.Builder(MeetingActivity.this, new CustomDialog.CustomClickEvent() {
            @Override
            public void onDismiss(CustomDialog hintDialog) {
                hintDialog.dismiss();
            }

            @Override
            public void onClick(CustomDialog hintDialog) {
                hintDialog.dismiss();
                mEndMeetingDialog.show();
                HttpCommClient.getInstance().dismissConf(mContext, mHandler, DISMISS_CONF, mToken, mGroupId, mChannelId);
            }

        }).setMessage("结束会议,所有参会人员都会离开会议,确定结束吗？").setPositive("确定").setNegative("取消").create();
        hintDialog.show();
    }

    /**
     * 参会人离开会议
     */

    private void showFinishDialog() {
        CustomDialog hintDialog = new CustomDialog.Builder(MeetingActivity.this, new CustomDialog.CustomClickEvent() {
            @Override
            public void onDismiss(CustomDialog hintDialog) {
                hintDialog.dismiss();
            }

            @Override
            public void onClick(CustomDialog hintDialog) {
                hintDialog.dismiss();
                AgoraManager.getInstance(mContext).leaveChannel(mChannelId);
                MeetingInfo.getInstance(mContext).setMeetingStatus(MeetingStatus.End);
                finish();
            }

        }).setMessage("会议还未结束,你确定要离开吗？").setPositive("确定").setNegative("取消").create();
        hintDialog.show();
    }

    /**
     * 设置会议进行的时间
     */
    private void setTime() {

        if (mTimeTv == null) {
            return;
        }

        mMinTime = (int) mMeetingTime / 60;
        mSecTime = (int) mMeetingTime % 60;
        mTimeTv.setText("会议已进行" + mMinTime + ":" + mSecTime);
    }


    /**
     * 设置扬声器开关
     */

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

    /**
     * 设置静音开关
     */
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

    /**
     * 更新用户头像音量大小
     *
     * @param speakers
     */
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


    /**
     * Agora媒体回调
     */
    private MyRtcEngineEventHandler mMyRtcEngineEventHandler = new MyRtcEngineEventHandler() {
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
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
    };

    /**
     * Agora信令回调
     */
    private MyAgoraAPICallBack mMyAgoraAPICallBack = new MyAgoraAPICallBack() {
        @Override
        public void onReconnecting(int nretry) {

        }

        @Override
        public void onReconnected(int fd) {

        }

        @Override
        public void onLoginSuccess(int uid, int fd) {

        }

        @Override
        public void onLogout(int ecode) {

        }

        @Override
        public void onLoginFailed(int ecode) {

        }

        @Override
        public void onChannelJoined(String channelID) {
        }

        @Override
        public void onChannelJoinFailed(String channelID, int ecode) {

        }

        @Override
        public void onChannelLeaved(String channelID, int ecode) {

        }

        @Override
        public void onChannelUserJoined(String account, int uid) {
            updateOnlineUser(account);
        }

        @Override
        public void onChannelUserLeaved(String account, int uid) {
            updateOffLineUser(account);
        }

        @Override
        public void onChannelUserList(String[] accounts, int[] uids) {
            updateOnlineUsers(accounts);
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
            updateRefuseStatus(channelID, account);
        }


        @Override
        public void onInviteFailed(String channelID, String account, int uid, int ecode) {

        }

        @Override
        public void onInviteEndByPeer(String channelID, String account, int uid) {
            if (!isSponsor) {
                leaveChannel();
            }
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
            if (msg.equals(MediaMessage.ALL_MUT_ON)) {
                if (!isSponsor) {
                    isMutOn = true;
                    mMutIv.setImageResource(R.drawable.mut_on);
                    AgoraManager.getInstance(mContext).muteLocalAudioStream(isMutOn);
                }
                mMessageData.add(mCreateName + "开启全员静音");
                mMessageListAdapter.notifyDataSetChanged();
            } else if (msg.equals(MediaMessage.ALL_MUT_CANCEL)) {
                if (!isSponsor) {
                    isMutOn = false;
                    mMutIv.setImageResource(R.drawable.mut_close);
                    AgoraManager.getInstance(mContext).muteLocalAudioStream(isMutOn);
                }
                mMessageData.add(mCreateName + "解除全员静音");
                mMessageListAdapter.notifyDataSetChanged();
            } else {
                mMessageData.add(msg);
                mMessageListAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onMessageChannelReceive(String channelID, String account, int uid, String msg) {
            Logger.d("onMessageChannelReceive", "onMessageChannelReceive---" + "msg----" +
                    msg + "account---" + account + "channelID-----" + channelID);

            if (msg.equals(MediaMessage.MEETING_EDN)) {
                mMessageData.add("会议结束");
                mMessageListAdapter.notifyDataSetChanged();
            }
            /**
             * 根据服务端返回的频道消息获取更改会议成员头像状态
             */
            if ("server_37".equals(account)) {
                setUserStatus(msg);
                setDelayConf(msg);
            }


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
    };

    /**
     * 通过server发送的频道信息,弹出延长时间对话框
     *
     * @param msg
     */
    private void setDelayConf(String msg) {
        DelayMeetingMsgBean delayMeetingMsgBean = JSON.parseObject(msg, DelayMeetingMsgBean.class);
        if (delayMeetingMsgBean != null) {
            final CustomDialog customDialog = new CustomDialog.Builder(mContext, new CustomDialog.CustomClickEvent() {
                @Override
                public void onClick(CustomDialog customDialog) {
                    customDialog.dismiss();
                    HttpCommClient.getInstance().delayConf(mContext, mHandler, DELAY_CONF, mChannelId, mCreateId, "");
                }

                @Override
                public void onDismiss(CustomDialog customDialog) {
                    customDialog.dismiss();
                }
            }).setTitle(delayMeetingMsgBean.getParam().getLeftTime() + "分钟后会议将自动结束，是否延时").setMessage("默认延时30分钟,到点继续提示")
                    .setPositive("好的").setNegative("不延时").create();
            customDialog.show();

        }

    }

    /**
     * 更新正忙信息(拒绝接听)
     *
     * @param channelID
     * @param account
     */
    private void updateRefuseStatus(String channelID, String account) {
        String name = "";
        for (GroupInfo2Bean.Data.UserInfo info : mUserInfos) {
            if (account.equals(info.id)) {
                name = info.name;
            }
        }
        for (GroupInfo2Bean.Data.UserInfo info : mUserInfos) {
            AgoraManager.getInstance(mContext).messageInstantSend(info.id, 0, name + "在忙碌", "");
        }
    }


    /**
     * 通过server发送的频道信息,更新会议成员头像状态,并弹框
     *
     * @param msg
     */
    private void setUserStatus(String msg) {
        List<ChannelMemberStatusBean> statusBeanList = JSON.parseArray(msg, ChannelMemberStatusBean.class);
        if (statusBeanList != null && statusBeanList.size() > 0) {
            for (GroupInfo2Bean.Data.UserInfo info : mUserInfos) {
                for (ChannelMemberStatusBean bean : statusBeanList) {
                    if (bean.getMember().equals(info.id)) {
                        if (!mChannelUserList.contains(info)) {
                            info.netOnLine = false;
                            mChannelUserList.add(info);
                            if (!mShowDialogUserMap.containsKey(info)) {
                                mShowDialogUserMap.put(info, "0");
                            }

                        }
                    }
                }
            }

            if (isSponsor) {
                showNetCallDialog();
            }
            mAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 自动弹出网络超时dialog
     */
    private void showNetCallDialog() {
        for (Map.Entry<GroupInfo2Bean.Data.UserInfo, String> entry : mShowDialogUserMap.entrySet()) {
            if ("0".equals(entry.getValue())) {
                final GroupInfo2Bean.Data.UserInfo info = entry.getKey();
                CallMeetingMemberDialog callMeetingMemberDialog = new CallMeetingMemberDialog(MeetingActivity.this,
                        info.name, info.pic, new CallMeetingMemberDialog.CallMeetingListener() {
                    @Override
                    public void onNetCall() {
                        HttpCommClient.getInstance().voipCall(mContext, mHandler, VOIP_CALL, info.id, mGroupId, mChannelId);
                        showNetCallDialog();
                    }

                    @Override
                    public void onPhoneCall() {
                        showNetCallDialog();
                    }
                });
                callMeetingMemberDialog.show();
                entry.setValue("1");
                break;
            }
        }

    }

    /**
     * 收到结束会议通知,离开会议频道
     */
    private void leaveChannel() {
        AgoraManager.getInstance(mContext).leaveChannel(mChannelId);
        MeetingInfo.getInstance(mContext).setMeetingStatus(MeetingStatus.End);
        if (mEndMeetingDialog != null && mEndMeetingDialog.isShowing()) {
            mEndMeetingDialog.dismiss();
        }
        finish();
    }

    /**
     * 更新离线成员
     *
     * @param account
     */
    private void updateOffLineUser(String account) {
        String userName = "";
        for (GroupInfo2Bean.Data.UserInfo info : mUserInfos) {
            if (account.equals(info.id)) {
                info.netOnLine = false;
                info.volume = 0;
                userName = info.name;
                if (mChannelUserList.contains(info)) {
                    mChannelUserList.remove(info);
                }
            }
        }
        mMessageData.add(userName + "离开了会议");
        mMessageListAdapter.notifyDataSetChanged();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 更新单个在线成员
     *
     * @param account
     */

    private void updateOnlineUser(String account) {
        String userName = "";
        for (GroupInfo2Bean.Data.UserInfo info : mUserInfos) {
            if (account.equals(info.id)) {
                info.netOnLine = true;
                userName = info.name;
                if (!mChannelUserList.contains(info)) {
                    mChannelUserList.add(info);
                }
            }
        }
        mMessageData.add(userName + "加入了会议");
        mMessageListAdapter.notifyDataSetChanged();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 更新多个在线成员
     *
     * @param accounts
     */
    private void updateOnlineUsers(String[] accounts) {
        for (GroupInfo2Bean.Data.UserInfo info : mUserInfos) {
            for (String account : accounts) {
                if (account.equals(info.id)) {
                    info.netOnLine = true;
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_CODE_UPDATE_GROUP && data != null) {
            List<GroupInfo2Bean.Data.UserInfo> userInfos = (List<GroupInfo2Bean.Data.UserInfo>) data.getSerializableExtra(
                    "selectList");
            if (userInfos != null && userInfos.size() > 0) {
                for (GroupInfo2Bean.Data.UserInfo info : userInfos) {
                    HttpCommClient.getInstance().voipCall(MeetingActivity.this, mHandler, VOIP_CALL, info.id, mGroupId,
                            mChannelId);
                    AgoraManager.getInstance(mContext).messageInstantSend(info.id, 0, mCreateName + "邀请" + info.name + "加入会议",
                            "");
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        return;
    }

    public static void openUI(Context context, String token, String userId, String groupId, String channelId, boolean isCreator,
                              MeetingBusinessCallBack meetingBusinessCallBack) {
        MeetingActivity.meetingBusinessCallBack = meetingBusinessCallBack;
        Intent intent = new Intent(context, MeetingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_EXTRA_TOKEN, token);
        intent.putExtra(INTENT_EXTRA_USER_ID, userId);
        intent.putExtra(INTENT_EXTRA_GROUP_ID, groupId);
        intent.putExtra(INTENT_EXTRA_CHANNEL_ID, channelId);
        intent.putExtra(INTENT_EXTRA_IS_CREATOR, isCreator);
        context.startActivity(intent);
    }

    public static void openUI(Context context, String token, String userId, String createId, String groupId, String channelId,
                              boolean isCreator, MeetingBusinessCallBack meetingBusinessCallBack) {
        MeetingActivity.meetingBusinessCallBack = meetingBusinessCallBack;
        Intent intent = new Intent(context, MeetingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_EXTRA_TOKEN, token);
        intent.putExtra(INTENT_EXTRA_USER_ID, userId);
        intent.putExtra(INTENT_EXTRA_CREATE_ID, createId);
        intent.putExtra(INTENT_EXTRA_GROUP_ID, groupId);
        intent.putExtra(INTENT_EXTRA_CHANNEL_ID, channelId);
        intent.putExtra(INTENT_EXTRA_IS_CREATOR, isCreator);
        context.startActivity(intent);
    }

    public void onEventMainThread(ChatGroupEvent event) {
        if (mGroupId == null || mUserInfos == null || mAdapter == null) {
            return;
        }
        ChatGroupPo group = event.group;
        if (mGroupId.equals(group.groupId)) {
            ChatGroupDao dao = new ChatGroupDao();
            ChatGroupPo po = dao.queryForId(mGroupId);
            String meeting = po.meeting;
            String groupUsers = po.groupUsers;
            List<GroupInfo2Bean.Data.UserInfo> userInfos = JSON.parseArray(groupUsers,
                    GroupInfo2Bean.Data.UserInfo.class);
            if (userInfos != null && userInfos.size() > mUserInfos.size()) {
                int i = userInfos.size() - mUserInfos.size();
                List<GroupInfo2Bean.Data.UserInfo> subList = userInfos.subList(userInfos.size() - i, userInfos.size());
                mUserInfos.addAll(subList);
                mAdapter.notifyDataSetChanged();
            }
        }

    }
}
