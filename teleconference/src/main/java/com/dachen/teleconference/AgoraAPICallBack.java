package com.dachen.teleconference;

import android.content.Context;
import android.os.Handler;

import com.dachen.common.utils.Logger;

import io.agora.AgoraAPI;
import io.agora.IAgoraAPI;

/**
 * Created by Administrator on 2016/8/25.
 */
public class AgoraAPICallBack extends AgoraAPI.CallBack {

    private MyAgoraAPICallBack myAgoraAPICallBack;
    private Context mContext;
    private Handler mMainHandler;

    public AgoraAPICallBack(Context mContext){
        this.mContext = mContext.getApplicationContext();
        mMainHandler = new Handler(mContext.getMainLooper());
    }

    public void addAgoraAPICallBack(MyAgoraAPICallBack myAgoraAPICallBack){
        this.myAgoraAPICallBack=myAgoraAPICallBack;
    }


    @Override
    public void setCB(IAgoraAPI.ICallBack cb) {

    }

    @Override
    public IAgoraAPI.ICallBack getCB() {
        return super.getCB();
    }

    @Override
    public void onReconnecting(final int nretry) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onReconnecting(nretry);
                }
            });
        }
    }

    @Override
    public void onReconnected(final int fd) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onReconnected(fd);
                }
            });
        }
    }

    @Override
    public void onLoginSuccess(final int uid, final int fd) {
        super.onLoginSuccess(uid, fd);
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onLoginSuccess(uid,fd);
                }
            });
        }
    }

    @Override
    public void onLogout(final int ecode) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onLogout(ecode);
                }
            });
        }
    }

    @Override
    public void onLoginFailed(final int ecode) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onLoginFailed(ecode);
                }
            });
        }
    }

    @Override
    public void onChannelJoined(final String channelID) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onChannelJoined(channelID);
                }
            });
        }
    }

    @Override
    public void onChannelJoinFailed(final String channelID, final int ecode) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onChannelJoinFailed(channelID,ecode);
                }
            });
        }
    }

    @Override
    public void onChannelLeaved(final String channelID, final int ecode) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onChannelLeaved(channelID, ecode);
                }
            });
        }
    }

    @Override
    public void onChannelUserJoined(final String account, final int uid) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onChannelUserJoined(account, uid);
                }
            });
        }
    }

    @Override
    public void onChannelUserLeaved(final String account, final int uid) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onChannelUserLeaved(account, uid);
                }
            });
        }
    }

    @Override
    public void onChannelUserList(final String[] accounts, final int[] uids) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onChannelUserList(accounts, uids);
                }
            });
        }
    }

    @Override
    public void onChannelQueryUserNumResult(final String channelID, final int ecode, final int num) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onChannelQueryUserNumResult(channelID, ecode, num);
                }
            });
        }
    }

    @Override
    public void onChannelAttrUpdated(final String channelID, final String name, final String value, final String type) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onChannelAttrUpdated(channelID, name, value, type);
                }
            });
        }
    }

    @Override
    public void onInviteReceived(final String channelID, final String account, final int uid) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onInviteReceived(channelID, account, uid);
                }
            });
        }
    }

    @Override
    public void onInviteReceivedByPeer(final String channelID, final String account, final int uid) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onInviteReceivedByPeer(channelID, account, uid);
                }
            });
        }
    }

    @Override
    public void onInviteAcceptedByPeer(final String channelID, final String account, final int uid) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onInviteAcceptedByPeer(channelID, account, uid);
                }
            });
        }
    }

    @Override
    public void onInviteRefusedByPeer(final String channelID, final String account, final int uid) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onInviteRefusedByPeer(channelID, account, uid);
                }
            });
        }
    }

    @Override
    public void onInviteFailed(final String channelID, final String account, final int uid, final int ecode) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onInviteFailed(channelID, account, uid, ecode);
                }
            });
        }
    }

    @Override
    public void onInviteEndByPeer(final String channelID, final String account, final int uid) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onInviteEndByPeer(channelID, account, uid);
                }
            });
        }
    }

    @Override
    public void onInviteEndByMyself(final String channelID, final String account, final int uid) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onInviteEndByMyself(channelID, account, uid);
                }
            });
        }
    }

    @Override
    public void onMessageSendError(final String messageID, final int ecode) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onMessageSendError(messageID, ecode);
                }
            });
        }
    }

    @Override
    public void onMessageSendSuccess(final String messageID) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onMessageSendSuccess(messageID);
                }
            });
        }
    }

    @Override
    public void onMessageAppReceived(final String msg) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onMessageAppReceived(msg);
                }
            });
        }
    }

    @Override
    public void onMessageInstantReceive(final String account, final int uid, final String msg) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onMessageInstantReceive(account, uid, msg);
                }
            });
        }
    }

    @Override
    public void onMessageChannelReceive(final String channelID, final String account, final int uid, final String msg) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onMessageChannelReceive(channelID, account, uid, msg);
                }
            });
        }
    }

    @Override
    public void onLog(final String txt) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onLog(txt);
                }
            });
        }
    }

    @Override
    public void onInvokeRet(final String name, final int ofu, final String reason, final String resp) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onInvokeRet(name, ofu, reason, resp);
                }
            });
        }
    }

    @Override
    public void onMsg(final String from, final String t, final String msg) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onMsg(from, t, msg);
                }
            });
        }
    }

    @Override
    public void onUserAttrResult(final String account, final String name, final String value) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onUserAttrResult(account, name, value);
                }
            });
        }
    }

    @Override
    public void onUserAttrAllResult(final String account, final String value) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onUserAttrAllResult(account, value);
                }
            });
        }
    }

    @Override
    public void onError(final String name, final int ecode, final String desc) {
        if (myAgoraAPICallBack!= null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    myAgoraAPICallBack.onError(name, ecode, desc);
                }
            });
        }
    }
}
