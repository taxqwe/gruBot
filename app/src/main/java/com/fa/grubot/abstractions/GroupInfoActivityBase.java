package com.fa.grubot.abstractions;

import com.fa.grubot.objects.GroupInfoButton;

import java.util.ArrayList;

public interface GroupInfoActivityBase {
    void setupFab();
    void setupToolbar();
    void setupRecyclerView(ArrayList<GroupInfoButton> buttons);
}
