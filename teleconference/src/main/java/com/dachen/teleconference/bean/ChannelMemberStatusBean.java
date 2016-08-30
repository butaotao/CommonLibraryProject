package com.dachen.teleconference.bean;

import java.io.Serializable;

/**
 * Created by TianWei on 2016/8/30.
 */
public class ChannelMemberStatusBean implements Serializable {
    private static final long serialVersionUID = 1151964846346631905L;


    /**
     * member : 102396
     * inOther : false
     * channelId : 3111434291810731
     * status : 0
     */

    private String member;
    private boolean inOther;
    private String channelId;
    private int status;

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public boolean isInOther() {
        return inOther;
    }

    public void setInOther(boolean inOther) {
        this.inOther = inOther;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
