package com.fa.grubot.abstractions;

public interface DashboardFragmentBase {
    void setupLayouts(boolean isNetworkAvailable);
    void setupViews();
    void setupRetryButton();
    void reloadFragment();
}
