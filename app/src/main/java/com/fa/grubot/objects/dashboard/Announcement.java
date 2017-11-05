package com.fa.grubot.objects.dashboard;

import com.fa.grubot.objects.group.Group;

import java.util.Date;

public class Announcement extends DashboardEntry {
    private String text;

    public Announcement(int id, Group group, String author, String desc, Date date, String text) {
        super(id, group, author, desc, date);

        this.text = text;
    }

    public String getText() {
        return text;
    }
}
