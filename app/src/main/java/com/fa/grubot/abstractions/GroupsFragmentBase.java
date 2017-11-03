package com.fa.grubot.abstractions;

import android.view.View;

import com.fa.grubot.objects.Group;

import java.util.ArrayList;

public interface GroupsFragmentBase {
    void setupRecyclerView(ArrayList<Group> groups);
    void setupSwipeRefreshLayout(int layout);
    void setupLayouts(boolean isNetworkAvailable, boolean isHasData);
    void setupViews(int layout, View v);
    void setupRetryButton();
    void reloadFragment();
}
