package com.dachen.teleconference.bean;

/**
 * Created by TianWei on 2016/8/26.
 */
public class GetConfInfoByChannelIdResponse extends BaseResponse {


    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private String id;
        private String creater;
        private String createTime;
        private String endTime;
        private String planEndTime;
        private String channelId;
        private String recordUrl;
        private String status;
        private String memberStatus;
        private String member;
        private String newJoiner;
        private String gId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreater() {
            return creater;
        }

        public void setCreater(String creater) {
            this.creater = creater;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getPlanEndTime() {
            return planEndTime;
        }

        public void setPlanEndTime(String planEndTime) {
            this.planEndTime = planEndTime;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getRecordUrl() {
            return recordUrl;
        }

        public void setRecordUrl(String recordUrl) {
            this.recordUrl = recordUrl;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMemberStatus() {
            return memberStatus;
        }

        public void setMemberStatus(String memberStatus) {
            this.memberStatus = memberStatus;
        }

        public String getMember() {
            return member;
        }

        public void setMember(String member) {
            this.member = member;
        }

        public String getNewJoiner() {
            return newJoiner;
        }

        public void setNewJoiner(String newJoiner) {
            this.newJoiner = newJoiner;
        }

        public String getgId() {
            return gId;
        }

        public void setgId(String gId) {
            this.gId = gId;
        }
    }
}
