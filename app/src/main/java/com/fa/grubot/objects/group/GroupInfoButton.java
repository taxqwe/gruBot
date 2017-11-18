package com.fa.grubot.objects.group;

import com.fa.grubot.adapters.GroupInfoRecyclerAdapter;

import java.util.ArrayList;

public class GroupInfoButton {
    private int id;
    private String text;
    private ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> childList;

    public GroupInfoButton(int id, String text, ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> childList) {
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

    public void addChild(GroupInfoRecyclerAdapter.GroupInfoRecyclerItem item) {
        childList.add(item);
    }
    public int getChildCount() {
        return childList.size();
    }
}
