package com.dachen.teleconference.http;

import android.content.Context;
import android.os.Handler;

/**
 *
 * @ClassName: HttpComm
 * @Description: TODO(http接口)
 * @author yehj
 * @date 2015-8-6 上午11:14:47
 * @version V1.0.0
 */
public interface HttpComm {


	public void  getMediaDynamicKey(Context context, Handler mHandler, int what,String channelName,String uid,String expiredTs);

	public void getSigningKey(Context context, Handler mHandler, int what,String uid,String expiredTs);

}
