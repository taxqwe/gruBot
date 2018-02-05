package com.fa.grubot.presenters;


import com.fa.grubot.App;
import com.fa.grubot.abstractions.GroupsFragmentBase;
import com.fa.grubot.models.GroupsModel;
import com.fa.grubot.objects.group.Group;
import com.fa.grubot.util.Globals;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Map;

public class GroupsPresenter {

    private GroupsFragmentBase fragment;
    private GroupsModel model;

    private ArrayList<Group> groups = new ArrayList<>();

    private Query groupsQuery = FirebaseFirestore.getInstance().collection("groups").whereEqualTo("users." + App.INSTANCE.getCurrentUser().getId(), true);
    private ListenerRegistration groupsRegistration;

    public GroupsPresenter(GroupsFragmentBase fragment) {
        this.fragment = fragment;
        this.model = new GroupsModel();
    }

    public void notifyFragmentStarted() {
        fragment.setupToolbar();
        setRegistration();
    }

    private void notifyViewCreated(int state) {
        fragment.showRequiredViews();

        switch (state) {
            case Globals.FragmentState.STATE_CONTENT:
                fragment.setupRecyclerView(groups);
                break;
            case Globals.FragmentState.STATE_NO_INTERNET_CONNECTION:
                fragment.setupRetryButton();
                break;
            case Globals.FragmentState.STATE_NO_DATA:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    public void setRegistration() {
        groupsRegistration = groupsQuery.addSnapshotListener((documentSnapshots, e) -> {
            if (e == null) {
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot doc = dc.getDocument();
                    Group group = new Group(doc.getId(), doc.get("name").toString(), (Map<String, Boolean>) doc.get("users"), doc.get("imgUrl").toString());

                    if (fragment != null) {
                        if (!fragment.isAdapterExists() && fragment.isListEmpty()) {
                            fragment.setupLayouts(true, true);
                            notifyViewCreated(Globals.FragmentState.STATE_CONTENT);
                        }

                        fragment.handleListUpdate(dc.getType(), dc.getNewIndex(), dc.getOldIndex(), group);
                    }
                }

                if (fragment != null && fragment.isListEmpty()) {
                    fragment.setupLayouts(true, false);
                    notifyViewCreated(Globals.FragmentState.STATE_NO_DATA);
                }
            } else {
                if (fragment != null) {
                    fragment.setupLayouts(false, false);
                    notifyViewCreated(Globals.FragmentState.STATE_NO_INTERNET_CONNECTION);
                }
            }
        });
    }

    public void onRetryBtnClick() {
        setRegistration();
    }

    public void removeRegistration() {
        if (groupsRegistration != null)
            groupsRegistration.remove();
    }

    public void destroy() {
        removeRegistration();
        fragment = null;
        model = null;
    }
}
