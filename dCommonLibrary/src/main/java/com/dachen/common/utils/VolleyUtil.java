package com.dachen.common.utils;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * 工具类  单例
 * @author WANG
 *
 */
public class VolleyUtil {
	private static final String TAG = VolleyUtil.class.getSimpleName();
	
	private volatile static RequestQueue requestQueue;

	public static RequestQueue getQueue(Context context) {
		if (requestQueue == null) {
			synchronized (VolleyUtil.class) {
				if (requestQueue == null) {
					requestQueue = Volley.newRequestQueue(context.getApplicationContext());
					requestQueue.start();
				}
			}
		}
		
		return requestQueue;
	}
	
	public static <T> void setTimeOut(Request<T> request,int timeOut){
		request.setRetryPolicy(new DefaultRetryPolicy(timeOut, 0, 0));
	}
	
	public static <T> void addRequest(Context context,Request<T> request){	
		setTimeOut(request, 10000);
		getQueue(context).add(request);
	}
	
	public static void cancelAll(Context context,Object tag){
		getQueue(context).cancelAll(tag);
	}

	public static Map<String,String> checkParam(Map<String,String > m){
		HashMap<String,String> res=new HashMap<>();
		for (Map.Entry<String, String> entry:m.entrySet()) {
			if(entry.getValue()!=null){
				res.put(entry.getKey(),entry.getValue());
			}
		}
		return res;
	}

	public static Listener<String> getEmptyListener(){
		return new Listener<String>() {
			@Override
			public void onResponse(String s) {
				Logger.d(TAG, "s=" + s);
			}
		};
	}
}
