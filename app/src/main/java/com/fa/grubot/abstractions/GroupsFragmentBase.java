package com.fa.grubot.abstractions;

import com.fa.grubot.objects.group.Group;

import java.util.ArrayList;

public interface GroupsFragmentBase {
    void setupRecyclerView(ArrayList<Group> groups);
    void setupSwipeRefreshLayout(int layout);
    void setupToolbar();
    void setupLayouts(boolean isNetworkAvailable, boolean isHasData);
    void setupRetryButton();
    void setupViews();
    void reloadFragment();
}
