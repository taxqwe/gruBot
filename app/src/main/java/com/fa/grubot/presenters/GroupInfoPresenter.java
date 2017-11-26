package com.fa.grubot.presenters;


import android.content.Context;
import android.util.Log;

import com.fa.grubot.abstractions.GroupInfoFragmentBase;
import com.fa.grubot.adapters.GroupInfoRecyclerAdapter;
import com.fa.grubot.models.GroupInfoModel;
import com.fa.grubot.objects.dashboard.Action;
import com.fa.grubot.objects.dashboard.ActionAnnouncement;
import com.fa.grubot.objects.group.Group;
import com.fa.grubot.objects.group.GroupInfoButton;
import com.fa.grubot.objects.group.User;
import com.fa.grubot.util.Globals;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Date;

public class GroupInfoPresenter {
    private GroupInfoFragmentBase fragment;
    private GroupInfoModel model;
    private ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> buttons = new ArrayList<>();

    private String groupId;
    private Group group;

    private DocumentReference documentReference;
    private ListenerRegistration registration;

    public GroupInfoPresenter(GroupInfoFragmentBase fragment) {
        this.fragment = fragment;
        this.model = new GroupInfoModel();
    }

    public void notifyFragmentStarted(String groupId) {
        documentReference = FirebaseFirestore.getInstance().document("groups/" + groupId + "/");
        setupConnection();
        setRegistration();
    }

    private void notifyViewCreated(int state){
        fragment.showRequiredViews();

        switch (state) {
            case Globals.FragmentState.STATE_CONTENT:
                fragment.setupToolbar();
                fragment.setupFab();
                fragment.setupRecyclerView(buttons);
                break;
            case Globals.FragmentState.STATE_NO_INTERNET_CONNECTION:
                fragment.setupRetryButton();
                break;
        }
    }

    private void setupConnection() {
        buttons.clear();
        setGroup();
    }

    @SuppressWarnings("unchecked")
    private void setGroup() {
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                group = new Group(doc.getId(), doc.get("name").toString(), (ArrayList<DocumentReference>) doc.get("users"), doc.get("imgUrl").toString());

                Log.e("mytag", group.getId());
                for (DocumentReference ref : (ArrayList<DocumentReference>) doc.get("users"))
                    Log.e("mytag", ref.getPath());

                buttons.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(new GroupInfoButton(1, "Чат", new ArrayList<>())));
                buttons.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(new GroupInfoButton(2, "Ветки осбуждений", new ArrayList<>())));
                setAnnouncements();
            } else {
                fragment.setupLayouts(false);
                notifyViewCreated(Globals.FragmentState.STATE_NO_INTERNET_CONNECTION);
            }
        });
    }

    private void setAnnouncements() {
        ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> items = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("announcements").whereEqualTo("group", documentReference).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    ActionAnnouncement announcement = new ActionAnnouncement(doc.getId(),
                                                                            (DocumentReference) doc.get("group"),
                                                                            (DocumentReference) doc.get("author"),
                                                                            doc.get("desc").toString(),
                                                                            (Date) doc.get("date"),
                                                                            doc.get("text").toString());

                    announcement = (ActionAnnouncement) setDataForAction(announcement);
                    items.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(announcement));
                }
                buttons.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(new GroupInfoButton(4, "Голосования", new ArrayList<>())));
                setUsers();
            } else {
                fragment.setupLayouts(false);
                notifyViewCreated(Globals.FragmentState.STATE_NO_INTERNET_CONNECTION);
            }
        });
        buttons.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(new GroupInfoButton(3, "Объявления", items)));
    }

    private Action setDataForAction(Action action) {
        action.setGroupName(group.getName());
        action.getAuthor().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                User user = new User(doc.getId(),
                                    doc.get("username").toString(),
                                    doc.get("fullname").toString(),
                                    doc.get("phoneNumber").toString(),
                                    doc.get("desc").toString(),
                                    doc.get("imgUrl").toString());
                action.setAuthorName(user.getFullname());
            } else {
                fragment.setupLayouts(false);
                notifyViewCreated(Globals.FragmentState.STATE_NO_INTERNET_CONNECTION);
            }
        });
        return action;
    }

    private void setUsers() {
        ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> items = new ArrayList<>();
        for (DocumentReference ref : group.getUsers()) {
            ref.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    User user = new User(doc.getId(),
                            doc.get("username").toString(),
                            doc.get("fullname").toString(),
                            doc.get("phoneNumber").toString(),
                            doc.get("desc").toString(),
                            doc.get("imgUrl").toString());
                    items.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(user));
                } else {
                    fragment.setupLayouts(false);
                    notifyViewCreated(Globals.FragmentState.STATE_NO_INTERNET_CONNECTION);
                }
            });
        }
        buttons.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(new GroupInfoButton(5, "Список участников", items)));
        fragment.setupLayouts(true);
        notifyViewCreated(Globals.FragmentState.STATE_CONTENT);
    }

    @SuppressWarnings("unchecked")
    private void setRegistration() {
        /*registration = collectionReference.addSnapshotListener((documentSnapshots, e) -> {
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
        });*/
    }

    public void onRetryBtnClick(Context context, Group group) {

    }

    public void destroy(){
        fragment = null;
        model = null;
    }
}
