package com.fa.grubot.abstractions;

import com.fa.grubot.objects.dashboard.Action;

import java.util.ArrayList;

public interface ActionsFragmentBase extends FragmentBase{
    void setupRecyclerView(ArrayList<Action> entries);
    void setupSwipeRefreshLayout(int state);
    void setupLayouts(boolean isNetworkAvailable, boolean isHasData);
}
