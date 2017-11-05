package com.fa.grubot.objects.group;

import com.fa.grubot.objects.dashboard.DashboardEntry;

import java.util.ArrayList;

public class GroupInfoButton {
    private int id;
    private String text;
    private ArrayList<DashboardEntry> childList;

    public GroupInfoButton(int id, String text, ArrayList<DashboardEntry> childList) {
        this.id = id;
        this.text = text;
        this.childList = childList;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getChildCount() {
        return childList.size();
    }
}
