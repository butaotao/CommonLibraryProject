package com.dachen.incomelibrary.http;

import android.content.Context;
import android.os.Handler;


public interface HttpComm {

	//获取收入基本信息
	public void  getIncomeInfo(Context context, Handler mHandler, int what, String access_token, String doctorId);

	//获取历史收入明细
	public void getIncomeHistoryList(Context context, Handler mHandler, int what, String access_token, String doctorId, int
			pageSize, int pageIndex);

	//获取月收入明细
	public void getInComeMonthList(Context context, Handler mHandler, int what, String access_token, String doctorId, String time,
								   int pageSize, int pageIndex);

	//获取打款记录
	public void getIncomePaidList(Context context, Handler mHandler, int what, String access_token, String userId, int pageSize,
								  int pageIndex);

	//获取单个打款项目详情
	public void getIncomePaidItemList(Context context, Handler mHandler, int what, String access_token, String userId, String id,
									  int
											  pageSize, int pageIndex);



	//查询账户余额明细或者未完成订单余额明细
	public void getIncomeDetails(Context context, Handler mHandler, int what, String doctorId, int type, int pageIndex,
								 int pageSize);

	//删除银行卡
	public void  deleteBankCard(Context context, Handler mHandler, int what, String access_token, String id);

	//更新银行卡
	public void  updateBankCard(Context context, Handler mHandler, int what, String access_token, String id, String subBank);

	//设置指定银行卡为默认银行卡
	public void  setBankStatus(Context context, Handler mHandler, int what, String id, int status);

	//查询绑定银行账号
	public  void  queryBindBankAccount(Context context, Handler mHandler, int what,String access_token);

	//获取获取银行卡名称
	public void  findBankName(Context context, Handler mHandler, int what, String access_token, String bankCard);

	//添加银行卡
	public void  addBankCard(Context context, Handler mHandler, int what,String access_token,String bankNo,String bankName, String subBank, String personNo, String userRealName);

	//查询银行信息
	public void  queryBankInfo(Context context, Handler mHandler, int what,String access_token);
}
