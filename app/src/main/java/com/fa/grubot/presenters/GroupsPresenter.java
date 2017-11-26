package com.fa.grubot.presenters;


import com.fa.grubot.abstractions.GroupsFragmentBase;
import com.fa.grubot.models.GroupsModel;
import com.fa.grubot.objects.group.Group;
import com.fa.grubot.util.Globals;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

public class GroupsPresenter {

    private GroupsFragmentBase fragment;
    private GroupsModel model;

    private ArrayList<Group> groups = new ArrayList<>();

    private CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("groups");
    private ListenerRegistration registration;

    public GroupsPresenter(GroupsFragmentBase fragment){
        this.fragment = fragment;
        this.model = new GroupsModel();
    }

    private void notifyViewCreated(int state){
        fragment.showRequiredViews();

        switch (state) {
            case Globals.FragmentState.STATE_CONTENT:
                fragment.setupToolbar();
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
    private void setupConnection() {
        collectionReference.get().addOnCompleteListener(task -> {
            groups.clear();
            if (task.isSuccessful()) {
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    Group group = new Group(doc.getId(), doc.get("name").toString(), (ArrayList<String>) doc.get("users"), doc.get("imgUrl").toString());
                    groups.add(group);
                }

                if (groups.isEmpty()) {
                    fragment.setupLayouts(true, false);
                    notifyViewCreated(Globals.FragmentState.STATE_NO_DATA);
                } else {
                    fragment.setupLayouts(true, true);
                    notifyViewCreated(Globals.FragmentState.STATE_CONTENT);
                }
            } else {
                fragment.setupLayouts(false, false);
                notifyViewCreated(Globals.FragmentState.STATE_NO_INTERNET_CONNECTION);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void setRegistration() {
        registration = collectionReference.addSnapshotListener((documentSnapshots, e) -> {
            if (e == null) {
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot doc = dc.getDocument();
                    Group group = new Group(doc.getId(), doc.get("name").toString(), (ArrayList<String>) doc.get("users"), doc.get("imgUrl").toString());

                    fragment.handleListUpdate(dc.getType(), dc.getNewIndex(), dc.getOldIndex(), group);
                }
            } else {
                fragment.setupLayouts(false, false);
                notifyViewCreated(Globals.FragmentState.STATE_NO_INTERNET_CONNECTION);
            }
        });
    }

    public void removeRegistration() {
        registration.remove();
    }

    public void resumeRegistration() {
        setRegistration();
    }

    public void notifyFragmentStarted(){
        setupConnection();
        setRegistration();
    }

    public void onRetryBtnClick() {
        setupConnection();
        setRegistration();
    }

    public void destroy() {
        registration.remove();
        fragment = null;
        model = null;
    }
}
