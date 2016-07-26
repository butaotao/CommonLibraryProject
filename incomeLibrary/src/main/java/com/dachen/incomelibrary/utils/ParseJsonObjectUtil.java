package com.dachen.incomelibrary.utils;


import com.dachen.incomelibrary.bean.AccountInfo;
import com.dachen.incomelibrary.bean.BankInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;


public class ParseJsonObjectUtil {
	
	public static List<AccountInfo> getBankCards(JsonObject response){
		if(!response.has("data")){
			return null;
		}
		JsonArray jsonArray=response.get("data").getAsJsonArray();
		List<AccountInfo>  accountInfos=new ArrayList<AccountInfo>();
		for(JsonElement element:jsonArray){
			JsonObject jsonObject=element.getAsJsonObject();
			AccountInfo accountInfo=new AccountInfo();
			if(jsonObject.has("id")){
			
				accountInfo.setId(jsonObject.get("id").getAsInt());
			}
			if(jsonObject.has("bankId")){
				accountInfo.setBankId(jsonObject.get("bankId").getAsInt());
			}
			if(jsonObject.has("bankName")){
				accountInfo.setBankName(jsonObject.get("bankName").getAsString());
			}
			if(jsonObject.has("bankNo")){
				accountInfo.setBankNo(jsonObject.get("bankNo").getAsString());
			}
			if(jsonObject.has("subBank")){
				accountInfo.setSubBank(jsonObject.get("subBank").getAsString());
			}
			if(jsonObject.has("isDefault")){
				accountInfo.setIsDefault(jsonObject.get("isDefault").getAsBoolean());
			}
			if(jsonObject.has("bankIoc")){
				accountInfo.setBankIoc(jsonObject.get("bankIoc").getAsString());
			}
			if(jsonObject.has("personNo")){
				accountInfo.setPersonNo(jsonObject.get("personNo").getAsString());
			}
			accountInfos.add(accountInfo);
		}
		return accountInfos ;
		
	}
	public static List<BankInfo> getBankInfos(JsonObject response){

		if(!response.has("data")){
			return null;
		}
		JsonArray jsonArray=response.get("data").getAsJsonArray();
		List<BankInfo> bankInfos=new ArrayList<BankInfo>();
		for(JsonElement element:jsonArray){
			JsonObject jsonObject=element.getAsJsonObject();
			BankInfo bankInfo=new BankInfo();
			if(jsonObject.has("bankName")){
				bankInfo.setBankName(jsonObject.get("bankName").getAsString());
			}
			if(jsonObject.has("id")){
				bankInfo.setId(jsonObject.get("id").getAsInt());
			}
			bankInfos.add(bankInfo);

		}

		return bankInfos;
	}

}
