package com.project.findme.HelperClasses.HomeAdapter;



public class FriendsHelperClass {
    String fullname;
    String username;

    public FriendsHelperClass(String fullname, String username) {
        this.fullname = fullname;
        this.username = username;
    }

    public FriendsHelperClass() {
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
