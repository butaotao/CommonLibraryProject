package com.dachen.incomelibrary.bean;

/**
 * Created by lmc on 2016/1/14.
 */
public class IncomePaidListDetails extends BaseModel {
    //            "actualMoney": 900,
    //            "createTime": 1452671457752,
    //            "id": 1,
    //            "settleMoney": 1000,
    //            "settleTableName": "网银转账",
    //            "status": 2,
    //            "taxMoney": 0,
    //            "userBankId": 14,
    //            "userId": "641",
    //            "userType": 2

    private static final long serialVersionUID = 737144382582873695L;
    private long actualMoney;
    private long createTime;
    private int id;
    private long settleMoney;
    private String settleTableName;
    private int status;
    private String taxMoney;
    private int userBankId;
    private String userId;
    private int userType;


    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getActualMoney() {
        return actualMoney;
    }

    public void setActualMoney(long actualMoney) {
        this.actualMoney = actualMoney;
    }

    public long getSettleMoney() {
        return settleMoney;
    }

    public void setSettleMoney(long settleMoney) {
        this.settleMoney = settleMoney;
    }

    public String getSettleTableName() {
        return settleTableName;
    }

    public void setSettleTableName(String settleTableName) {
        this.settleTableName = settleTableName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTaxMoney() {
        return taxMoney;
    }

    public void setTaxMoney(String taxMoney) {
        this.taxMoney = taxMoney;
    }

    public int getUserBankId() {
        return userBankId;
    }

    public void setUserBankId(int userBankId) {
        this.userBankId = userBankId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
