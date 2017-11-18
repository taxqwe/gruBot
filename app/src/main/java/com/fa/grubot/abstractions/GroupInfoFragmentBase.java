package com.fa.grubot.abstractions;

import com.fa.grubot.adapters.GroupInfoRecyclerAdapter;

import java.util.ArrayList;

public interface GroupInfoFragmentBase extends FragmentBase {
    void setupFab();
    void setupRecyclerView(ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> buttons);
    void setupLayouts(boolean isNetworkAvailable);
}
