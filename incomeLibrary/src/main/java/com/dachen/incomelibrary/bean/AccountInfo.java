package com.dachen.incomelibrary.bean;

import java.io.Serializable;

public class AccountInfo implements Serializable {


	private  int id;
	private int bankId;
	private String bankName;
	private String bankNo;
	private String subBank;
	private boolean isDefault;
	private String bankIoc;
	private String personNo;

	public String getPersonNo() {
		return personNo;
	}

	public void setPersonNo(String personNo) {
		this.personNo = personNo;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBankId() {
		return bankId;
	}
	public void setBankId(int bankId) {
		this.bankId = bankId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public String getSubBank() {
		return subBank;
	}
	public void setSubBank(String subBank) {
		this.subBank = subBank;
	}

	public boolean isIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean aDefault) {
		isDefault = aDefault;
	}

	public String getBankIoc() {
		return bankIoc;
	}

	public void setBankIoc(String bankIoc) {
		this.bankIoc = bankIoc;
	}
}
