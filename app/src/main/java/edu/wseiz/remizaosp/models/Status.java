package edu.wseiz.remizaosp.models;

public class Status {

    private String uid;
    private String title;

    public Status() {
    }

    public Status(String id, String title) {
        this.uid = id;
        this.title = title;
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
}
