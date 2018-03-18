package com.fa.grubot.objects.chat;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class Chat implements Serializable, Cloneable {
    private String id;
    private String name;
    private Map<String, Boolean> users;
    private String imgUri;
    private String lastMessage;
    private String type;
    private long lastMessageDate;
    private String lastMessageFrom;

    public Chat(String id, String name, Map<String, Boolean> users, String imgUri, String lastMessage, String type, long lastMessageDate, String lastMessageFrom) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.imgUri = imgUri;
        this.users = users;
        this.type = type;
        this.lastMessageDate = lastMessageDate;
        this.lastMessageFrom = lastMessageFrom;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, Boolean> getUsers() {
        return users;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setUsers(Map<String, Boolean> users) {
        this.users = users;
    }

    public String getImgURI() {
        return imgUri;
    }

    public String getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImgUri() {
        return imgUri;
    }

    public long getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(long lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public String getLastMessageFrom() {
        return lastMessageFrom;
    }

    public void setLastMessageFrom(String lastMessageFrom) {
        this.lastMessageFrom = lastMessageFrom;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
