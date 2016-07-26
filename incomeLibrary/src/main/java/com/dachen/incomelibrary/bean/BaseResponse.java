/*
    ShengDao Android Client, BaseResponse
    Copyright (c) 2014 ShengDao Tech Company Limited
 */

package com.dachen.incomelibrary.bean;

import android.os.Handler;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;

/**
 * [返回结果基类]
 * 
 * @author devin.hu
 * @version 1.0
 * @date 2013-9-30
 **/
public class BaseResponse extends BaseModel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5616901114632786764L;

	private int resultCode;
	private String resultMsg;
	private int pageNo;
	private int pageSize;
	private int totalCount;
	

	public int getPageNo() {
		return pageNo;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public boolean isSuccess(){
		if(resultCode == 1){
			return true;
		}
		return false;
	}
	
	/**
	 * 处理分页
	 * 
	 * @param refreshlistview
	 * @param pageNo
	 * @param totalCount
	 */
	@SuppressWarnings("rawtypes")
	public void doPageInfo(final PullToRefreshBase refreshlistview, int pageNo, int totalCount) {
		doPageInfo(refreshlistview, pageNo, totalCount, 20);
	}

	/**
	 * 处理分页
	 * 
	 * @param refreshlistview
	 * @param pageNo
	 * @param totalCount
	 */
	@SuppressWarnings("rawtypes")
	public void doPageInfo(final PullToRefreshBase refreshlistview, int pageNo, int totalCount, int pagesize) {
		if ((pageNo * pagesize) >= totalCount) {
			new Handler().postAtTime(new Runnable() {
				@Override
				public void run() {
					refreshlistview.setMode(Mode.PULL_FROM_START);
				}
			}, 1000);
		} else {
			refreshlistview.setMode(Mode.BOTH);
		}
	}
}
