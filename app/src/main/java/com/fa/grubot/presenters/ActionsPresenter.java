package com.fa.grubot.presenters;


import android.content.Context;

import com.fa.grubot.R;
import com.fa.grubot.abstractions.ActionsFragmentBase;
import com.fa.grubot.models.ActionsModel;
import com.fa.grubot.objects.dashboard.Action;
import com.fa.grubot.util.Globals;

import java.util.ArrayList;

public class ActionsPresenter {
    private ActionsFragmentBase fragment;
    private ActionsModel model;
    private ArrayList<Action> actions = new ArrayList<>();

    public ActionsPresenter(ActionsFragmentBase fragment){
        this.fragment = fragment;
        this.model = new ActionsModel();
    }

    public void notifyViewCreated(int state) {
        fragment.setupViews();

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
