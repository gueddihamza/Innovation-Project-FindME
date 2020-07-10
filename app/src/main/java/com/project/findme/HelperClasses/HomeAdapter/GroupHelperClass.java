package com.project.findme.HelperClasses.HomeAdapter;

import java.util.ArrayList;
import java.util.List;

public class GroupHelperClass {
    String name;
    String description;
    String type;
    String owner;
    boolean isMember;
    List<String> listUID;
    String key;

    public GroupHelperClass(String name, String description, String type, String owner, boolean isMember, List<String> listUID,String key) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.owner = owner;
        this.isMember = isMember;
        this.listUID = listUID;
        this.key=key;

    }

    @Override
    public String toString() {
        return "GroupHelperClass{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", owner='" + owner + '\'' +
                ", isMember=" + isMember +
                ", listUID=" + listUID +
                ", key='" + key + '\'' +
                '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getListUID() {
        return listUID;
    }

    public void setListUID(List<String> listUID) {
        this.listUID = listUID;
    }


    public GroupHelperClass() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
    }
}
