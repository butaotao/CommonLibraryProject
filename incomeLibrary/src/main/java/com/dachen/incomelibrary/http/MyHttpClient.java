package com.dachen.incomelibrary.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;


/**
 * 
* @ClassName: MyHttpClient 
* @Description: TODO(封装AsyncHttpClient) 
* @author yehj
* @date 2015-8-8 下午11:35:13 
* @version V1.0.0
 */
public class MyHttpClient extends AsyncHttpClient {

	private static String TAG = "yehj";
	
	private static MyHttpClient httpClient ;
	private Context mContext;
	
	private MyHttpClient(Context context){
		this.mContext = context;
	}
	
	private MyHttpClient(){
	}	
	
	public static MyHttpClient getInstance(){
		if(httpClient == null){
			synchronized (MyHttpClient.class) {
				if(httpClient == null){
					httpClient = new MyHttpClient();
					httpClient.setTimeout(1000*30);
				}
			}
		}
		return httpClient;
	}	
	
	public static MyHttpClient getInstance(Context context){
		if(httpClient == null){
			synchronized (MyHttpClient.class) {
				if(httpClient == null){
					httpClient = new MyHttpClient(context);
					httpClient.setTimeout(1000*30);
				}
			}
		}         
		return httpClient;
	}
	
	
	
	public RequestHandle post(Context context, RequestParams params,String URL,
			ResponseHandlerInterface responseHandler) {
		// TODO Auto-generated method stub


		return super.post(context,URL, params, responseHandler);
	}
	public RequestHandle postJson(Context context, String jsonStr,String URL,
			ResponseHandlerInterface responseHandler) {
		// TODO Auto-generated method stub
		

		HttpEntity entity=null;
		try {
			entity = new StringEntity(jsonStr, "UTF-8");
		} catch (UnsupportedCharsetException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return super.post(context, URL, entity, "application/json", responseHandler);
	}
	
	
//	public RequestHandle upload(Context context, RequestParams params,
//			ResponseHandlerInterface responseHandler) {
//		// TODO Auto-generated method stub
//		Logger.d(TAG, "MyHttpClient upload");
//		return super.post(context,ConstantsApp.FILE_URL, params, responseHandler);
//	}	
	
	
	
	
	public RequestHandle downloadFile(Context context,String downloadUrl,String[] allowedContentTypes,
			ResponseHandlerInterface responseHandler) {
		// TODO Auto-generated method stub
	//	return super.get(downloadUrl, responseHandler);
		return  super.get(downloadUrl, responseHandler);
	}	
	
}
