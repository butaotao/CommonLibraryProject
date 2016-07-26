package com.dachen.incomelibrary.bean;

/**
 * Created by TianWei on 2016/1/12.
 */
public class IncomeInfoData {
    private long balance;
    private long unbalance;
    private long totalIncome;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(long totalIncome) {
        this.totalIncome = totalIncome;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getUnbalance() {
        return unbalance;
    }

    public void setUnbalance(long unbalance) {
        this.unbalance = unbalance;
    }
}
