package com.dachen.teleconference.http;

import android.content.Context;
import android.os.Handler;

import com.dachen.common.json.JsonMananger;
import com.dachen.teleconference.bean.BaseResponse;
import com.dachen.teleconference.bean.CreatePhoneMeetingResponse;
import com.dachen.teleconference.bean.GetConfInfoByChannelIdResponse;
import com.dachen.teleconference.bean.GetMediaDynamicKeyResponse;
import com.dachen.teleconference.bean.GetSigningKeyResponse;
import com.google.gson.JsonObject;

/**
 * @version V1.0.0
 * @ClassName: HttpCommImpl
 * @Description:TODO(数据请实现类)
 * @author: yehj
 * @date: 2015-8-29 下午4:11:14
 */
public class HttpCommImpl implements HttpComm {

    @Override
    public void getMediaDynamicKey(Context context, Handler mHandler, int what, String channelName, String uid,
                                   String expiredTs) {
        MyHttpClient client = MyHttpClient.getInstance();
        MyRequestParams params = new MyRequestParams(context);
        params.set("channelName", channelName);
        params.set("uid", uid);
        params.set("expiredTs", expiredTs);
        client.post(context, params, Constants.IP + "phone/getMediaDynamicKey",
                new GsonHttpResponseHandler(mHandler, what, context) {
                    @Override
                    protected Object parseJson(JsonObject response) {
                        return JsonMananger.jsonToBean(String.valueOf(response), GetMediaDynamicKeyResponse.class);
                    }
                });
    }

    @Override
    public void getSigningKey(Context context, Handler mHandler, int what, String uid, String expiredTs) {
        MyHttpClient client = MyHttpClient.getInstance();
        MyRequestParams params = new MyRequestParams(context);
        params.set("uid", uid);
        params.set("expiredTs", expiredTs);
        client.post(context, params, Constants.IP + "phone/getSigningKey",
                new GsonHttpResponseHandler(mHandler, what, context) {
                    @Override
                    protected Object parseJson(JsonObject response) {
                        return JsonMananger.jsonToBean(String.valueOf(response), GetSigningKeyResponse.class);
                    }
                });
    }

    @Override
    public void createPhoneMeeting(Context context, Handler mHandler, int what, String token, String creater, String groupId) {
        MyHttpClient client = MyHttpClient.getInstance();
        MyRequestParams params = new MyRequestParams(context);
        params.set("token", token);
        params.set("creater", creater);
        params.set("groupId", groupId);
        params.set("planEndTime", "3600");
        client.post(context, params, Constants.IP + "phone/createConf",
                new GsonHttpResponseHandler(mHandler, what, context) {
                    @Override
                    protected Object parseJson(JsonObject response) {
                        return JsonMananger.jsonToBean(String.valueOf(response), CreatePhoneMeetingResponse.class);
                    }
                });
    }

    @Override
    public void getConfInfoByChannelId(Context context, Handler mHandler, int what, String channelId) {
        MyHttpClient client = MyHttpClient.getInstance();
        MyRequestParams params = new MyRequestParams(context);
        params.set("channelId", channelId);
        client.post(context, params, Constants.IP + "phone/getConfInfoByChannelId",
                new GsonHttpResponseHandler(mHandler, what, context) {
                    @Override
                    protected Object parseJson(JsonObject response) {
                        return JsonMananger.jsonToBean(String.valueOf(response), GetConfInfoByChannelIdResponse.class);
                    }
                });
    }

    @Override
    public void dismissConf(Context context, Handler mHandler, int what, String token, String groupId, String channelId) {
        MyHttpClient client = MyHttpClient.getInstance();
        MyRequestParams params = new MyRequestParams(context);
        params.set("token", token);
        params.set("groupId", groupId);
        params.set("channelId", channelId);
        client.post(context, params, Constants.IP + "phone/dismissConf",
                new GsonHttpResponseHandler(mHandler, what, context) {
                    @Override
                    protected Object parseJson(JsonObject response) {
                        return JsonMananger.jsonToBean(String.valueOf(response), BaseResponse.class);
                    }
                });
    }

    @Override
    public void voipCall(Context context, Handler mHandler, int what, String user, String gId, String channelId) {
        MyHttpClient client = MyHttpClient.getInstance();
        MyRequestParams params = new MyRequestParams(context);
        params.set("user", user);
        params.set("gId", gId);
        params.set("channelId", channelId);
        client.post(context, params, Constants.IP + "phone/voipCall",
                new GsonHttpResponseHandler(mHandler, what, context) {
                    @Override
                    protected Object parseJson(JsonObject response) {
                        return JsonMananger.jsonToBean(String.valueOf(response), BaseResponse.class);
                    }
                });
    }

    @Override
    public void voipCallUsers(Context context, Handler mHandler, int what, String users, String gId, String channelId) {
        MyHttpClient client = MyHttpClient.getInstance();
        MyRequestParams params = new MyRequestParams(context);
        params.set("users", users);
        params.set("gId", gId);
        params.set("channelId", channelId);
        client.post(context, params, Constants.IP + "phone/voipCallUsers",
                new GsonHttpResponseHandler(mHandler, what, context) {
                    @Override
                    protected Object parseJson(JsonObject response) {
                        return JsonMananger.jsonToBean(String.valueOf(response), BaseResponse.class);
                    }
                });
    }
}
