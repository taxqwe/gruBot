package com.fa.grubot.objects.dashboard;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class Action implements Serializable {
    private String id;
    private String groupId;
    private String author;
    private String desc;
    private Date date;
    private long messageId;
    private Map<String, String> users;
    private String type;

    private String authorName;
    private String groupName;

    public Action() {

    }

    public Action(String id, String groupId, String groupName, String author, String authorName, String desc, Date date, Map<String, String> users, long messageId, String type) {
        this.id = id;
        this.groupId = groupId;
        this.author = author;
        this.desc = desc;
        this.date = date;
        this.users = users;
        this.authorName = authorName;
        this.groupName = groupName;
        this.messageId = messageId;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getGroup() {
        return groupId;
    }

    public String getAuthor() {
        return author;
    }

    public String getDesc() {
        return desc;
    }

    public String getDate() {
        Format formatter = new SimpleDateFormat("MMM dd, yyyy kk:mm", Locale.getDefault());
        return formatter.format(this.date);
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getGroupName() {
        return groupName;
    }

    public Map<String, String> getUsers() {
        return users;
    }

    public String getGroupId() {
        return groupId;
    }

    public long getMessageId() {
        return messageId;
    }

    public String getType() {
        return type;
    }
}
