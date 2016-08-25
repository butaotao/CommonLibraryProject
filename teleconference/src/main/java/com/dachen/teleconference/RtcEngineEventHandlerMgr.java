package com.dachen.teleconference;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import io.agora.rtc.IRtcEngineEventHandler;

/**
 * 回调事件管理器，agora的回调事件是在子线程中执行的，为方便使用，这里将它抛到主线程
 *
 * @author gzhuo
 * @date 2016/8/16
 */
public class RtcEngineEventHandlerMgr extends IRtcEngineEventHandler {
    private MyRtcEngineEventHandler myRtcEngineEventHandler;
    private Context mContext;
    private Handler mMainHandler;

    public RtcEngineEventHandlerMgr(Context context) {
        mContext = context.getApplicationContext();
        mMainHandler = new Handler(mContext.getMainLooper());
    }

    public void addRtcEngineEventHandler(MyRtcEngineEventHandler myRtcEngineEventHandler) {
        if (myRtcEngineEventHandler!= null) {
            this.myRtcEngineEventHandler=myRtcEngineEventHandler;
        }
    }

    //----------------- interface start -----------------//
    @Override
    public void onJoinChannelSuccess(final String channel, final int uid, final int elapsed) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onJoinChannelSuccess(channel, uid, elapsed);
                }
            });
        }

    }

    @Override
    public void onRejoinChannelSuccess(final String channel, final int uid, final int elapsed) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onRejoinChannelSuccess(channel,uid,elapsed);
                }
            });
        }
    }

    @Override
    public void onWarning(final int warn) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onWarning(warn);
                }
            });
        }
    }

    @Override
    public void onError(final int err) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onError(err);
                }
            });
        }
    }

    @Override
    public void onApiCallExecuted(final String api, final int error) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onApiCallExecuted(api,error);
                }
            });
        }
    }

    @Override
    public void onCameraReady() {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onCameraReady();
                }
            });
        }
    }

    @Override
    public void onVideoStopped() {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onVideoStopped();
                }
            });
        }
    }

    @Override
    public void onAudioQuality(final int uid, final int quality, final short delay, final short lost) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onAudioQuality(uid,quality,delay,lost);
                }
            });
        }
    }

    @Override
    public void onLeaveChannel(final IRtcEngineEventHandler.RtcStats stats) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onLeaveChannel(stats);
                }
            });
        }
    }

    @Override
    public void onRtcStats(final IRtcEngineEventHandler.RtcStats stats) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onRtcStats(stats);
                }
            });
        }
    }

    @Override
    public void onAudioVolumeIndication(final IRtcEngineEventHandler.AudioVolumeInfo[] speakers, final int totalVolume) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onAudioVolumeIndication(speakers,totalVolume);
                }
            });
        }
    }

    @Override
    public void onNetworkQuality(final int quality) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onNetworkQuality(quality);
                }
            });
        }
    }

    @Override
    public void onUserJoined(final int uid, final int elapsed) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onUserJoined(uid,elapsed);
                }
            });
        }
    }

    @Override
    public void onUserOffline(final int uid, final int reason) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onUserOffline(uid,reason);
                }
            });
        }
    }

    @Override
    public void onUserMuteAudio(final int uid, final boolean muted) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onUserMuteAudio(uid,muted);
                }
            });
        }
    }

    @Override
    public void onUserMuteVideo(final int uid, final boolean muted) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onUserMuteVideo(uid,muted);
                }
            });
        }
    }

    @Override
    public void onUserEnableVideo(final int uid, final boolean enabled) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onUserEnableVideo(uid,enabled);
                }
            });
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    @Override
    public void onLocalVideoStat(final int sentBitrate, final int sentFrameRate) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onLocalVideoStat(sentBitrate,sentFrameRate);
                }
            });
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    @Override
    public void onRemoteVideoStat(final int uid, final int delay, final int receivedBitrate, final int receivedFrameRate) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onRemoteVideoStat(uid,delay,receivedBitrate,receivedFrameRate);
                }
            });
        }
    }

    @Override
    public void onRemoteVideoStats(final IRtcEngineEventHandler.RemoteVideoStats stats) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onRemoteVideoStats(stats);
                }
            });
        }
    }

    @Override
    public void onLocalVideoStats(final IRtcEngineEventHandler.LocalVideoStats stats) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onLocalVideoStats(stats);
                }
            });
        }
    }

    @Override
    public void onFirstRemoteVideoFrame(final int uid, final int width, final int height, final int elapsed) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onFirstRemoteVideoFrame(uid,width,height,elapsed);
                }
            });
        }
    }

    @Override
    public void onFirstLocalVideoFrame(final int width, final int height, final int elapsed) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onFirstLocalVideoFrame(width,height,elapsed);
                }
            });
        }
    }

    @Override
    public void onFirstRemoteVideoDecoded(final int uid, final int width, final int height, final int elapsed) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onFirstRemoteVideoDecoded(uid,width,height,elapsed);
                }
            });
        }
    }

    @Override
    public void onConnectionLost() {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onConnectionLost();
                }
            });
        }
    }

    @Override
    public void onConnectionInterrupted() {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onConnectionInterrupted();
                }
            });
        }
    }

    @Override
    public void onRefreshRecordingServiceStatus(final int status) {
        if(myRtcEngineEventHandler!=null){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myRtcEngineEventHandler.onRefreshRecordingServiceStatus(status);
                }
            });
        }
    }

    //----------------- interface end -----------------//


}
