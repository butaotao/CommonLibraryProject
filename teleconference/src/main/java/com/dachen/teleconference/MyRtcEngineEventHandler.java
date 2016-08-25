package com.dachen.teleconference;

import io.agora.rtc.IRtcEngineEventHandler;

/**
 * Created by TianWei on 2016/8/25.
 */
public interface MyRtcEngineEventHandler {

    public void onJoinChannelSuccess(final String channel, final int uid, final int elapsed);

    public void onRejoinChannelSuccess(final String channel, final int uid, final int elapsed) ;
    public void onWarning(final int warn) ;
    public void onError(final int err);

    public void onApiCallExecuted(final String api, final int error);
    public void onCameraReady();
    public void onVideoStopped() ;

    public void onAudioQuality(final int uid, final int quality, final short delay, final short lost);
    public void onLeaveChannel(final IRtcEngineEventHandler.RtcStats stats) ;

    public void onRtcStats(final IRtcEngineEventHandler.RtcStats stats);
    public void onAudioVolumeIndication(final IRtcEngineEventHandler.AudioVolumeInfo[] speakers, final int totalVolume) ;
    public void onNetworkQuality(final int quality);

    public void onUserJoined(final int uid, final int elapsed);
    public void onUserOffline(final int uid, final int reason) ;
    public void onUserMuteAudio(final int uid, final boolean muted);
    public void onUserMuteVideo(final int uid, final boolean muted);
    public void onUserEnableVideo(final int uid, final boolean enabled);

    /**
     * @deprecated
     */
    @Deprecated
    public void onLocalVideoStat(final int sentBitrate, final int sentFrameRate) ;
    /**
     * @deprecated
     */
    @Deprecated
    public void onRemoteVideoStat(final int uid, final int delay, final int receivedBitrate, final int receivedFrameRate) ;
    public void onRemoteVideoStats(final IRtcEngineEventHandler.RemoteVideoStats stats);

    public void onLocalVideoStats(final IRtcEngineEventHandler.LocalVideoStats stats) ;

    public void onFirstRemoteVideoFrame(final int uid, final int width, final int height, final int elapsed);
    public void onFirstLocalVideoFrame(final int width, final int height, final int elapsed) ;

    public void onFirstRemoteVideoDecoded(final int uid, final int width, final int height, final int elapsed) ;

    public void onConnectionLost();

    public void onConnectionInterrupted() ;

    public void onRefreshRecordingServiceStatus(final int status);
}
