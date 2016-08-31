package com.dachen.teleconference;

/**
 * Created by TianWei on 2016/8/31.
 */
public interface CreateOrJoinMeetingCallBack {
    public void createOrJoinMeetingSuccess(String channelId);

    public void createOrJoinMeetingFailed(String failMessage);
}
