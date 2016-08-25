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
    private List<IRtcEngineEventHandler> mRtcEngineEventHandlers = new ArrayList<>();
    private Context mContext;
    private Handler mMainHandler;

    public RtcEngineEventHandlerMgr(Context context) {
        mContext = context.getApplicationContext();
        mMainHandler = new Handler(mContext.getMainLooper());
    }

    public void addRtcEngineEventHandler(IRtcEngineEventHandler eventHandler) {
        if (eventHandler != null) {
            mRtcEngineEventHandlers.add(eventHandler);
        }
    }

    public void removeRtcEngineEventHandler(IRtcEngineEventHandler eventHandler) {
        mRtcEngineEventHandlers.remove(eventHandler);
    }


    //----------------- interface start -----------------//
    @Override
    public void onJoinChannelSuccess(final String channel, final int uid, final int elapsed) {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onJoinChannelSuccess(channel, uid, elapsed);
                }
            });
        }

    }

    @Override
    public void onRejoinChannelSuccess(final String channel, final int uid, final int elapsed) {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onRejoinChannelSuccess(channel, uid, elapsed);
                }
            });
        }
    }

    @Override
    public void onWarning(final int warn) {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onWarning(warn);
                }
            });
        }
    }

    @Override
    public void onError(final int err) {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onError(err);
                }
            });
        }
    }

    @Override
    public void onApiCallExecuted(final String api, final int error) {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onApiCallExecuted(api, error);
                }
            });
        }
    }

    @Override
    public void onCameraReady() {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onCameraReady();
                }
            });
        }
    }

    @Override
    public void onVideoStopped() {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onVideoStopped();
                }
            });
        }
    }

    @Override
    public void onAudioQuality(final int uid, final int quality, final short delay, final short lost) {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onAudioQuality(uid, quality, delay, lost);
                }
            });
        }
    }

    @Override
    public void onLeaveChannel(final IRtcEngineEventHandler.RtcStats stats) {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onLeaveChannel(stats);
                }
            });
        }
    }

    @Override
    public void onRtcStats(final IRtcEngineEventHandler.RtcStats stats) {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onRtcStats(stats);
                }
            });
        }
    }

    @Override
    public void onAudioVolumeIndication(final IRtcEngineEventHandler.AudioVolumeInfo[] speakers, final int totalVolume) {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onAudioVolumeIndication(speakers, totalVolume);
                }
            });
        }
    }

    @Override
    public void onNetworkQuality(final int quality) {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onNetworkQuality(quality);
                }
            });
        }
    }

    @Override
    public void onUserJoined(final int uid, final int elapsed) {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onUserJoined(uid, elapsed);
                }
            });
        }
    }

    @Override
    public void onUserOffline(final int uid, final int reason) {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onUserOffline(uid, reason);
                }
            });
        }
    }

    @Override
    public void onUserMuteAudio(final int uid, final boolean muted) {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onUserMuteAudio(uid, muted);
                }
            });
        }
    }

    @Override
    public void onUserMuteVideo(final int uid, final boolean muted) {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onUserMuteVideo(uid, muted);
                }
            });
        }
    }

    @Override
    public void onUserEnableVideo(final int uid, final boolean enabled) {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onUserEnableVideo(uid, enabled);
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
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onLocalVideoStat(sentBitrate, sentFrameRate);
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
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onRemoteVideoStat(uid, delay, receivedBitrate, receivedFrameRate);
                }
            });
        }
    }

    @Override
    public void onRemoteVideoStats(final IRtcEngineEventHandler.RemoteVideoStats stats) {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onRemoteVideoStats(stats);
                }
            });
        }
    }

    @Override
    public void onLocalVideoStats(final IRtcEngineEventHandler.LocalVideoStats stats) {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onLocalVideoStats(stats);
                }
            });
        }
    }

    @Override
    public void onFirstRemoteVideoFrame(final int uid, final int width, final int height, final int elapsed) {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onFirstRemoteVideoFrame(uid, width, height, elapsed);
                }
            });
        }
    }

    @Override
    public void onFirstLocalVideoFrame(final int width, final int height, final int elapsed) {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onFirstLocalVideoFrame(width, height, elapsed);
                }
            });
        }
    }

    @Override
    public void onFirstRemoteVideoDecoded(final int uid, final int width, final int height, final int elapsed) {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onFirstRemoteVideoDecoded(uid, width, height, elapsed);
                }
            });
        }
    }

    @Override
    public void onConnectionLost() {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onConnectionLost();
                }
            });
        }
    }

    @Override
    public void onConnectionInterrupted() {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onConnectionInterrupted();
                }
            });
        }
    }

    @Override
    public void onRefreshRecordingServiceStatus(final int status) {
        List<IRtcEngineEventHandler> rtcEngineEventHandlers = mRtcEngineEventHandlers;
        for (final IRtcEngineEventHandler rtcEngineEventHandler : rtcEngineEventHandlers) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rtcEngineEventHandler.onRefreshRecordingServiceStatus(status);
                }
            });
        }
    }

    //----------------- interface end -----------------//


}
