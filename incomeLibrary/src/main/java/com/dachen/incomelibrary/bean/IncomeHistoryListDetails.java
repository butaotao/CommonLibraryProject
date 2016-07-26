package com.dachen.incomelibrary.bean;

import java.util.List;

/**
 * Created by TianWei on 2016/1/13.
 */
public class IncomeHistoryListDetails extends BaseModel {

    private static final long serialVersionUID = -7131933798940368533L;
    private String keyY;
    private long totalMoney;
    private String totalNum;

    public String getKeyY() {
        return keyY;
    }

    public void setKeyY(String keyY) {
        this.keyY = keyY;
    }

    public long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(long totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public List<IncomeHistoryListDetailModel> mList;

    public class IncomeHistoryListDetailModel {
        private String keyM;
        private long totalMoney;
        private String totalNum;

        public String getKeyM() {
            return keyM;
        }

        public void setKeyM(String keyM) {
            this.keyM = keyM;
        }

        public long getTotalMoney() {
            return totalMoney;
        }

        public void setTotalMoney(long totalMoney) {
            this.totalMoney = totalMoney;
        }

        public String getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(String totalNum) {
            this.totalNum = totalNum;
        }
    }
}
