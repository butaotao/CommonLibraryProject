package com.dachen.teleconference.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.dachen.imsdk.db.dao.ChatGroupDao;
import com.dachen.imsdk.db.po.ChatGroupPo;
import com.dachen.imsdk.net.SessionGroup;
import com.dachen.teleconference.AgoraManager;
import com.dachen.teleconference.CreateOrJoinMeetingCallBack;
import com.dachen.teleconference.MyAgoraAPICallBack;
import com.dachen.teleconference.bean.CreatePhoneMeetingResponse;
import com.dachen.teleconference.bean.GetMediaDynamicKeyResponse;
import com.dachen.teleconference.bean.ImMeetingBean;
import com.dachen.teleconference.bean.MeetingRole;
import com.dachen.teleconference.bean.MeetingStatus;
import com.dachen.teleconference.http.HttpCommClient;
import com.dachen.teleconference.utils.MeetingInfo;

/**
 * 会议开启帮助类
 * <p>
 * Created by TianWei on 2016/8/31.
 */
public class MeetingOpenHelper {

    private static final int CREATE_PHONE_MEETING = 1001;
    private static final int GET_MEDIADYNAMIC_KEY = 1002;
    private static MeetingOpenHelper INSTANCE;
    private CreateOrJoinMeetingCallBack mCallBack;
    private final Context mContext;
    private String mToken;
    private String mUserId;
    private String mGroupId;
    private String mChannelId;
    private boolean isSponsor;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CREATE_PHONE_MEETING:
                    if (msg.arg1 == 1) {
                        if (msg.obj != null) {
                            CreatePhoneMeetingResponse res = (CreatePhoneMeetingResponse) msg.obj;
                            mChannelId = res.getData();
                            if (TextUtils.isEmpty(mChannelId)) {
                                mCallBack.createOrJoinMeetingFailed("创建会议失败");
                                return;
                            }
                            HttpCommClient.getInstance().getMediaDynamicKey(mContext, mHandler, GET_MEDIADYNAMIC_KEY, mChannelId,
                                    mUserId, "3600");
                        }
                    } else {
                        mCallBack.createOrJoinMeetingFailed((String) msg.obj);
                    }
                    break;

                case GET_MEDIADYNAMIC_KEY:
                    if (msg.arg1 == 1) {
                        if (msg.obj != null) {

                            final String data = ((GetMediaDynamicKeyResponse) msg.obj).getData();
                            AgoraManager.getInstance(mContext).joinChannel(mChannelId, data, Integer.parseInt(mUserId));

                        }
                    } else {
                        mCallBack.createOrJoinMeetingFailed((String) msg.obj);
                    }
                    break;
            }

        }
    };


    private MeetingOpenHelper(Context context) {
        mContext = context;
        AgoraManager.getInstance(context).getAgoraAPICallBack().addAgoraAPICallBack(mMyAgoraAPICallBack);
    }

    public static MeetingOpenHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MeetingOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MeetingOpenHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void createMeeting(String token, String userId, String groupId, CreateOrJoinMeetingCallBack callBack) {
        isSponsor = true;
        mToken = token;
        mUserId = userId;
        mGroupId = groupId;
        mCallBack = callBack;
        updateGroupMember();

    }

    private void updateGroupMember() {
        HttpCommClient.getInstance().createPhoneMeeting(mContext, mHandler, CREATE_PHONE_MEETING, mToken, mUserId, mGroupId);
    }

    public void joinMeeting(String token, String userId, String groupId, String channelId, CreateOrJoinMeetingCallBack callBack) {
        isSponsor = false;
        mToken = token;
        mUserId = userId;
        mGroupId = groupId;
        mCallBack = callBack;
        mChannelId = channelId;
        checkImStatus();
    }

    private void checkImStatus() {
        SessionGroup group = new SessionGroup(mContext);
        group.setCallbackNew(new SessionGroup.SessionGroupCallbackNew() {
            @Override
            public void onGroupInfo(ChatGroupPo po, int what) {
                String meeting = po.meeting;
                ImMeetingBean imMeetingBean = JSON.parseObject(meeting, ImMeetingBean.class);
                if (imMeetingBean == null) {
                    mCallBack.createOrJoinMeetingFailed("加入会议失败,会议状态异常");
                    return;
                }
                if ("1".equals(imMeetingBean.getConfStatus())) {
                    HttpCommClient.getInstance().getMediaDynamicKey(mContext, mHandler, GET_MEDIADYNAMIC_KEY, mChannelId,
                            mUserId, "3600");
                } else {
                    mCallBack.createOrJoinMeetingFailed("加入会议失败,会议已结束");
                }
            }

            @Override
            public void onGroupInfoFailed(String msg) {
                mCallBack.createOrJoinMeetingFailed(msg);
            }
        });
        group.getGroupInfoNew(mGroupId);
    }

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
        public void onChannelJoined(final String channelID) {
            final ChatGroupDao dao = new ChatGroupDao();
            AgoraManager.getInstance(mContext).channelInviteAccept(channelID, "server_37");
            SessionGroup group = new SessionGroup(mContext);
            group.setCallbackNew(new SessionGroup.SessionGroupCallbackNew() {
                @Override
                public void onGroupInfo(ChatGroupPo po, int what) {
                    dao.saveGroup(po);
                    mCallBack.createOrJoinMeetingSuccess(channelID);
                    MeetingInfo.getInstance(mContext).setMeetingChannel(channelID);
                    MeetingInfo.getInstance(mContext).setMeetingRole(isSponsor ? MeetingRole.Sponsor : MeetingRole.Member);
                    MeetingInfo.getInstance(mContext).setMeetingStatus(MeetingStatus.Start);
                }

                @Override
                public void onGroupInfoFailed(String msg) {

                }
            });
            group.getGroupInfoNew(mGroupId);


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
    };

}
