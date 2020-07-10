package com.project.findme.HelperClasses;

import java.util.ArrayList;
import java.util.List;

public class GroupInfo {
    public String groupName;
    public String groupDescription;
    public String groupType;
    public String ownerUID;
    public List<String> usersUID = new ArrayList<String>();

    public GroupInfo() {
    }

    public GroupInfo(String groupName, String groupDescription, String groupType, String ownerUID, List<String> usersUID) {
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.groupType = groupType;
        this.ownerUID = ownerUID;
        this.usersUID = usersUID;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public String getGroupType() {
        return groupType;
    }

    public String getOwnerUID() {
        return ownerUID;
    }

    public List<String> getUsersUID() {
        return usersUID;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public void setOwnerUID(String ownerUID) {
        this.ownerUID = ownerUID;
    }

    public void setUsersUID(List<String> usersUID) {
        this.usersUID = usersUID;
    }

    @Override
    public String toString() {
        return "GroupInfo{" +
                "groupName='" + groupName + '\'' +
                ", groupDescription='" + groupDescription + '\'' +
                ", groupType='" + groupType + '\'' +
                ", ownerUID='" + ownerUID + '\'' +
                ", usersUID=" + usersUID.toString() +
                '}';
    }
}
