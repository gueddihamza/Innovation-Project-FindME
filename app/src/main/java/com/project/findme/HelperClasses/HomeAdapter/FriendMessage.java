package com.project.findme.HelperClasses.HomeAdapter;

public class FriendMessage {
    String userUID;
    String friendUID;
    String message;
    String username;

    public FriendMessage() {
    }

    public FriendMessage(String userUID, String friendUID, String message, String username) {
        this.userUID = userUID;
        this.friendUID = friendUID;
        this.message = message;
        this.username = username;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getFriendUID() {
        return friendUID;
    }

    public void setFriendUID(String friendUID) {
        this.friendUID = friendUID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
