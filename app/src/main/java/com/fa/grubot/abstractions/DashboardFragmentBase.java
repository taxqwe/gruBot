package com.fa.grubot.abstractions;

import com.fa.grubot.objects.dashboard.DashboardEntry;

import java.util.ArrayList;

public interface DashboardFragmentBase {
    void setupRecyclerView(ArrayList<DashboardEntry> entries);
    void setupSwipeRefreshLayout(int layout);
    void setupLayouts(boolean isNetworkAvailable, boolean isHasData);
    void setupRetryButton();
    void reloadFragment();
}
