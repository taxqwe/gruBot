package com.fa.grubot.presenters;


import android.content.Context;
import android.util.Log;

import com.fa.grubot.abstractions.GroupsFragmentBase;
import com.fa.grubot.models.GroupsModel;
import com.fa.grubot.objects.group.Group;
import com.fa.grubot.util.Globals;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GroupsPresenter {

    private GroupsFragmentBase fragment;
    private GroupsModel model;

    private ArrayList<Group> groups = new ArrayList<>();

    private CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("groups");
    private EventListener eventListener;

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
                fragment.setupSwipeRefreshLayout();
                break;
            case Globals.FragmentState.STATE_NO_INTERNET_CONNECTION:
                fragment.setupRetryButton();
                break;
            case Globals.FragmentState.STATE_NO_DATA:
                fragment.setupSwipeRefreshLayout();
                break;
        }
    }

    private void setupConnection() {
        collectionReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                groups.clear();
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    Group group = new Group(doc.getId(), doc.get("name").toString(), (ArrayList<String>) doc.get("users"), doc.get("imgUrl").toString());
                    groups.add(group);
                    Log.e("mytag", group.getName());
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

        new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e == null) {
                    for (DocumentChange dc : documentSnapshots.getDocumentChanges()){
                        switch (dc.getType()) {
                            case ADDED:

                                break;
                            case MODIFIED:

                                break;
                            case REMOVED:

                                break;
                        }
                    }
                }
            }
        };

        collectionReference.addSnapshotListener(eventListener);
        collectionReference.
    }

    public void notifyFragmentStarted(Context context){
        setupConnection();
        /*if (model.isNetworkAvailable(context))
            getData(true);
        else
            fragment.setupLayouts(false, false);*/
    }

    private void getData(final boolean isFirst) {
        Observable.defer(() -> Observable.just(model.loadGroups()))
                .filter(result -> result != null)
                .subscribeOn(Schedulers.io())
                .timeout(15, TimeUnit.SECONDS)
                .doOnSubscribe(d -> {
                    if (!isFirst)
                        fragment.showLoadingView();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(result -> {
                    groups = result;
                    if (groups.isEmpty())
                        fragment.setupLayouts(true, false);
                    else
                        fragment.setupLayouts(true, true);
                    notifyViewCreated(Globals.FragmentState.STATE_CONTENT);
                })
                .doOnError(error -> {
                    fragment.setupLayouts(false, false);
                    notifyViewCreated(Globals.FragmentState.STATE_NO_INTERNET_CONNECTION);
                })
                .subscribe();
    }

    public void onRefresh(Context context) {
        if (model.isNetworkAvailable(context)) {
            getData(false);
        } else {
            fragment.setupLayouts(false, false);
            notifyViewCreated(Globals.FragmentState.STATE_NO_INTERNET_CONNECTION);
        }
    }

    public void onRetryBtnClick(Context context) {
        if (model.isNetworkAvailable(context)) {
            getData(false);
        }
    }

    public void destroy() {
        fragment = null;
        model = null;
    }
}
