package com.fa.grubot.presenters;


import com.fa.grubot.App;
import com.fa.grubot.abstractions.ActionsFragmentBase;
import com.fa.grubot.fragments.ActionsFragment;
import com.fa.grubot.models.ActionsModel;
import com.fa.grubot.objects.dashboard.Action;
import com.fa.grubot.objects.dashboard.ActionAnnouncement;
import com.fa.grubot.util.Globals;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ActionsPresenter {
    private ActionsFragmentBase fragment;
    private ActionsModel model;
    private ArrayList<Action> actions = new ArrayList<>();

    private Query actionsQuery;
    private ListenerRegistration registration;

    public ActionsPresenter(ActionsFragmentBase fragment){
        this.fragment = fragment;
        this.model = new ActionsModel();
    }

    public void notifyFragmentStarted(int type) {
        switch (type) {
            case ActionsFragment.TYPE_ANNOUNCEMENTS:
                actionsQuery = FirebaseFirestore.getInstance().collection("announcements").whereEqualTo("users." + App.INSTANCE.getCurrentUser().getId(), "new");
                break;
            case ActionsFragment.TYPE_ANNOUNCEMENTS_ARCHIVE:
                actionsQuery = FirebaseFirestore.getInstance().collection("announcements").whereEqualTo("users." + App.INSTANCE.getCurrentUser().getId(), "archive");
                break;
            case ActionsFragment.TYPE_VOTES:
                actionsQuery = FirebaseFirestore.getInstance().collection("votes").whereEqualTo("users." + App.INSTANCE.getCurrentUser().getId(), "new");
                break;
            case ActionsFragment.TYPE_VOTES_ARCHIVE:
                actionsQuery = FirebaseFirestore.getInstance().collection("votes").whereEqualTo("users." + App.INSTANCE.getCurrentUser().getId(), "archive");
                break;
        }

        setupConnection(type);
        setRegistration(type);
    }

    private void notifyViewCreated(int state) {
        fragment.showRequiredViews();

        switch (state) {
            case Globals.FragmentState.STATE_CONTENT:
                fragment.setupRecyclerView(actions);
                break;
            case Globals.FragmentState.STATE_NO_INTERNET_CONNECTION:
                fragment.setupRetryButton();
                break;
            case Globals.FragmentState.STATE_NO_DATA:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private void setupConnection(int type) {
        actionsQuery.get().addOnCompleteListener(task -> {
            actions.clear();
            if (task.isSuccessful()) {
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    if (type == ActionsFragment.TYPE_ANNOUNCEMENTS  || type == ActionsFragment.TYPE_ANNOUNCEMENTS_ARCHIVE) {
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

                        actions.add(announcement);
                    } else {
                        //TODO Vote
                    }
                }

                if (fragment != null) {
                    if (actions.isEmpty()) {
                        fragment.setupLayouts(true, false);
                        notifyViewCreated(Globals.FragmentState.STATE_NO_DATA);
                    } else {
                        fragment.setupLayouts(true, true);
                        notifyViewCreated(Globals.FragmentState.STATE_CONTENT);
                    }
                }
            } else {
                if (fragment != null) {
                    fragment.setupLayouts(false, false);
                    notifyViewCreated(Globals.FragmentState.STATE_NO_INTERNET_CONNECTION);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void setRegistration(int type) {
        registration = actionsQuery.addSnapshotListener((documentSnapshots, e) -> {
            if (e == null) {
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot doc = dc.getDocument();
                    if (type == ActionsFragment.TYPE_ANNOUNCEMENTS || type == ActionsFragment.TYPE_ANNOUNCEMENTS_ARCHIVE) {
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

                        if (fragment != null)
                            fragment.handleListUpdate(dc.getType(), dc.getNewIndex(), dc.getOldIndex(), announcement);
                    } else {
                        //TODO Vote
                    }
                }
            } else {
                if (fragment != null) {
                    fragment.setupLayouts(false, false);
                    notifyViewCreated(Globals.FragmentState.STATE_NO_INTERNET_CONNECTION);
                }
            }
        });
    }

    public void addActionToArchive(Action action, int type) {
        if (type == ActionsFragment.TYPE_ANNOUNCEMENTS) {
            addAnnouncementToArchive((ActionAnnouncement) action);
        }
    }

    @SuppressWarnings("unchecked")
    private void addAnnouncementToArchive(ActionAnnouncement announcement) {
        FirebaseFirestore.getInstance().collection("announcements")
                .document(announcement.getId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Map<String, String> users = (Map<String, String>) documentSnapshot.get("users");

                    users.put(App.INSTANCE.getCurrentUser().getId(), "archive");

                    FirebaseFirestore.getInstance().collection("announcements")
                            .document(announcement.getId())
                            .update("users", users)
                            .addOnSuccessListener(aVoid -> {
                                if (fragment != null)
                                    fragment.showArchiveSnackbar(announcement);
                            });
                });
    }

    public void restoreActionFromArchive(Action action, int type) {
        if (type == ActionsFragment.TYPE_ANNOUNCEMENTS) {
            restoreAnnouncementFromArchive((ActionAnnouncement) action);
        }
    }

    @SuppressWarnings("unchecked")
    private void restoreAnnouncementFromArchive(ActionAnnouncement announcement) {
        FirebaseFirestore.getInstance().collection("announcements")
                .document(announcement.getId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Map<String, String> users = (Map<String, String>) documentSnapshot.get("users");

                    users.put(App.INSTANCE.getCurrentUser().getId(), "new");

                    FirebaseFirestore.getInstance().collection("announcements")
                            .document(announcement.getId())
                            .update("users", users);
                });
    }

    public void onRetryBtnClick(int type) {
        setupConnection(type);
        setRegistration(type);
    }

    public void removeRegistration() {
        registration.remove();
    }

    public void destroy() {
        registration.remove();
        fragment = null;
        model = null;
    }
}
