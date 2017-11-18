package com.fa.grubot.abstractions;

import com.fa.grubot.adapters.GroupInfoRecyclerAdapter;

import java.util.ArrayList;

public interface GroupInfoFragmentBase {
    void setupFab();
    void setupToolbar();
    void setupRecyclerView(ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> buttons);
    void setupLayouts(boolean isNetworkAvailable);
    void setupRetryButton();
    void setupViews();
    void reloadFragment();
}
