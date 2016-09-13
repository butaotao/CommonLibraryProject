package com.dachen.community.data.results;

import com.dachen.common.http2.result.BaseResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pqixi on 2016/9/13 0013.
 * 举报页面请求结果
 */
public class ReportResult extends BaseResult {

    private List<Type> types;

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public List<String> getTypeStringList() {
        if (types == null) {
            return new ArrayList<>();
        }

        List<String> stringList = new ArrayList<>();
        for (Type t : types) {
            stringList.add(t.getDesc());
        }
        return stringList;
    }

    public class Type {
        private Integer type;
        private String desc;

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
