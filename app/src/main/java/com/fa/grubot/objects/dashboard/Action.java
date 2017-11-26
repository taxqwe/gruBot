package com.fa.grubot.objects.dashboard;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Action implements Serializable {
    private String id;
    private DocumentReference group;
    private DocumentReference author;
    private String desc;
    private Date date;

    private String authorName;
    private String groupName;

    public Action() {

    }

    public Action(String id, DocumentReference group, DocumentReference author, String desc, Date date) {
        this.id = id;
        this.group = group;
        this.author = author;
        this.desc = desc;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public DocumentReference getGroup() {
        return group;
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
}
