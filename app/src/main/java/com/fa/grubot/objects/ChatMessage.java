package com.fa.grubot.objects;


import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

/**
 * Created by ni.petrov on 22/10/2017.
 */

public class ChatMessage implements IMessage {

    private String id;

    private String text;

    private IUser user;

    private Date createdAt;

    public ChatMessage(String id, String text, IUser user, Date createdAd) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.createdAt = createdAd;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public IUser getUser() {
        return user;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }
}
