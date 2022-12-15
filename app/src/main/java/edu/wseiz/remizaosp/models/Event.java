package edu.wseiz.remizaosp.models;

import java.util.List;

public class Event {

    private String uid;

    private String title;
    private String description;
    private boolean ongoing;

    private Address address;
    private long timestamp;

    private List<Participation> participationList;

    public Event() {}

    public Event(String uid, String title, String description, Address address, long timestamp, List<Participation> participationList) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.ongoing = true;
        this.address = address;
        this.timestamp = timestamp;
        this.participationList = participationList;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOngoing() {
        return ongoing;
    }

    public void setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<Participation> getParticipationList() {
        return participationList;
    }

    public void setParticipationList(List<Participation> participationList) {
        this.participationList = participationList;
    }
}
