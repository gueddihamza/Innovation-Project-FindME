package com.project.findme.HelperClasses.HomeAdapter;

public class GroupMessage {
    String groupUID;
    String senderUID;
    String message;
    String username;

    public GroupMessage(String groupUID, String senderUID, String message, String username) {
        this.groupUID = groupUID;
        this.senderUID = senderUID;
        this.message = message;
        this.username = username;
    }

    public GroupMessage() {
    }

    @Override
    public String toString() {
        return "GroupMessage{" +
                "groupUID='" + groupUID + '\'' +
                ", senderUID='" + senderUID + '\'' +
                ", message='" + message + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGroupUID() {
        return groupUID;
    }

    public void setGroupUID(String groupUID) {
        this.groupUID = groupUID;
    }

    public String getSenderUID() {
        return senderUID;
    }

    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
