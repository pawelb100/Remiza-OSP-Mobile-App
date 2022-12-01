package edu.wseiz.remizaosp.models;

public class User {
    private String uid;
    private String name;
    private String email;
    private String role;
    private String statusId;

    public User() {}

    public User(String uid, String name, String email, String role, String statusId) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.role = role;
        this.statusId = statusId;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }
}
