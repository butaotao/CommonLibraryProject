package com.dachen.teleconference.bean;

/**
 * Created by TianWei on 2016/8/31.
 */
public class ImMeetingMemberBean {
    /**
     * channelId : 3225447141127496
     * startTime  : 1472638605000
     * creater : 102366
     * confStatus  : 1
     * planEndTime : 1472642205000
     */

    private String channelId;
    private String startTime;
    private String creater;
    private String confStatus;
    private String planEndTime;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getConfStatus() {
        return confStatus;
    }

    public void setConfStatus(String confStatus) {
        this.confStatus = confStatus;
    }

    public String getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(String planEndTime) {
        this.planEndTime = planEndTime;
    }
}
