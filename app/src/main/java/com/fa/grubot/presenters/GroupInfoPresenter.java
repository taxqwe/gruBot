package com.fa.grubot.presenters;


import android.content.Context;

import com.fa.grubot.abstractions.GroupInfoFragmentBase;
import com.fa.grubot.adapters.GroupInfoRecyclerAdapter;
import com.fa.grubot.models.GroupInfoModel;
import com.fa.grubot.objects.dashboard.ActionAnnouncement;
import com.fa.grubot.objects.dashboard.ActionVote;
import com.fa.grubot.objects.group.Group;
import com.fa.grubot.objects.group.GroupInfoButton;
import com.fa.grubot.objects.group.User;
import com.fa.grubot.objects.misc.VoteOption;
import com.fa.grubot.util.Globals;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class GroupInfoPresenter {
    private GroupInfoFragmentBase fragment;
    private GroupInfoModel model;
    private ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> buttons = new ArrayList<>();

    private Group group;

    private transient DocumentReference groupReference;
    private transient ListenerRegistration registration;
    private Query usersQuery;
    private Query announcementsQuery;
    private Query votesQuery;

    public GroupInfoPresenter(GroupInfoFragmentBase fragment) {
        this.fragment = fragment;
        this.model = new GroupInfoModel();
    }

    public void notifyFragmentStarted(String groupId) {
        groupReference = FirebaseFirestore.getInstance().collection("groups").document(groupId);
        usersQuery = FirebaseFirestore.getInstance().collection("users").whereEqualTo("groups." + groupId, true);
        announcementsQuery = FirebaseFirestore.getInstance().collection("announcements").whereEqualTo("group", groupId);
        votesQuery = FirebaseFirestore.getInstance().collection("votes").whereEqualTo("group", groupId);

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
        groupReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                group = new Group(doc.getId(), doc.get("name").toString(), (Map<String, Boolean>) doc.get("users"), doc.get("imgUrl").toString());

                buttons.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(new GroupInfoButton(1, "Чат", new ArrayList<>())));
                buttons.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(new GroupInfoButton(2, "Ветки осбуждений", new ArrayList<>())));
                setAnnouncements();
            } else {
                fragment.setupLayouts(false);
                notifyViewCreated(Globals.FragmentState.STATE_NO_INTERNET_CONNECTION);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void setAnnouncements() {
        ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> items = new ArrayList<>();
        announcementsQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    ActionAnnouncement announcement =
                            new ActionAnnouncement(
                                    doc.getId(),
                                    doc.get("group").toString(),
                                    doc.get("groupName").toString(),
                                    (DocumentReference) doc.get("author"),
                                    doc.get("authorName").toString(),
                                    doc.get("desc").toString(),
                                    (Date) doc.get("date"),
                                    doc.get("text").toString(),
                                    (Map<String, String>) doc.get("users"));

                    items.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(announcement));
                }
                buttons.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(new GroupInfoButton(3, "Объявления", items)));
                buttons.addAll(items);
                setVotes();
            } else {
                fragment.setupLayouts(false);
                notifyViewCreated(Globals.FragmentState.STATE_NO_INTERNET_CONNECTION);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void setVotes() {
        ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> items = new ArrayList<>();
        votesQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    ArrayList<VoteOption> voteOptions = new ArrayList<>();
                    for (Map.Entry<String, String> option : ((Map<String, String>) doc.get("voteOptions")).entrySet())
                        voteOptions.add(new VoteOption(option.getValue()));

                    ActionVote vote =
                            new ActionVote(
                                    doc.getId(),
                                    doc.get("group").toString(),
                                    doc.get("groupName").toString(),
                                    (DocumentReference) doc.get("author"),
                                    doc.get("authorName").toString(),
                                    doc.get("desc").toString(),
                                    (Date) doc.get("date"),
                                    voteOptions,
                                    (Map<String, String>) doc.get("users"));

                    items.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(vote));
                }
                buttons.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(new GroupInfoButton(4, "Голосования", items)));
                buttons.addAll(items);
                setUsers();
            } else {
                fragment.setupLayouts(false);
                notifyViewCreated(Globals.FragmentState.STATE_NO_INTERNET_CONNECTION);
            }
        });
    }

    private void setUsers() {
        ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> items = new ArrayList<>();
        usersQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    User user = new User(doc.getId(),
                            doc.get("username").toString(),
                            doc.get("fullname").toString(),
                            doc.get("phoneNumber").toString(),
                            doc.get("desc").toString(),
                            doc.get("imgUrl").toString());
                    items.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(user));
                }

                buttons.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(new GroupInfoButton(5, "Участники", items)));
                buttons.addAll(items);

                if (fragment != null) {
                    fragment.setupLayouts(true);
                    notifyViewCreated(Globals.FragmentState.STATE_CONTENT);
                }
            } else {
                if (fragment != null) {
                    fragment.setupLayouts(false);
                    notifyViewCreated(Globals.FragmentState.STATE_NO_INTERNET_CONNECTION);
                }
            }
        });
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
