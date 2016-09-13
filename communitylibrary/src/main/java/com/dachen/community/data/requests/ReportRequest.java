package com.dachen.community.data.requests;

import com.dachen.common.http2.request.BaseRequest;

/**
 * Created by pqixi on 2016/9/12 0012.
 */
public class ReportRequest extends BaseRequest {
    private String topicId;
    private Integer userId;
    private Integer type;
    private String desc;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

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
