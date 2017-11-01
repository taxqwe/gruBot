package com.fa.grubot.abstractions;

import com.fa.grubot.adapters.GroupInfoRecyclerAdapter;

import java.util.ArrayList;

public interface GroupInfoActivityBase {
    void setupFab();
    void setupToolbar();
    void setupRecyclerView(ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> buttons);
}
