package com.fa.grubot.objects;

import java.io.Serializable;
import java.util.Map;

public class Chat implements Serializable {
    private String id;
    private String name;
    private Map<String, Boolean> users;
    private String lastMessage;

    public Chat(String id, String name, Map<String, Boolean> users, String lastMessage) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.users = users;
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
}
