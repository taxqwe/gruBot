package com.fa.grubot.abstractions;

import com.fa.grubot.objects.misc.ProfileItem;

import java.util.ArrayList;

public interface ProfileFragmentBase {
    void setupRecyclerView(ArrayList<ProfileItem> groups);
    void setupToolbar();
    void setupLayouts(boolean isNetworkAvailable);
    void setupRetryButton();
    void setupViews();
    void reloadFragment();
}
