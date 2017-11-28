package com.fa.grubot.objects.dashboard;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class Action implements Serializable {
    private String id;
    private String groupId;
    private DocumentReference author;
    private String desc;
    private Date date;
    private Map<String, Boolean> users;

    private String authorName;
    private String groupName;

    public Action() {

    }

    public Action(String groupId, DocumentReference author, String desc, Date date, Map<String, Boolean> users) {
        this.groupId = groupId;
        this.author = author;
        this.desc = desc;
        this.date = date;
        this.users = users;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getGroup() {
        return groupId;
    }

    public DocumentReference getAuthor() {
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

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Map<String, Boolean> getUsers() {
        return users;
    }
}
