package com.fa.grubot.abstractions;

import com.fa.grubot.objects.dashboard.Action;
import com.fa.grubot.objects.group.Group;
import com.google.firebase.firestore.DocumentChange;

import java.util.ArrayList;

public interface ActionsFragmentBase {
    void setupRecyclerView(ArrayList<Action> entries);
    void setupLayouts(boolean isNetworkAvailable, boolean isHasData);
    void setupRetryButton();
    void showRequiredViews();
    void showLoadingView();
    void handleListUpdate(DocumentChange.Type type, int newIndex, int oldIndex, Action action);
}
