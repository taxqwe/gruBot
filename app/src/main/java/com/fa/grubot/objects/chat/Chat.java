package com.fa.grubot.objects.chat;

import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.util.Log;

import com.fa.grubot.util.DataType;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Chat implements Serializable, Cloneable, Comparable<Chat> {
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

    @Override
    public int compareTo(@NonNull Chat chat) {
        Long dateThisMillis = this.lastMessageDate * (this.type.equals(DataType.VK) ? 1000 : 1);
        Long dateThatMillis = chat.getLastMessageDate() * (chat.getType().equals(DataType.VK) ? 1000 : 1);

        Date dateThis = new Date(dateThisMillis);
        Date dateThat = new Date(dateThatMillis);

        if (dateThis.after(dateThat)) {
            return -1;
        } else if (dateThis.before(dateThat)){
            return 1;
        } else {
            return 0;
        }
    }

    public String getLastMessageDateAsString() {
            Long date = lastMessageDate * (type.equals(DataType.VK) ? 1000 : 1);
            SimpleDateFormat dateFormat;

            if (DateUtils.isToday(date))
                dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            else if (DateUtils.isToday(date + DateUtils.DAY_IN_MILLIS))
                return "Вчера";
            else
                dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());

            return dateFormat.format(date);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Chat){
            Chat comparableChat = (Chat) obj;
            if (id.equals(comparableChat.getId()) &&
                    getLastMessageDate() == comparableChat.getLastMessageDate()){
                return true;
            }
        }
        return false;
    }
}
