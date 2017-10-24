package com.fa.grubot.abstractions;

import com.fa.grubot.objects.Group;

import java.util.ArrayList;

public interface GroupsFragmentBase {
    void setupRecyclerView(ArrayList<Group> entries);
    void setupSwipeRefreshLayout();
}
