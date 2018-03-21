package com.fa.grubot.objects.users;

import java.io.Serializable;

public class User implements Serializable {

    private String userId;
    private String userType;
    private String fullname;
    private String userName;
    private String phoneNumber;
    private String imgUrl;

    public User(String userId, String userType, String fullname, String userName, String phoneNumber, String imgUrl) {
        this.userId = userId;
        this.userType = userType;
        this.fullname = fullname;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.imgUrl = imgUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserType() {
        return userType;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
