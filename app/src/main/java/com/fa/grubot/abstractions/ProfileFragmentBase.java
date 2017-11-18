package com.fa.grubot.abstractions;

import com.fa.grubot.objects.misc.ProfileItem;

import java.util.ArrayList;

public interface ProfileFragmentBase extends FragmentBase {
    void setupRecyclerView(ArrayList<ProfileItem> groups);
    void setupLayouts(boolean isNetworkAvailable);
}
