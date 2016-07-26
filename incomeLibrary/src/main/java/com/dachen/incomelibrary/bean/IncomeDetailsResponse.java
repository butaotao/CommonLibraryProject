package com.dachen.incomelibrary.bean;

public class IncomeDetailsResponse extends BaseResponse {

	private static final long serialVersionUID = -6002819035522330816L;

	private IncomeDetailsData data;

	public IncomeDetailsData getData() {
		return data;
	}

	public void setData(IncomeDetailsData data) {
		this.data = data;
	}
}
