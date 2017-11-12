package com.fa.grubot.abstractions;

public interface ProfileFragmentBase {
    void setupToolbar();
    void setupLayouts(boolean isNetworkAvailable);
    void setupViews();
    void setupRetryButton();
    void reloadFragment();
}
