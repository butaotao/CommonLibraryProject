package com.dachen.teleconference.bean;

/**
 * Created by TianWei on 2016/9/6.
 */
public class DelayMeetingMsgBean {
    private int type;
    private Param param;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Param getParam() {
        return param;
    }

    public void setParam(Param param) {
        this.param = param;
    }

    public static class Param {
        private String leftTime;

        public String getLeftTime() {
            return leftTime;
        }

        public void setLeftTime(String leftTime) {
            this.leftTime = leftTime;
        }
    }

}
