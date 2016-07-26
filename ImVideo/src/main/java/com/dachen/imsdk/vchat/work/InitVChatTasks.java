package com.dachen.imsdk.vchat.work;

import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dachen.common.toolbox.DCommonRequest;
import com.dachen.common.utils.Logger;
import com.dachen.common.utils.ToastUtil;
import com.dachen.common.utils.VolleyUtil;
import com.dachen.imsdk.ImSdk;
import com.dachen.imsdk.net.PollingURLs;
import com.dachen.imsdk.vchat.ImVideo;
import com.dachen.imsdk.vchat.VChatUtil;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.ObjectResult;
import com.tencent.av.sdk.AVError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mcp on 2016/3/19.
 */
public class InitVChatTasks {
    private static final String TAG = InitVChatTasks.class.getSimpleName();
    public String userId;

    public InitVChatTasks(String userId) {
        this.userId = userId;
    }

    public void execute() {
        getUserSign();
    }

    private void getUserSign() {
        final String reqTag = "getUserSign";
        RequestQueue queue = VolleyUtil.getQueue(ImSdk.getInstance().context);
        queue.cancelAll(reqTag);
        StringRequest request = new DCommonRequest(ImSdk.getInstance().context, Request.Method.POST, PollingURLs.getTxSig(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                handleResponse(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                ToastUtil.showErrorNet(ImSdk.getInstance().context);
                ImSdk.getInstance().context.sendBroadcast(new Intent(VChatUtil.ACTION_START_CONTEXT_COMPLETE).putExtra(VChatUtil.EXTRA_AV_ERROR_RESULT, AVError.AV_ERR_FAILED));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", ImSdk.getInstance().accessToken);
                params.put("userId", userId);
                Logger.d(TAG, params.toString());
                return params;
            }
        };
        //重试3次
        request.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(reqTag);
        queue.add(request);
    }

    private void handleResponse(String response) {
//        Logger.d(TAG, "response sign=" + response);
        ObjectResult<String> result = JSON.parseObject(response, new TypeReference<ObjectResult<String>>() {
        });
        if (result == null || result.getResultCode() != Result.CODE_SUCCESS || result.getData() == null) {
            ToastUtil.showErrorNet(ImSdk.getInstance().context);
            return;
        }
        String sign = result.getData();
        ImVideo.getInstance().mQavsdkControl.startContext(userId, sign);
    }
}
