package com.project.findme.HelperClasses;

import java.io.Serializable;

public class UserInfo  {
    private String fullName;
    private String username;
    private String email;
    private String phoneNumber;
    private String function;

    @Override
    public String toString() {
        return "UserInfo{" +
                "fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", function='" + function + '\'' +
                '}';
    }


    public UserInfo() {
    }

    public UserInfo(String fullName, String username, String email, String phoneNumber, String function) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.function = function;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFunction() {
        return function;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFunction(String function) {
        this.function = function;
    }

}
