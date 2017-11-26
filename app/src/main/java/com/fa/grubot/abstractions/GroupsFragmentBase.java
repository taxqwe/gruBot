package com.fa.grubot.abstractions;

import com.fa.grubot.objects.group.Group;
import com.google.firebase.firestore.DocumentChange;

import java.util.ArrayList;

public interface GroupsFragmentBase extends FragmentBase {
    void setupRecyclerView(ArrayList<Group> groups);
    void handleListUpdate(DocumentChange.Type type, int newIndex, int oldIndex, Group group);
    void setupLayouts(boolean isNetworkAvailable, boolean isHasData);
}
