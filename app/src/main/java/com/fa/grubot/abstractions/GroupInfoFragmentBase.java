package com.fa.grubot.abstractions;

import com.fa.grubot.adapters.GroupInfoRecyclerAdapter;
import com.fa.grubot.objects.chat.Chat;
import com.fa.grubot.objects.dashboard.Action;
import com.fa.grubot.objects.group.User;
import com.google.firebase.firestore.DocumentChange;

import java.util.ArrayList;

public interface GroupInfoFragmentBase extends FragmentBase {
    void setupFab();
    void setupRecyclerView(ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> buttons);
    void handleActionsUpdate(DocumentChange.Type type, int newIndex, int oldIndex, Action action);
    void handleUsersUpdate(DocumentChange.Type type, int newIndex, int oldIndex, User user);
    void handleUIUpdate(Chat chat);
    void setupLayouts(boolean isNetworkAvailable);
    boolean isAdapterExists();
}
