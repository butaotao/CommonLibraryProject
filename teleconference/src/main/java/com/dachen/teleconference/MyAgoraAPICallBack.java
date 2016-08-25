package com.dachen.teleconference;

/**
 * Created by TianWei on 2016/8/25.
 */
public interface MyAgoraAPICallBack {

    public void onReconnecting(int nretry);

    public void onReconnected(int fd);

    public void onLoginSuccess(int uid, int fd);

    public void onLogout(int ecode) ;

    public void onLoginFailed(int ecode) ;

    public void onChannelJoined(String channelID);

    public void onChannelJoinFailed(String channelID, int ecode);

    public void onChannelLeaved(String channelID, int ecode) ;

    public void onChannelUserJoined(String account, int uid) ;

    public void onChannelUserLeaved(String account, int uid) ;

    public void onChannelUserList(String[] accounts, int[] uids) ;

    public void onChannelQueryUserNumResult(String channelID, int ecode, int num) ;

    public void onChannelAttrUpdated(String channelID, String name, String value, String type);

    public void onInviteReceived(String channelID, String account, int uid) ;

    public void onInviteReceivedByPeer(String channelID, String account, int uid) ;

    public void onInviteAcceptedByPeer(String channelID, String account, int uid) ;

    public void onInviteRefusedByPeer(String channelID, String account, int uid) ;

    public void onInviteFailed(String channelID, String account, int uid, int ecode);

    public void onInviteEndByPeer(String channelID, String account, int uid);

    public void onInviteEndByMyself(String channelID, String account, int uid) ;

    public void onMessageSendError(String messageID, int ecode);

    public void onMessageSendSuccess(String messageID) ;

    public void onMessageAppReceived(String msg) ;

    public void onMessageInstantReceive(String account, int uid, String msg) ;

    public void onMessageChannelReceive(String channelID, String account, int uid, String msg) ;

    public void onLog(String txt);

    public void onInvokeRet(String name, int ofu, String reason, String resp);

    public void onMsg(String from, String t, String msg) ;

    public void onUserAttrResult(String account, String name, String value) ;

    public void onUserAttrAllResult(String account, String value) ;

    public void onError(String name, int ecode, String desc);
}
