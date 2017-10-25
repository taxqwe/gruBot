package com.fa.grubot.abstractions;

import com.fa.grubot.objects.DashboardEntry;

import java.util.ArrayList;

public interface DashboardFragmentBase {
    void setupRecyclerView(ArrayList<DashboardEntry> entries);
    void setupSwipeRefreshLayout();
}
