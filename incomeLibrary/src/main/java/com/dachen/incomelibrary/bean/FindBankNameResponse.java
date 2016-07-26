package com.dachen.incomelibrary.bean;

import java.io.Serializable;

public class FindBankNameResponse extends BaseResponse{

	private BankInfo data;

	public BankInfo getData() {
		return data;
	}

	public void setData(BankInfo data) {
		this.data = data;
	}

	public class BankInfo implements Serializable {
//		"bankBinNo":622588,"bankCateName":"一卡通","bankCode":"3080000","bankName":"招商银行银行","bankNoLength":16,"bankNoType":"借记卡","id":"56a60de924eccb15dcac7f96"
		private String id;
		private String bankName;
		private String bankCateName;
		private String bankNoType;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getBankName() {
			return bankName;
		}

		public void setBankName(String bankName) {
			this.bankName = bankName;
		}

		public String getBankCateName() {
			return bankCateName;
		}

		public void setBankCateName(String bankCateName) {
			this.bankCateName = bankCateName;
		}

		public String getBankNoType() {
			return bankNoType;
		}

		public void setBankNoType(String bankNoType) {
			this.bankNoType = bankNoType;
		}
	}

}
