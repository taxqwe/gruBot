package com.fa.grubot.abstractions;

import android.view.View;

import com.fa.grubot.objects.DashboardEntry;

import java.util.ArrayList;

public interface DashboardFragmentBase {
    void setupRecyclerView(ArrayList<DashboardEntry> entries);
    void setupSwipeRefreshLayout();
    void setupLayouts(boolean isNetworkAvailable);
    void setupViews(int layout, View v);
    void setupRetryButton();
    void reloadFragment();
}
