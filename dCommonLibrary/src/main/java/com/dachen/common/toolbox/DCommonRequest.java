package com.dachen.common.toolbox;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.dachen.common.DCommonSdk;
import com.dachen.common.consts.RequestConsts;
import com.dachen.common.json.EmptyResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mcp on 2016/4/25.
 */
public class DCommonRequest extends StringRequest{

    private Context context;
    protected boolean deliverAnyWay;
    public DCommonRequest(Context context,int method, String url, Listener<String> listener, ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.context=context;
    }
    public DCommonRequest(int method, String url, Listener<String> listener, ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.context= DCommonSdk.mContext;
    }

    public DCommonRequest(Context context,String url, Listener<String> listener, ErrorListener errorListener) {
        super(url, listener, errorListener);
        this.context=context;
    }

    @Override
    protected void deliverResponse(String response) {
        EmptyResult res= JSON.parseObject(response,EmptyResult.class);
        boolean b=false;
        if(res!=null){
            if(res.resultCode== RequestConsts.ERROR_TOKEN_ERROR||res.resultCode== RequestConsts.ERROR_TOKEN_NONE){
                b= CommonManager.onRequestTokenErr();
            }
            else if(res.resultCode==RequestConsts.ERROR_UPDATE_VERSION){
                b= CommonManager.onUpdateVersionErr(res.resultMsg);
            }
        }
        if(deliverAnyWay|| !b)
            super.deliverResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        if (headers == null || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }
        if (context!= null) {
            StringBuilder label = new StringBuilder();
            try {
                PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                if(!TextUtils.isEmpty(DCommonSdk.reqLabel)){
                    label.append(DCommonSdk.reqLabel).append("/");
                }
//                if(pInfo.packageName.equals("com.dachen.dgroupdoctor")){
//                    label.append("DGroupDoctor");
//                    label.append("/");
//                }else if(pInfo.packageName.equals("com.dachen.dgrouppatient")){
//                    label.append("DGroupPatient");
//                    label.append("/");
//                }else if(pInfo.packageName.equals("com.bestunimed.dgroupdoctor")){
//                    label.append("DGroupDoctor_BDJL");
//                    label.append("/");
//                }else if(pInfo.packageName.equals("com.bestunimed.dgrouppatient")){
//                    label.append("DGroupPatient_BDJL");
//                    label.append("/");
//                }
                label.append(pInfo.versionName);
            } catch (PackageManager.NameNotFoundException e) {
            }
            label.append("/");
            label.append(System.getProperty("http.agent"));
            headers.put("User-Agent", label.toString());
        }
        return headers;
    }
}
