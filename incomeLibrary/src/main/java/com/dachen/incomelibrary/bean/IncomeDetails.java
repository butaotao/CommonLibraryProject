package com.dachen.incomelibrary.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/1/12.
 */
public class IncomeDetails extends BaseModel {
    private static final long serialVersionUID = -3700912189606530863L;
    private String keyYM;
    private List<IncomeDetailModel> list;


    public String getKeyYM() {
        return keyYM;
    }

    public void setKeyYM(String keyYM) {
        this.keyYM = keyYM;
    }

    public List<IncomeDetailModel> getList() {
        return list;
    }

    public void setList(List<IncomeDetailModel> list) {
        this.list = list;
    }

}
