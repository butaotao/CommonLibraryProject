package com.dachen.incomelibrary.utils;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;

/**
 * 
* @ClassName: MyRequestParams 
* @Description: TODO(自定义RequestParams) 
* @author yehj
* @date 2015-8-9 下午10:47:02 
* @version V1.0.0
 */
public class MyRequestParams extends RequestParams {

	//是否对参数进行加密
	private boolean isEncode = false;
	private Context context;
	
	private String action ;
	
	
	public String getAction() {
		return action;
	}

	public void set(String key,String value){
		try{
			
			urlParams.put(key, value == null ? "" : value);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setAction(String action) {
		this.action = action;
	}

	public MyRequestParams(Context context){
		this.context=context;
		
	}
	
	public MyRequestParams(boolean isEncode){
		this.isEncode = isEncode;
	}
	
	private static final String TAG = "yehj";
	
	
	@Override
	public HttpEntity getEntity(ResponseHandlerInterface progressHandler)
			throws IOException {
		// TODO Auto-generated method stub

		return super.getEntity(progressHandler);
	}
	
	private void doStreamParams(){
		
	}
	
	private void doFileParams(){
		
	}
	
	private void doUrlParamsWithObjects(){
		
	}
	
	private void encodeRC5(){
		StringBuilder result = new StringBuilder();
		int i = 0;
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
        	result.append(entry.getKey());
        	result.append("=");
        	result.append(entry.getValue());
        	i++;
        	if(i < urlParams.size())
        		result.append("&");
        }	
        Log.d(TAG, "encodeRC5 result:"+result);
        urlParams.clear();
        urlParams.put("params", RC4.encryptionRC4String(result.toString(), RC4.KEY));
	}
	
}
