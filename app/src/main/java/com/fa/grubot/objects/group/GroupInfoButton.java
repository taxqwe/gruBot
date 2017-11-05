package com.fa.grubot.objects.group;

import com.fa.grubot.adapters.GroupInfoRecyclerAdapter;
import com.fa.grubot.objects.dashboard.DashboardEntry;

import java.util.ArrayList;

public class GroupInfoButton<T> {
    private int id;
    private String text;
    private ArrayList<Class<T>> childList;

    public GroupInfoButton(int id, String text, ArrayList<Class<T>> childList) {
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

    public <S> void addChild(S entry) {
        childList.add((Class<T>) entry);
    }
    public int getChildCount() {
        return childList.size();
    }
}
