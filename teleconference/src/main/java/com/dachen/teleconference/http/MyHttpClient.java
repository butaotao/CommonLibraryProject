package com.dachen.teleconference.http;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.dachen.common.utils.Logger;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;
import java.security.KeyStore;


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
	//				httpClient.setSSLSocketFactory(getSocketFactory());
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
	//				httpClient.setSSLSocketFactory(getSocketFactory());
				}
			}
		}         
		return httpClient;
	}
	
	
	
	public RequestHandle post(Context context, RequestParams params, String URL,
							  ResponseHandlerInterface responseHandler) {
		// TODO Auto-generated method stub
		Logger.d(TAG, "====================================");
		Logger.e(TAG,  "URL: " + URL);
		Logger.e(TAG, "params: " + String.valueOf(params));
		Logger.d(TAG, "====================================");
		StringBuilder label = new StringBuilder();
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			if(pInfo.packageName.equals("com.dachen.dgroupdoctor")){
				label.append("DGroupDoctor");
			}else if(pInfo.packageName.equals("com.dachen.dgrouppatient")){
				label.append("DGroupPatient");
				label.append("/");
			}
			label.append(pInfo.versionName);
		} catch (PackageManager.NameNotFoundException e) {
		}
		label.append("/");
		label.append(System.getProperty("http.agent"));
		setUserAgent(label.toString());
		return super.post(context,URL, params, responseHandler);
	}
	public RequestHandle postJson(Context context, String jsonStr,String URL,
			ResponseHandlerInterface responseHandler) {
		// TODO Auto-generated method stub
		
		Logger.d(TAG, "====================================");
		Logger.e(TAG,  "URL: " + URL);
		Logger.e(TAG, "json: " +jsonStr);
		Logger.d(TAG, "====================================");
		HttpEntity entity=null;
		try {
			try {
				entity = new StringEntity(jsonStr, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedCharsetException e1) {
			e1.printStackTrace();
		}
		return super.post(context, URL, entity, "application/json", responseHandler);
	}

	public RequestHandle downloadFile(Context context,String downloadUrl,String[] allowedContentTypes,
			ResponseHandlerInterface responseHandler) {
		// TODO Auto-generated method stub
	//	return super.get(downloadUrl, responseHandler);
		return  super.get(downloadUrl, responseHandler);
	}

	public  static SSLSocketFactory getSocketFactory() {
		Logger.d("yehj","SSLSocketFactory");
		MySSLSocketFactory socketFactory=null;
		try{
			KeyStore  trustStore=KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			// We initialize a new SSLSocketFacrory
			socketFactory = new MySSLSocketFactory(trustStore);
			// We set that all host names are allowed in the socket factory
			socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		}catch (Exception e){

		}
		return socketFactory ;
	}
	
}
