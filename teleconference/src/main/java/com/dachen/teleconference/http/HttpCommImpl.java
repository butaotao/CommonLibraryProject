package com.dachen.teleconference.http;

import android.content.Context;
import android.os.Handler;

import com.dachen.common.json.JsonMananger;
import com.dachen.teleconference.bean.GetMediaDynamicKeyResponse;
import com.dachen.teleconference.bean.GetSigningKeyResponse;
import com.google.gson.JsonObject;

/**
 *
 * @ClassName: HttpCommImpl
 * @Description:TODO(数据请实现类)
 * @author: yehj
 * @date: 2015-8-29 下午4:11:14
 * @version V1.0.0
 */
public class HttpCommImpl implements HttpComm {

	@Override
	public void getMediaDynamicKey(Context context, Handler mHandler, int what, String channelName, String uid, String expiredTs) {
		MyHttpClient client = MyHttpClient.getInstance();
		MyRequestParams params = new MyRequestParams(context);
		params.set("channelName",channelName);
		params.set("uid",uid);
		params.set("expiredTs",expiredTs);
		client.post(context,params, "http://1560t568w7.iok.la:8089/phone/getMediaDynamicKey",new GsonHttpResponseHandler(mHandler,what,context){
			@Override
			protected Object parseJson(JsonObject response) {
				return JsonMananger.jsonToBean(String.valueOf(response),GetMediaDynamicKeyResponse.class);
			}
		});
	}

	@Override
	public void getSigningKey(Context context, Handler mHandler, int what, String uid, String expiredTs) {
		MyHttpClient client = MyHttpClient.getInstance();
		MyRequestParams params = new MyRequestParams(context);
		params.set("uid",uid);
		params.set("expiredTs",expiredTs);
		client.post(context,params, "http://1560t568w7.iok.la:8089/phone/getSigningKey",new GsonHttpResponseHandler(mHandler,what,context){
			@Override
			protected Object parseJson(JsonObject response) {
				return JsonMananger.jsonToBean(String.valueOf(response),GetSigningKeyResponse.class);
			}
		});
	}
}
