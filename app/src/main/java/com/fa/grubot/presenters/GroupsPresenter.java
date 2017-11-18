package com.fa.grubot.presenters;


import android.content.Context;

import com.fa.grubot.R;
import com.fa.grubot.abstractions.GroupsFragmentBase;
import com.fa.grubot.models.GroupsModel;
import com.fa.grubot.objects.group.Group;
import com.fa.grubot.util.Globals;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GroupsPresenter {
    private GroupsFragmentBase fragment;
    private GroupsModel model;

    private ArrayList<Group> groups = new ArrayList<>();


    public GroupsPresenter(GroupsFragmentBase fragment){
        this.fragment = fragment;
        this.model = new GroupsModel();
    }

    public void notifyViewCreated(int state){
        fragment.showRequiredViews();
        switch (state) {
            case Globals.FragmentState.STATE_CONTENT:
                fragment.setupToolbar();
                fragment.setupRecyclerView(groups);
                fragment.setupSwipeRefreshLayout(state);
                break;
            case Globals.FragmentState.STATE_NO_INTERNET_CONNECTION:
                fragment.setupRetryButton();
                break;
            case Globals.FragmentState.STATE_NO_DATA:
                fragment.setupSwipeRefreshLayout(state);
                break;
        }
    }

    public void updateView(int layout, Context context){
        groups = model.loadGroups();
        if (model.isNetworkAvailable(context)) {
            if (layout == R.layout.fragment_actions && groups.size() > 0)
                updateDashboardRecyclerView(groups);
            else
                fragment.reloadFragment();
        } else
            fragment.reloadFragment();
    }

    private void updateDashboardRecyclerView(ArrayList<Group> entries){
        fragment.setupRecyclerView(groups);
    }

    public void notifyFragmentStarted(Context context){
        if (model.isNetworkAvailable(context))
            getData(true);
        else
            fragment.setupLayouts(false, false);
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

    public void onRetryBtnClick(Context context) {
        if (model.isNetworkAvailable(context)) {
            getData(false);
        }
    }

    public void destroy(){
        fragment = null;
        model = null;
    }
}
