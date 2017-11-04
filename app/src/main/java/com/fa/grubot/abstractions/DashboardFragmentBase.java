package com.fa.grubot.abstractions;

import android.view.View;

import com.fa.grubot.objects.DashboardEntry;

import java.util.ArrayList;

public interface DashboardFragmentBase {
    void setupRecyclerView(ArrayList<DashboardEntry> entries);
    void setupSwipeRefreshLayout(int layout);
    void setupLayouts(boolean isNetworkAvailable, boolean isHasData);
    void setupRetryButton();
    void reloadFragment();
}
