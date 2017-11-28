package com.fa.grubot.util;

import java.util.Map;

public class Usr {

    private String username;
    private String fullname;
    private String phoneNumber;
    private String desc;
    private String imgUrl;
    private Map<String, Boolean> groups;

    public Usr(String username, String fullname, String phoneNumber, String desc, String imgUrl, Map<String, Boolean> groups) {
        this.username = username;
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
        this.desc = desc;
        this.imgUrl = imgUrl;
        this.groups = groups;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Map<String, Boolean> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, Boolean> groups) {
        this.groups = groups;
    }
}
