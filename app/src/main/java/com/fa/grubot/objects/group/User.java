package com.fa.grubot.objects.group;

import com.stfalcon.chatkit.commons.models.IUser;

import java.io.Serializable;

public class User implements IUser, Serializable {

    private int id;
    private String username;
    private String fullname;
    private String phoneNumber;
    private String desc;
    private String imgUrl;

    public User(int id, String username, String fullname, String phoneNumber, String desc, String imgUrl) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
        this.desc = desc;
        this.imgUrl = imgUrl;
    }

    @Override
    public String getAvatar() {
        return imgUrl;
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    @Override
    public String getName() {
        return fullname;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDesc() {
        return desc;
    }
}
