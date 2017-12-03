package com.fa.grubot.abstractions;

import com.fa.grubot.objects.dashboard.DashboardItem;

import java.util.ArrayList;

public interface DashboardFragmentBase extends FragmentBase {
    void setupRecyclerView(ArrayList<DashboardItem> items);
    void handleListUpdate(int count, int type);
    void setupLayouts(boolean isNetworkAvailable);
    boolean isAdapterExists();
}
