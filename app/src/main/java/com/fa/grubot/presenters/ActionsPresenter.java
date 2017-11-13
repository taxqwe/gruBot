package com.fa.grubot.presenters;


import android.content.Context;
import android.view.View;

import com.fa.grubot.R;
import com.fa.grubot.abstractions.ActionsFragmentBase;
import com.fa.grubot.models.ActionsModel;
import com.fa.grubot.objects.dashboard.Action;

import java.util.ArrayList;

public class ActionsPresenter {
    private ActionsFragmentBase fragment;
    private ActionsModel model;
    private ArrayList<Action> actions = new ArrayList<>();

    public ActionsPresenter(ActionsFragmentBase fragment){
        this.fragment = fragment;
        this.model = new ActionsModel();
    }

    public void notifyViewCreated(int layout, View v){
        switch (layout) {
            case R.layout.fragment_actions:
                fragment.setupToolbar();
                fragment.setupRecyclerView(actions);
                fragment.setupSwipeRefreshLayout(layout);
                break;
            case R.layout.content_no_internet_connection:
                fragment.setupRetryButton();
                break;
            case R.layout.content_no_data:
                fragment.setupSwipeRefreshLayout(layout);
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

    public void notifyFragmentStarted(Context context, int type){
        boolean isNetworkAvailable = model.isNetworkAvailable(context);
        boolean isHasData = false;
        if (isNetworkAvailable) {
            actions = model.loadActions(type);
        }

        if (actions.size() > 0)
            isHasData = true;

        fragment.setupLayouts(isNetworkAvailable, isHasData);
    }

    public void onRetryBtnClick(){
        fragment.reloadFragment();
    }

    public void destroy(){
        fragment = null;
        model = null;
    }
}
