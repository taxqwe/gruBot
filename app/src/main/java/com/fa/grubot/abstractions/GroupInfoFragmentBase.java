package com.fa.grubot.abstractions;

import com.fa.grubot.objects.dashboard.Action;
import com.google.firebase.firestore.DocumentChange;

public interface GroupInfoFragmentBase extends FragmentBase {
    void setupFab();
    void setupButtonClickListeners();
    void setupRecyclerView(String dataType);
    void handleDataUpdate(String dataType, DocumentChange.Type type, int newIndex, int oldIndex, Action action);
    void setupLayouts(boolean isNetworkAvailable);
    boolean isOneOfTheAdaptersExists();
}
