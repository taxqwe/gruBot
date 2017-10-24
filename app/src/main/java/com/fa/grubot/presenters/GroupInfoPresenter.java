package com.fa.grubot.presenters;


import com.fa.grubot.abstractions.GroupInfoActivityBase;
import com.fa.grubot.models.GroupInfoModel;

public class GroupInfoPresenter {
    private GroupInfoActivityBase activity;
    private GroupInfoModel model;

    public GroupInfoPresenter(GroupInfoActivityBase activity){
        this.activity = activity;
        this.model = new GroupInfoModel();
    }

    public void notifyViewCreated(){
        activity.setupToolbar();
        activity.setupFab();
        activity.setupRecyclerView(model.loadButtons());
    }

    public void destroy(){
        activity = null;
        model = null;
    }
}
