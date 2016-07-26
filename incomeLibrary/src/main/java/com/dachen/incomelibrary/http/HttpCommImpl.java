package com.dachen.incomelibrary.http;

import android.content.Context;
import android.os.Handler;

import com.dachen.incomelibrary.bean.BaseResponse;
import com.dachen.incomelibrary.bean.FindBankNameResponse;
import com.dachen.incomelibrary.bean.IncomeDetailsResponse;
import com.dachen.incomelibrary.bean.IncomeHistoryListResponse;
import com.dachen.incomelibrary.bean.IncomeInfoResponse;
import com.dachen.incomelibrary.bean.IncomeMonthListResponse;
import com.dachen.incomelibrary.bean.IncomePaidListResponse;
import com.dachen.incomelibrary.utils.Constants;
import com.dachen.incomelibrary.utils.GsonHttpResponseHandler;
import com.dachen.incomelibrary.utils.JsonMananger;
import com.dachen.incomelibrary.utils.Logger;
import com.dachen.incomelibrary.utils.MyRequestParams;
import com.dachen.incomelibrary.utils.ParseJsonObjectUtil;
import com.google.gson.JsonObject;


/**
 * 
 * @ClassName: HttpCommImpl
 * @Description:TODO(数据请实现类)
 * @author: yehj
 * @date: 2015-8-29 下午4:11:14
 * @version V1.0.0
 */
public class HttpCommImpl implements HttpComm {



	@Override
	public void getIncomeInfo(Context context, Handler mHandler, int what, String access_token, String doctorId) {
		MyHttpClient client = MyHttpClient.getInstance();
		MyRequestParams params = new MyRequestParams(context);
		params.set("access_token", access_token);
		params.set("doctorId", doctorId);
		client.post(context, params, Constants.GET_INCOME_INFO,
				new GsonHttpResponseHandler(mHandler, what) {
					@Override
					protected Object parseJson(JsonObject response) {
						if (response!=null){
							return JsonMananger.jsonToBean(response.toString(), IncomeInfoResponse.class);
						}
						return null;
					}
				});

	}

	@Override
	public void getIncomeHistoryList(Context context, Handler mHandler, int what, String access_token, String doctorId,
									 int pageSize, int pageIndex) {
		MyHttpClient client = MyHttpClient.getInstance();
		MyRequestParams params = new MyRequestParams(context);
		params.set("access_token", access_token);
		params.set("doctorId", doctorId);
		params.set("pageSize", pageSize+"");
		params.set("pageIndex", pageIndex+"");
		client.post(context, params, Constants.GET_INCOME_HISTORY_LIST,
				new GsonHttpResponseHandler(mHandler, what) {
					@Override
					protected Object parseJson(JsonObject response) {
						if (response!=null){
							return JsonMananger.jsonToBean(response.toString(), IncomeHistoryListResponse.class);
						}
						return null;
					}
				});
	}

	@Override
	public void getInComeMonthList(Context context, Handler mHandler, int what, String access_token, String doctorId,
								   String time,int pageSize, int pageIndex) {
		MyHttpClient client = MyHttpClient.getInstance();
		MyRequestParams params = new MyRequestParams(context);
		params.set("access_token", access_token);
		params.set("doctorId", doctorId);
		params.set("time", time);
		params.set("pageSize", pageSize+"");
		params.set("pageIndex", pageIndex+"");
		client.post(context, params, Constants.GET_INCOME_MONTH_LIST,
				new GsonHttpResponseHandler(mHandler, what) {
					@Override
					protected Object parseJson(JsonObject response) {
						if (response!=null){
							return JsonMananger.jsonToBean(response.toString(), IncomeMonthListResponse.class);
						}
						return null;
					}
				});

	}

	@Override
	public void getIncomePaidList(Context context, Handler mHandler, int what, String access_token, String userId, int pageSize,
									int pageIndex) {
		MyHttpClient client = MyHttpClient.getInstance();
		MyRequestParams params = new MyRequestParams(context);
		params.set("access_token", access_token);
		params.set("userId", userId);
		params.set("pageSize", pageSize+"");
		params.set("pageIndex", pageIndex+"");
		client.post(context, params, Constants.GET_INCOME_PAID_LIST,
				new GsonHttpResponseHandler(mHandler, what) {
					@Override
					protected Object parseJson(JsonObject response) {
						if (response!=null){
							return JsonMananger.jsonToBean(response.toString(), IncomePaidListResponse.class);
						}
						return null;
					}
				});

	}

	@Override
	public void getIncomePaidItemList(Context context, Handler mHandler, int what, String access_token, String userId, String id,
									  int pageSize, int pageIndex) {
		MyHttpClient client = MyHttpClient.getInstance();
		MyRequestParams params = new MyRequestParams(context);
		params.set("access_token", access_token);
		params.set("userId", userId);
		params.set("id", id);
		params.set("pageSize", pageSize+"");
		params.set("pageIndex", pageIndex+"");
		client.post(context, params, Constants.GET_INCOME_PAID_ITEM_LIST,
				new GsonHttpResponseHandler(mHandler, what) {
					@Override
					protected Object parseJson(JsonObject response) {

						if (response!=null){
							return JsonMananger.jsonToBean(response.toString(), IncomeDetailsResponse.class);
						}
						return null;
					}
				});

	}





	@Override
	public void getIncomeDetails(Context context, Handler mHandler, int what, String doctorId, int type, int pageIndex, int pageSize) {
		MyHttpClient client = MyHttpClient.getInstance();
		MyRequestParams params = new MyRequestParams(context);
//		params.set("access_token",UserSp.getInstance(context).getAccessToken(""));
		params.set("doctorId", doctorId);
		params.set("type", type+"");
		params.set("pageIndex", pageIndex+"");
		params.set("pageSize", pageSize+"");
		client.post(context, params, Constants.GET_INCOME_DETAIL,
				new GsonHttpResponseHandler(mHandler, what) {
					@Override
					protected Object parseJson(JsonObject response) {
						return JsonMananger.jsonToBean(String.valueOf(response), IncomeDetailsResponse.class);
					}
				});
	}

	@Override
	public void deleteBankCard(Context context, Handler mHandler, int what,
			String access_token, String id) {
		// TODO Auto-generated method stub

		MyHttpClient client = MyHttpClient.getInstance();
		MyRequestParams params = new MyRequestParams(context);
		params.set("access_token", access_token);
		params.set("id", id);
		client.post(context, params, Constants.DELET_EBANK_CARD,
				new GsonHttpResponseHandler(mHandler, what) {
					@Override
					protected Object parseJson(JsonObject response) {
						// TODO Auto-generated method stub
						return super.parseJson(response);
					}
				});

	}

	@Override
	public void updateBankCard(Context context, Handler mHandler, int what,
			String access_token, String id, String subBank) {
		// TODO Auto-generated method stub
		MyHttpClient client = MyHttpClient.getInstance();
		MyRequestParams params = new MyRequestParams(context);
		params.set("access_token", access_token);
		params.set("id", id);
		params.set("subBank", subBank);
		client.post(context, params, Constants.UPDATE_BANK_CARD,
				new GsonHttpResponseHandler(mHandler, what) {
					@Override
					protected Object parseJson(JsonObject response) {
						// TODO Auto-generated method stub
						return super.parseJson(response);
					}
				});

	}

	@Override
	public void setBankStatus(Context context, Handler mHandler, int what, String id, int status) {

		MyHttpClient client = MyHttpClient.getInstance();
		MyRequestParams params = new MyRequestParams(context);
//		params.set("access_token", UserSp.getInstance(context).getAccessToken(""));
		params.set("id", id);
		params.set("isDefault", status + "");
		client.post(context, params, Constants.SET_BANK_DEAULT,
				new GsonHttpResponseHandler(mHandler, what) {
					@Override
					protected Object parseJson(JsonObject response) {
						// TODO Auto-generated method stub
						return JsonMananger.jsonToBean(response.toString(),
								BaseResponse.class);
					}
				});

	}
	@Override
	public void queryBindBankAccount(Context context, Handler mHandler,
									 int what, String access_token) {
		// TODO Auto-generated method stub
		MyHttpClient client = MyHttpClient.getInstance();
		MyRequestParams params = new MyRequestParams(context);
		params.set("access_token", access_token);
		client.post(context, params, Constants.GET_BANK_CARDS,
				new GsonHttpResponseHandler(mHandler, what) {
					@Override
					protected Object parseJson(JsonObject response) {
						return ParseJsonObjectUtil.getBankCards(response);
					}
				});




	}

	@Override
	public void findBankName(Context context, Handler mHandler, int what, String access_token, String bankCard) {

		MyHttpClient client = MyHttpClient.getInstance();
		MyRequestParams params = new MyRequestParams(context);
		params.set("access_token", access_token);
		params.set("bankCard", bankCard);
		client.post(context, params, Constants.FIND_BANK_NAME,
				new GsonHttpResponseHandler(mHandler, what) {
					@Override
					protected Object parseJson(JsonObject response) {
						// TODO Auto-generated method stub
						Logger.i("cyc","----------findBankName-" + response.toString());
						return JsonMananger.jsonToBean(response.toString(),
								FindBankNameResponse.class);
					}
				});
	}

	@Override
	public void addBankCard(Context context, Handler mHandler, int what,
							String access_token, String bankNo, String bankName, String subBank, String personNo, String userRealName) {
		// TODO Auto-generated method stub
		MyHttpClient client = MyHttpClient.getInstance();
		MyRequestParams params = new MyRequestParams(context);
		params.set("access_token", access_token);
		params.set("bankNo", bankNo);
		params.set("bankName", bankName);
		params.set("subBank", subBank);
		params.set("personNo", personNo);
		params.set("userRealName", userRealName);
		client.post(context, params, Constants.ADD_BANK_CARD,
				new GsonHttpResponseHandler(mHandler, what) {
					@Override
					protected Object parseJson(JsonObject response) {
						// TODO Auto-generated method stub
						Logger.i("cyc", "-----------addBankCard---" + response.toString());
						return super.parseJson(response);
					}
				});

	}
	@Override
	public void queryBankInfo(Context context, Handler mHandler, int what,
							  String access_token) {
		// TODO Auto-generated method stub
		MyHttpClient client = MyHttpClient.getInstance();
		MyRequestParams params = new MyRequestParams(context);
		params.set("access_token", access_token);
		client.post(context, params, Constants.GET_BANK_INFO,
				new GsonHttpResponseHandler(mHandler, what) {
					@Override
					protected Object parseJson(JsonObject response) {
						// TODO Auto-generated method stub
						return ParseJsonObjectUtil.getBankInfos(response);
					}
				});

	}

}