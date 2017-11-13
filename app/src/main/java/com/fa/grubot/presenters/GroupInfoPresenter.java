package com.fa.grubot.presenters;


import android.content.Context;
import android.view.View;

import com.fa.grubot.R;
import com.fa.grubot.abstractions.GroupInfoFragmentBase;
import com.fa.grubot.adapters.GroupInfoRecyclerAdapter;
import com.fa.grubot.models.GroupInfoModel;
import com.fa.grubot.objects.group.Group;

import java.util.ArrayList;

public class GroupInfoPresenter {
    private GroupInfoFragmentBase fragment;
    private GroupInfoModel model;
    private ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> actions = new ArrayList<>();

    public GroupInfoPresenter(GroupInfoFragmentBase fragment){
        this.fragment = fragment;
        this.model = new GroupInfoModel();
    }

    public void notifyViewCreated(int layout, View v){
        switch (layout) {
            case R.layout.fragment_group_info:
                fragment.setupToolbar();
                fragment.setupFab();
                fragment.setupRecyclerView(actions);
                break;
            case R.layout.content_no_internet_connection:
                fragment.setupRetryButton();
                break;
        }
    }

    public void notifyFragmentStarted(Context context, Group group){
        boolean isNetworkAvailable = model.isNetworkAvailable(context);
        if (isNetworkAvailable) {
            actions = model.loadButtons(group);
        }

        fragment.setupLayouts(isNetworkAvailable);
    }

    public void onRetryBtnClick(){
        fragment.reloadFragment();
    }

    public void destroy(){
        fragment = null;
        model = null;
    }
}
