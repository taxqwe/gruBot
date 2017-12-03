package com.fa.grubot.abstractions;

import com.fa.grubot.objects.group.User;
import com.fa.grubot.objects.misc.ProfileItem;

import java.util.ArrayList;

public interface ProfileFragmentBase {
    void setupRecyclerView(ArrayList<ProfileItem> items, User user);
    void handleProfileUpdate(User user, ArrayList<String> changes);
    void setupLayouts(boolean isNetworkAvailable);
    void setupToolbar(User user);
    void setupRetryButton();
    void showRequiredViews();
    boolean isAdapterExists();
}
