package com.dachen.teleconference.http;

/**
 * 
* @ClassName: HttpCommClient 
* @Description: TODO(http单列模式) 
* @author yehj
* @date 2015-8-6 上午11:14:15 
* @version V1.0.0
 */
public class HttpCommClient {

	public static HttpComm httpComm;
	
	private HttpCommClient(){
		
	}
	public static HttpComm getInstance(){
		if(httpComm == null){
			synchronized (HttpCommClient.class) {//线程安全单例模式
				if(httpComm == null){
					httpComm = new HttpCommImpl();
				}
			}
		}
		return httpComm;
	}
	
}
