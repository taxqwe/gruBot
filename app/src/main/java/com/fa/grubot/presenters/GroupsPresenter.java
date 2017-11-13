package com.fa.grubot.presenters;


import android.content.Context;
import android.view.View;

import com.fa.grubot.R;
import com.fa.grubot.abstractions.GroupsFragmentBase;
import com.fa.grubot.models.GroupsModel;
import com.fa.grubot.objects.group.Group;
import com.fa.grubot.util.Globals;

import java.util.ArrayList;

public class GroupsPresenter {
    private GroupsFragmentBase fragment;
    private GroupsModel model;

    private ArrayList<Group> groups = new ArrayList<>();


    public GroupsPresenter(GroupsFragmentBase fragment){
        this.fragment = fragment;
        this.model = new GroupsModel();
    }

    public void notifyViewCreated(int state){
        fragment.setupViews();
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
        boolean isNetworkAvailable = model.isNetworkAvailable(context);
        boolean isHasData = false;
        if (isNetworkAvailable)
            groups = model.loadGroups();

        if (groups.size() > 0)
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
