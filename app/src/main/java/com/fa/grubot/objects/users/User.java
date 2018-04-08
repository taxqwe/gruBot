package com.fa.grubot.objects.users;

import com.github.badoualy.telegram.tl.api.TLAbsInputUser;
import com.stfalcon.chatkit.commons.models.IUser;

import java.io.Serializable;

public class User implements Serializable, IUser {

    private String userId;
    private String userType;
    private String fullname;
    private String userName;
    private String imgUrl;
    private String chatRole;
    private TLAbsInputUser inputUser;

    public User(String userId, String userType, String fullname, String userName, String imgUrl) {
        this.userId = userId;
        this.userType = userType;
        this.fullname = fullname;
        this.userName = userName;
        this.imgUrl = imgUrl;
    }

    @Override
    public String getId() {
        return userId;
    }

    @Override
    public String getName() {
        return fullname;
    }

    @Override
    public String getAvatar() {
        return imgUrl;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public String getPhoneNumber() {
        return "";
    }

    public String getChatRole() {
        return chatRole;
    }

    public void setChatRole(String chatRole) {
        this.chatRole = chatRole;
    }

    public TLAbsInputUser getInputUser() {
        return inputUser;
    }

    public void setInputUser(TLAbsInputUser inputUser) {
        this.inputUser = inputUser;
    }
}
