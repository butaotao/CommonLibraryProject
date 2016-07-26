package com.dachen.incomelibrary.bean;

import java.io.Serializable;

public class BankInfo implements Serializable{

	private int id;
	private String bankName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	
}
