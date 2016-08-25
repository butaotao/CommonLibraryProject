package com.dachen.teleconference;

import com.dachen.common.utils.Logger;

import io.agora.AgoraAPI;
import io.agora.IAgoraAPI;

/**
 * Created by Administrator on 2016/8/25.
 */
public class AgoraAPICallBack extends AgoraAPI.CallBack {

    @Override
    public void setCB(IAgoraAPI.ICallBack cb) {
        super.setCB(cb);
    }

    @Override
    public IAgoraAPI.ICallBack getCB() {
        return super.getCB();
    }

    @Override
    public void onReconnecting(int nretry) {
        super.onReconnecting(nretry);
    }

    @Override
    public void onReconnected(int fd) {
        super.onReconnected(fd);
    }

    @Override
    public void onLoginSuccess(int uid, int fd) {
        super.onLoginSuccess(uid, fd);
        Logger.d("yehj","onLoginSuccess");
    }

    @Override
    public void onLogout(int ecode) {
        super.onLogout(ecode);
    }

    @Override
    public void onLoginFailed(int ecode) {
        super.onLoginFailed(ecode);
    }

    @Override
    public void onChannelJoined(String channelID) {
        super.onChannelJoined(channelID);
    }

    @Override
    public void onChannelJoinFailed(String channelID, int ecode) {
        super.onChannelJoinFailed(channelID, ecode);
    }

    @Override
    public void onChannelLeaved(String channelID, int ecode) {
        super.onChannelLeaved(channelID, ecode);
    }

    @Override
    public void onChannelUserJoined(String account, int uid) {
        super.onChannelUserJoined(account, uid);
    }

    @Override
    public void onChannelUserLeaved(String account, int uid) {
        super.onChannelUserLeaved(account, uid);
    }

    @Override
    public void onChannelUserList(String[] accounts, int[] uids) {
        super.onChannelUserList(accounts, uids);
    }

    @Override
    public void onChannelQueryUserNumResult(String channelID, int ecode, int num) {
        super.onChannelQueryUserNumResult(channelID, ecode, num);
    }

    @Override
    public void onChannelAttrUpdated(String channelID, String name, String value, String type) {
        super.onChannelAttrUpdated(channelID, name, value, type);
    }

    @Override
    public void onInviteReceived(String channelID, String account, int uid) {
        super.onInviteReceived(channelID, account, uid);
    }

    @Override
    public void onInviteReceivedByPeer(String channelID, String account, int uid) {
        super.onInviteReceivedByPeer(channelID, account, uid);
    }

    @Override
    public void onInviteAcceptedByPeer(String channelID, String account, int uid) {
        super.onInviteAcceptedByPeer(channelID, account, uid);
    }

    @Override
    public void onInviteRefusedByPeer(String channelID, String account, int uid) {
        super.onInviteRefusedByPeer(channelID, account, uid);
    }

    @Override
    public void onInviteFailed(String channelID, String account, int uid, int ecode) {
        super.onInviteFailed(channelID, account, uid, ecode);
    }

    @Override
    public void onInviteEndByPeer(String channelID, String account, int uid) {
        super.onInviteEndByPeer(channelID, account, uid);
    }

    @Override
    public void onInviteEndByMyself(String channelID, String account, int uid) {
        super.onInviteEndByMyself(channelID, account, uid);
    }

    @Override
    public void onMessageSendError(String messageID, int ecode) {
        super.onMessageSendError(messageID, ecode);
    }

    @Override
    public void onMessageSendSuccess(String messageID) {
        super.onMessageSendSuccess(messageID);
    }

    @Override
    public void onMessageAppReceived(String msg) {
        super.onMessageAppReceived(msg);
    }

    @Override
    public void onMessageInstantReceive(String account, int uid, String msg) {
        super.onMessageInstantReceive(account, uid, msg);
    }

    @Override
    public void onMessageChannelReceive(String channelID, String account, int uid, String msg) {
        super.onMessageChannelReceive(channelID, account, uid, msg);
    }

    @Override
    public void onLog(String txt) {
        super.onLog(txt);
        Logger.d("yehj","txt---"+txt);
    }

    @Override
    public void onInvokeRet(String name, int ofu, String reason, String resp) {
        super.onInvokeRet(name, ofu, reason, resp);
    }

    @Override
    public void onMsg(String from, String t, String msg) {
        super.onMsg(from, t, msg);
    }

    @Override
    public void onUserAttrResult(String account, String name, String value) {
        super.onUserAttrResult(account, name, value);
    }

    @Override
    public void onUserAttrAllResult(String account, String value) {
        super.onUserAttrAllResult(account, value);
    }

    @Override
    public void onError(String name, int ecode, String desc) {
        super.onError(name, ecode, desc);
    }
}
