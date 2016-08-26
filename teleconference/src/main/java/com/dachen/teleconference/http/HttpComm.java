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

    public void  getConfInfoByChannelId(Context context, Handler mHandler, int what,String channelId);

}
