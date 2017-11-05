package com.fa.grubot.objects.dashboard;

import com.fa.grubot.objects.group.Group;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DashboardEntry implements Serializable {
    private int id;
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
