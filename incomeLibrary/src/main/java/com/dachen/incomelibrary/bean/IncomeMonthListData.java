package com.dachen.incomelibrary.bean;

import java.util.List;

/**
 * Created by TianWei on 2016/1/13.
 */
public class IncomeMonthListData extends BaseModel {
    private static final long serialVersionUID = 7922638495234705327L;
    private String keyYM;
    private long totalMoney;
    private String totalNum;
    private int pageCount;
    private int pageIndex;
    private int pageSize;
    private int start;
    private int total;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getKeyYM() {
        return keyYM;
    }

    public void setKeyYM(String keyYM) {
        this.keyYM = keyYM;
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

    public List<OrderIncomeDetail> shareList;
    public List<SharePartIncomeDetail> unshareList;


    public class OrderIncomeDetail {
        private String day;
        private long money;
        private String orderId;
        private String orderTypeName;
        private int type;

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

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
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
    }

    public class SharePartIncomeDetail {

        private String day;
        private String doctorName;
        private long money;
        private String orderTypeName;
        private int type;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public long getMoney() {
            return money;
        }

        public void setMoney(long money) {
            this.money = money;
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
    }
}
