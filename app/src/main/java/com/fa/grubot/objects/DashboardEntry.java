package com.fa.grubot.objects;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DashboardEntry implements Serializable {
    private int id;
    private int type;
    private Group group;
    private String author;
    private String desc;
    private Date date;

    public DashboardEntry() {

    }

    public DashboardEntry(int id, Group group, String author, String desc, Date date) {
        this.id = id;
        this.group = group;
        this.author = author;
        this.desc = desc;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public Group getGroup() {
        return group;
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
}
