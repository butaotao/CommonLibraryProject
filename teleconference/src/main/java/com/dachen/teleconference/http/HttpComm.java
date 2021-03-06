package com.dachen.teleconference.http;

import android.content.Context;
import android.os.Handler;

/**
 * @author yehj
 * @version V1.0.0
 * @ClassName: HttpComm
 * @Description: TODO(http接口)
 * @date 2015-8-6 上午11:14:47
 */
public interface HttpComm {


    public void getMediaDynamicKey(Context context, Handler mHandler, int what, String channelName, String uid, String expiredTs);

    public void getSigningKey(Context context, Handler mHandler, int what, String uid, String expiredTs);

    public void createPhoneMeeting(Context context, Handler mHandler, int what, String token, String creater, String groupId);

    public void getConfInfoByChannelId(Context context, Handler mHandler, int what, String channelId);

    public void dismissConf(Context context, Handler mHandler, int what, String token, String groupId, String channelId);

    public void voipCall(Context context, Handler mHandler, int what, String user, String gId, String channelId);

    public void voipCallUsers(Context context, Handler mHandler, int what, String users, String gId, String channelId);

    public void delayConf(Context context, Handler mHandler, int what, String channelId, String creater, String delayTime);

    public void floorCall(Context context, Handler mHandler, int what, String token, String userId, String gId, String channelId);

}
