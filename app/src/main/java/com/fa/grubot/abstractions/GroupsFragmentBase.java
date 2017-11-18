package com.fa.grubot.abstractions;

import com.fa.grubot.objects.group.Group;

import java.util.ArrayList;

public interface GroupsFragmentBase extends FragmentBase {
    void setupRecyclerView(ArrayList<Group> groups);
    void setupSwipeRefreshLayout();
    void setupLayouts(boolean isNetworkAvailable, boolean isHasData);
}
