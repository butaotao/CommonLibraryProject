package com.dachen.teleconference.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.dachen.imsdk.db.po.ChatGroupPo;
import com.dachen.imsdk.net.SessionGroup;
import com.dachen.teleconference.AgoraManager;
import com.dachen.teleconference.CreateOrJoinMeetingCallBack;
import com.dachen.teleconference.bean.CreatePhoneMeetingResponse;
import com.dachen.teleconference.bean.GetMediaDynamicKeyResponse;
import com.dachen.teleconference.bean.ImMeetingBean;
import com.dachen.teleconference.http.HttpCommClient;

/**
 * 会议开启帮助类
 * <p/>
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
                            AgoraManager.getInstance(mContext).joinChannel(mChannelId,
                                    ((GetMediaDynamicKeyResponse) msg.obj).getData(), Integer.parseInt(mUserId));
                            mCallBack.createOrJoinMeetingSuccess(mChannelId);
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
        mToken = token;
        mUserId = userId;
        mGroupId = groupId;
        mCallBack = callBack;
        updateGroupMember();

    }

    private void updateGroupMember() {
        SessionGroup group = new SessionGroup(mContext);
        group.setCallbackNew(new SessionGroup.SessionGroupCallbackNew() {
            @Override
            public void onGroupInfo(ChatGroupPo po, int what) {
                HttpCommClient.getInstance().createPhoneMeeting(mContext, mHandler, CREATE_PHONE_MEETING, mToken, mUserId, mGroupId);
            }

            @Override
            public void onGroupInfoFailed(String msg) {

            }
        });
        group.getGroupInfoNew(mGroupId);
    }

    public void joinMeeting(String token, String userId, String groupId,String channelId, CreateOrJoinMeetingCallBack callBack) {
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
                if ("1".equals(imMeetingBean.getConfStatus())) {
                    HttpCommClient.getInstance().getMediaDynamicKey(mContext, mHandler, GET_MEDIADYNAMIC_KEY, mChannelId,
                            mUserId, "3600");
                } else {
                    mCallBack.createOrJoinMeetingFailed("加入会议失败,会议已结束");
                }
            }

            @Override
            public void onGroupInfoFailed(String msg) {

            }
        });
        group.getGroupInfoNew(mGroupId);
    }


}
