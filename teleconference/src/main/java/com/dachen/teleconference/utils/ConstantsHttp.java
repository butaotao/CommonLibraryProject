package com.dachen.teleconference.utils;

public class ConstantsHttp {
	
	
	//Status Code
	public final static int HTTP_DISCONNECTED = 0;//断网或超时
	public final static int HTTP_SERVER_ERROR = 500;//服务器内部错误
	public final static int DO_SUCCESS=1;  //操作
	public final static int DO_FAIL=2; //操作失败
	public final static int PARAM_ERROR=101; //参数错误
	public final static int SERVER_ERROR=200; //服务器异常
	public final static int USER_exist=112;//账号已经存在
	public final static int PHONE_CODE_ERROR=131;//手机验证码错误
	public final static int PONE_CODE_INVALID=132; //验证码过期
	 
}
