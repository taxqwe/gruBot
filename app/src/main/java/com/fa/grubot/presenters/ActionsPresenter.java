package com.fa.grubot.presenters;


import android.content.Context;

import com.fa.grubot.App;
import com.fa.grubot.abstractions.ActionsFragmentBase;
import com.fa.grubot.fragments.ActionsFragment;
import com.fa.grubot.models.ActionsModel;
import com.fa.grubot.objects.dashboard.Action;
import com.fa.grubot.objects.dashboard.ActionAnnouncement;
import com.fa.grubot.objects.group.Group;
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
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
        if (type == ActionsFragment.TYPE_ANNOUNCEMENTS)
            actionsQuery = FirebaseFirestore.getInstance().collection("announcements").whereEqualTo("users." + App.INSTANCE.getCurrentUser().getId(), true);
        else
            actionsQuery = FirebaseFirestore.getInstance().collection("votes").whereEqualTo("users." + App.INSTANCE.getCurrentUser().getId(), true);

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
                    if (type == ActionsFragment.TYPE_ANNOUNCEMENTS) {
                        ActionAnnouncement announcement =
                                new ActionAnnouncement(
                                        doc.get("group").toString(),
                                        (DocumentReference) doc.get("author"),
                                        doc.get("desc").toString(),
                                        new Date(),
                                        //(Date) doc.get("date"), TODO исправить
                                        doc.get("text").toString(),
                                        (Map<String, Boolean>) doc.get("users"));
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
                    if (type == ActionsFragment.TYPE_ANNOUNCEMENTS) {
                        ActionAnnouncement announcement =
                                new ActionAnnouncement(
                                        doc.get("group").toString(),
                                        (DocumentReference) doc.get("author"),
                                        doc.get("desc").toString(),
                                        new Date(),
                                        //(Date) doc.get("date"), TODO исправить
                                        doc.get("text").toString(),
                                        (Map<String, Boolean>) doc.get("users"));
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

    public void onRetryBtnClick(int type) {
        setupConnection(type);
        setRegistration(type);
    }

    public void destroy(){
        fragment = null;
        model = null;
    }
}
