package com.dachen.incomelibrary.bean;

/**
 * Created by Administrator on 2016/1/13.
 */
public class IncomeDetailModel extends BaseModel {
//  {"keyYM":"2015年09月","list":[{"day":"15","money":100,"orderTypeName":"图文咨询","type":11}],"totalMoney":0,"totalNum":0}

    private static final long serialVersionUID = -951275818587221122L;
    private String day;
    private long money;  //分
    private int orderId;
    private String orderTypeName;
    private int type;
    private String doctorName;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderTypeName() {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
