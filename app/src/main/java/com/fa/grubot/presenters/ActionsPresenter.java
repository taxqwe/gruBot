package com.fa.grubot.presenters;


import android.content.Context;

import com.fa.grubot.R;
import com.fa.grubot.abstractions.ActionsFragmentBase;
import com.fa.grubot.models.ActionsModel;
import com.fa.grubot.objects.dashboard.Action;
import com.fa.grubot.util.Globals;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ActionsPresenter {
    private ActionsFragmentBase fragment;
    private ActionsModel model;
    private ArrayList<Action> actions = new ArrayList<>();

    public ActionsPresenter(ActionsFragmentBase fragment){
        this.fragment = fragment;
        this.model = new ActionsModel();
    }

    public void notifyViewCreated(int state) {
        fragment.showRequiredViews();

        switch (state) {
            case Globals.FragmentState.STATE_CONTENT:
                fragment.setupToolbar();
                fragment.setupRecyclerView(actions);
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

    public void updateView(int layout, Context context, int type){
        actions = model.loadActions(type);
        if (model.isNetworkAvailable(context)) {
            if (layout == R.layout.fragment_actions && actions.size() > 0)
                updateDashboardRecyclerView(actions);
            else
                fragment.reloadFragment();
        } else
            fragment.reloadFragment();
    }

    private void updateDashboardRecyclerView(ArrayList<Action> entries){
        fragment.setupRecyclerView(entries);
    }

    public void notifyFragmentStarted(Context context, int type) {
        if (model.isNetworkAvailable(context))
            getData(true, type);
        else
            fragment.setupLayouts(false, false);
    }

    private void getData(final boolean isFirst, final int type) {
        Observable.defer(() -> Observable.just(model.loadActions(type)))
                .filter(result -> result != null)
                .subscribeOn(Schedulers.io())
                .timeout(15, TimeUnit.SECONDS)
                .doOnSubscribe(d -> {
                    if (!isFirst)
                        fragment.showLoadingView();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(result -> {
                    actions = result;
                    if (actions.isEmpty())
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

    public void onRetryBtnClick(Context context, int type) {
        if (model.isNetworkAvailable(context)) {
            getData(false, type);
        }
    }

    public void destroy(){
        fragment = null;
        model = null;
    }
}
