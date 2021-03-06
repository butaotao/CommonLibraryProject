package com.dachen.teleconference;

import android.content.Context;

import io.agora.AgoraAPI;
import io.agora.AgoraAPIOnlySignal;
import io.agora.rtc.RtcEngine;

/**
 * @author gzhuo
 * @date 2016/8/16
 */
public class AgoraManager {
    private Context mContext;
    private static AgoraManager mInstance;
    private static RtcEngine mRtcEngine;
    private static AgoraAPIOnlySignal mAgoraAPIOnlySignal;
    private static RtcEngineEventHandlerMgr mRtcEngineEventHandlerMgr;
    private static AgoraAPICallBack agoraAPICallBack;

    private AgoraManager() {
    }

    private AgoraManager(Context context) {
        mContext = context.getApplicationContext();
        mRtcEngineEventHandlerMgr = new RtcEngineEventHandlerMgr(mContext);
        agoraAPICallBack = new AgoraAPICallBack(mContext);
    }

    public static AgoraManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (AgoraManager.class) {
                mInstance = new AgoraManager(context);
            }
        }
        return mInstance;
    }

    public void initAgora(String vendorKey) {
        createRtcEngine(vendorKey);
        creatAgoraAPIOnlySignal(vendorKey);
    }

    private RtcEngine createRtcEngine(String vendorKey) {
        if (mRtcEngine == null) {
            mRtcEngine = RtcEngine.create(mContext, vendorKey, mRtcEngineEventHandlerMgr);
            mRtcEngine.monitorHeadsetEvent(true);
            mRtcEngine.monitorConnectionEvent(true);
            mRtcEngine.monitorBluetoothHeadsetEvent(true);
            mRtcEngine.enableHighPerfWifiMode(true);
            mRtcEngine.setEnableSpeakerphone(true);
            mRtcEngine.enableAudioVolumeIndication(500, 1);
        }
        return mRtcEngine;
    }

    private AgoraAPIOnlySignal creatAgoraAPIOnlySignal(String vendorKey) {
        if (mAgoraAPIOnlySignal == null) {
            mAgoraAPIOnlySignal = AgoraAPI.getInstance(mContext, vendorKey);
            mAgoraAPIOnlySignal.callbackSet(agoraAPICallBack);
        }
        return mAgoraAPIOnlySignal;
    }

    public RtcEngineEventHandlerMgr getEventHandlerMgr() {
        return mRtcEngineEventHandlerMgr;
    }

    public AgoraAPICallBack getAgoraAPICallBack() {
        return agoraAPICallBack;
    }


    public void loginAgora(String account, String token, String vendorKey) {
        mAgoraAPIOnlySignal.login(vendorKey, account, token, 0, "");
    }

    public void logoutAgora() {
        mAgoraAPIOnlySignal.logout();
    }

    public void joinChannel(String channel, String dynamicKey, int account) {
        mAgoraAPIOnlySignal.channelJoin(channel);
        mRtcEngine.joinChannel(dynamicKey, channel, "", account);
    }

    public void leaveChannel(String channel) {
        mAgoraAPIOnlySignal.channelLeave(channel);
        mRtcEngine.leaveChannel();
    }

    public void setEnableSpeakerphone(boolean b) {
        mRtcEngine.setEnableSpeakerphone(b);
    }

    public void muteLocalAudioStream(boolean b) {
        mRtcEngine.muteLocalAudioStream(b);
    }

    public void channelInviteAccept(String channelID, String account) {
        mAgoraAPIOnlySignal.channelInviteAccept(channelID, account, 0);
    }

    public void muteRemoteAudioStream(boolean b) {
        mRtcEngine.muteAllRemoteAudioStreams(b);
    }

    public void messageChannelSend(String channelID, String msg, String msgID) {
        mAgoraAPIOnlySignal.messageChannelSend(channelID, msg, msgID);
    }

    public void channelInviteRefuse(String channelID, String account) {
        mAgoraAPIOnlySignal.channelInviteRefuse(channelID, account, 0);
    }

    public void messageInstantSend(String account, int uid, String msg, String msgID) {
        mAgoraAPIOnlySignal.messageInstantSend(account, uid, msg, msgID);
    }


}
