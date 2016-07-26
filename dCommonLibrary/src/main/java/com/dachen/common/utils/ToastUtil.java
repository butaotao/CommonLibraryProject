package com.dachen.common.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.dachen.common.R;
import com.dachen.medicine.entity.Result;

public class ToastUtil {

	public static void showErrorNet(Context context) {
		if (context == null) {
			return;
		}
		showToast(context, R.string.net_exception);
	}

	public static void showErrorData(Context context) {
		if (context == null) {
			return;
		}
		showToast(context, R.string.data_exception);
	}

	//
	// public static void showFrequent(Context context) {
	// if (context == null) {
	// return;
	// }
	// showNormalToast(context, R.string.request_busy);
	// }
	//
	// public static void showNoMoreData(Context context) {
	// if (context == null) {
	// return;
	// }
	// showNormalToast(context, R.string.no_more_data);
	// }

	public static void showToast(Context context, String message) {
		if (context == null) {
			return;
		}
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static void showToast(Context context, int resId) {
		if (context == null) {
			return;
		}
		Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT).show();
	}
	public static void showToast(Context context, Result result) {
		if (context == null) {
			return;
		}
		if (!TextUtils.isEmpty(result.resultMsg)){
			Toast.makeText(context,  result.resultMsg, Toast.LENGTH_SHORT).show();
		}

	}

}
