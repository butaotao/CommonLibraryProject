package com.dachen.incomelibrary.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.HttpStatus;
import org.json.JSONException;

import org.apache.http.Header;

public class GsonHttpResponseHandler extends TextHttpResponseHandler {

	private static final String LOG_TAG = "yehj";
	
	private Handler handler;
	private int what;
	public static final String UTF8_BOM = "\uFEFF";
	private boolean isDecode = false;//是否对获取数据进行解密
	
	public GsonHttpResponseHandler(){
		
	}
	
	public GsonHttpResponseHandler(Handler handler, int what){
		this.handler = handler;
		this.what = what;
	}	
	
	public GsonHttpResponseHandler(boolean isDecode){
		this.isDecode = isDecode;
	}
	
	/*j
	 * 解析json有错误 
	 */
	@Override
	public void onFailure(int statusCode, Header[] headers,
			String responseString, Throwable throwable) {
		// TODO Auto-generated method stub
		Logger.w(LOG_TAG, "onFailure statusCode:"+statusCode+", responseString:"+responseString+" throwable:"+throwable);
		sendFailedMsg(what, handler, statusCode);
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers,
			String responseString) {
		// TODO Auto-generated method stub
		Logger.w(LOG_TAG, "onSuccess(int, Header[], String) was not overriden, but callback was received");
	}
	
	/**
     * Returns when request succeeds
     *
     * @param statusCode http response status line
     * @param headers    response headers if any
     * @param response   parsed response if any
     */
    public void onSuccess(int statusCode, Header[] headers, JsonObject response) {
//        Log.w(LOG_TAG, "onSuccess(int, Header[], JSONObject) was not overriden, but callback was received");
    	Logger.d(LOG_TAG,"onSuccess");
    	sendSuccessMsg(response);
    }
    

    /**
     * Returns when request succeeds
     *
     * @param statusCode http response status line
     * @param headers    response headers if any
     * @param response   parsed response if any
     */
    public void onSuccess(int statusCode, Header[] headers, JsonArray response) {
        Logger.w(LOG_TAG, "onSuccess(int, Header[], JSONArray) was not overriden, but callback was received");
    }

    /**
     * Returns when request failed
     *
     * @param statusCode    http response status line
     * @param headers       response headers if any
     * @param throwable     throwable describing the way request failed
     * @param errorResponse parsed response if any
     */
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JsonObject errorResponse) {
        Logger.w(LOG_TAG, "onFailure(int, Header[], Throwable, JsonObject) statusCode:"+statusCode+",  throwable:"+throwable);
    	sendFailedMsg(what, handler, statusCode);
    }

    /**
     * Returns when request failed
     *
     * @param statusCode    http response status line
     * @param headers       response headers if any
     * @param throwable     throwable describing the way request failed
     * @param errorResponse parsed response if any
     */
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JsonArray errorResponse) {
        Logger.w(LOG_TAG, "onFailure(int, Header[], Throwable, JsonArray) statusCode:"+statusCode+",  throwable:"+throwable);
    	sendFailedMsg(what, handler, statusCode);
    }


	
    @Override
    public final void onSuccess(final int statusCode, final Header[] headers, final byte[] responseBytes) {
    	Logger.d(LOG_TAG, "onSuccess");
        if (statusCode != HttpStatus.SC_NO_CONTENT) {
            Runnable parser = new Runnable() {
                @Override
                public void run() {
                    try {
                        final Object jsonResponse = parseResponse(responseBytes);
                        postRunnable(new Runnable() {
                            @Override
                            public void run() {
                            	Logger.d(LOG_TAG, "onSuccess run1");
                                if (jsonResponse instanceof JsonObject) {
                                	Logger.d(LOG_TAG, "onSuccess run2");
                                    onSuccess(statusCode, headers, (JsonObject) jsonResponse);
                                } else if (jsonResponse instanceof JsonArray) {
                                    onSuccess(statusCode, headers, (JsonArray) jsonResponse);
                                } else if (jsonResponse instanceof String) {
                                	Logger.d(LOG_TAG, "onSuccess run3");
                                    onFailure(statusCode, headers, (String) jsonResponse, new JSONException("Response cannot be parsed as JSON data"));
                                } else {
                                	Logger.d(LOG_TAG, "onSuccess run4");
                                    onFailure(statusCode, headers, new JSONException("Unexpected response type " + jsonResponse.getClass().getName()), (JsonObject) null);
                                }

                            }
                        });
                    } catch (final JSONException ex) {
                        postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                onFailure(statusCode, headers, ex, (JsonObject) null);
                            }
                        });
                    }
                }
            };
            if (!getUseSynchronousMode()) {
                new Thread(parser).start();
            } else {
                // In synchronous mode everything should be run on one thread
                parser.run();
            }
        } else {
            onSuccess(statusCode, headers, new JsonObject());
        }
    }
    
    @Override
    public final void onFailure(final int statusCode, final Header[] headers, final byte[] responseBytes, final Throwable throwable) {
    	Logger.d(LOG_TAG, "onFailure");
        if (responseBytes != null) {
            Runnable parser = new Runnable() {
                @Override
                public void run() {
                    try {
                        final Object jsonResponse = parseResponse(responseBytes);
                        postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                if (jsonResponse instanceof JsonObject) {
                                    onFailure(statusCode, headers, throwable, (JsonObject) jsonResponse);
                                } else if (jsonResponse instanceof JsonArray) {
                                    onFailure(statusCode, headers, throwable, (JsonArray) jsonResponse);
                                } else if (jsonResponse instanceof String) {
                                    onFailure(statusCode, headers, (String) jsonResponse, throwable);
                                } else {
                                    onFailure(statusCode, headers, new JSONException("Unexpected response type " + jsonResponse.getClass().getName()), (JsonObject) null);
                                }
                            }
                        });

                    } catch (final JSONException ex) {
                        postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                onFailure(statusCode, headers, ex, (JsonObject) null);
                            }
                        });

                    }
                }
            };
            if (!getUseSynchronousMode()) {
                new Thread(parser).start();
            } else {
                // In synchronous mode everything should be run on one thread
                parser.run();
            }
        } else {
            Log.v(LOG_TAG, "response body is null, calling onFailure(Throwable, JSONObject)");
            onFailure(statusCode, headers, throwable, (JsonObject) null);
        }
    }    
    
    protected Object parseResponse(byte[] responseBody) throws JSONException {
        if (null == responseBody)
            return null;
        Object result = null;
        //trim the string to prevent start with blank, and test if the string is valid JSON, because the parser don't do this :(. If JSON is not valid this will return null
        String jsonString = getResponseString(responseBody, getCharset());
        Logger.d(LOG_TAG, "parseResponse jsonString : "+jsonString);
        String deCodeJsonStr = isDecode ?RC4.decryptionRC4(jsonString, RC4.KEY) : jsonString;
        Logger.d(LOG_TAG, "parseResponse deCodeJsonStr :"+deCodeJsonStr);
        if (deCodeJsonStr != null) {
        	deCodeJsonStr = deCodeJsonStr.trim();
        	Logger.d(LOG_TAG, "parseResponse deCodeJsonStr 1");
            if (deCodeJsonStr.startsWith(UTF8_BOM)) {
            	deCodeJsonStr = deCodeJsonStr.substring(1);
            }
            try{
            	Gson gson = new Gson();
            	Logger.d(LOG_TAG, "parseResponse deCodeJsonStr 2");
            	if (deCodeJsonStr.startsWith("{")) {
            		result = gson.fromJson(deCodeJsonStr, JsonObject.class);
            		Logger.d(LOG_TAG, "parseResponse deCodeJsonStr 3");
            	}else if(jsonString.startsWith("[")){
            		result = gson.fromJson(deCodeJsonStr, JsonArray.class);
            		Logger.d(LOG_TAG, "parseResponse deCodeJsonStr 4");
            	}else{
            		Logger.d(LOG_TAG, "parseResponse deCodeJsonStr 5");
            		result = deCodeJsonStr;
            	}
            }catch(Exception e){
            	e.printStackTrace();
            	result = deCodeJsonStr;
            	Logger.d(LOG_TAG, "parseResponse deCodeJsonStr 6");
            }
        }
        if (result == null) {
        	Logger.d(LOG_TAG, "parseResponse result :"+result);
        	Logger.d(LOG_TAG, "parseResponse deCodeJsonStr 6");
            result = jsonString;
        }
        return result;
    }    
    
    protected void  sendSuccessMsg(JsonObject response){
    	Logger.d(LOG_TAG,"SuccessMsg=="+response);
		try{
			
			int status = response.get("resultCode").getAsInt();
			msg.what = what;
			msg.arg1 = status;
			if(status == 1){
				msg.obj = parseJson(response);
				if(msg.obj == null){//解析有问题
					msg.arg1 = -1;
					msg.obj = "解析问题";
					Logger.w(LOG_TAG, "error msg: "+msg.obj);
				}
			}else{
				Logger.d(LOG_TAG, "request has some error, status:"+status);
				Logger.d(LOG_TAG, "error msg: "+msg.obj);
				msg.obj=getErrorMsg(response);
			}
			
			handler.sendMessage(msg);		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
    
    
    protected Message msg = new Message();
	
	protected void  sendFailedMsg(int what,Handler handler,int statusCode){
		if(handler == null)
			return;
		msg.what = what;
		msg.arg1 = -1;
		msg.obj = getHttpErrorMsg(statusCode);
		handler.sendMessage(msg);		
	}
	
	private String getErrorMsg(JsonObject response){
		if(response.has("resultMsg")){
			return response.get("resultMsg").getAsString();
		}
		return "服务器内部错误";
	}
	private String getHttpErrorMsg(int status){
		switch(status){
		case ConstantsHttp.HTTP_DISCONNECTED://断网
			return "访问超时";
		case ConstantsHttp.HTTP_SERVER_ERROR://异常
			return "服务器内部错误";
		}
		return "服务器内部错误";
	}	
	
	protected  Object parseJson(JsonObject response){
		return response.toString();
	}    

}
