package com.fa.grubot.abstractions;

import com.fa.grubot.adapters.GroupInfoRecyclerAdapter;
import com.fa.grubot.objects.group.Group;
import com.google.firebase.firestore.DocumentChange;

import java.util.ArrayList;

public interface GroupInfoFragmentBase extends FragmentBase {
    void setupFab();
    void setupRecyclerView(ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> buttons);
    void handleListUpdate(DocumentChange.Type type, int newIndex, int oldIndex, Group group);
    void setupLayouts(boolean isNetworkAvailable);
}
