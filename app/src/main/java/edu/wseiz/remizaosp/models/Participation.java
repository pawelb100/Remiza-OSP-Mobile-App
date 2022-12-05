package edu.wseiz.remizaosp.models;

public class Participation {

    private String userId;
    private boolean isParticipating;

    public Participation(String userId, boolean participation) {
        this.userId = userId;
        this.isParticipating = participation;
    }

    public Participation() {}

    public String getUserId() {
        return userId;
    }

    public boolean isParticipating() {
        return isParticipating;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setParticipating(boolean participating) {
        this.isParticipating = participating;
    }
}
