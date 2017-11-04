package com.fa.grubot.objects;

import com.stfalcon.chatkit.commons.models.IUser;

/**
 * Created by ni.petrov on 04/11/2017.
 */

public class ChatUser implements IUser {

    private String id;

    private String name;

    private String avatar;

    public ChatUser(String id, String name, String avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }
}
