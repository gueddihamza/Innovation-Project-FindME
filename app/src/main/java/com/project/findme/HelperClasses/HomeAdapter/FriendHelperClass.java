package com.project.findme.HelperClasses.HomeAdapter;

public class FriendHelperClass {
    String username;
    String status;

    public FriendHelperClass() {
    }

    public FriendHelperClass(String username, String status) {
        this.username = username;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FriendHelperClass{" +
                "username='" + username + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
