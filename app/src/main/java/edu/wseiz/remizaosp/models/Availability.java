package edu.wseiz.remizaosp.models;

public class Availability {

    private String userId;
    private String statusId;

    public Availability() {}

    public Availability(String userId, String statusId) {
        this.userId = userId;
        this.statusId = statusId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }


}
