package com.fa.grubot.presenters;


import com.fa.grubot.abstractions.GroupInfoActivityBase;
import com.fa.grubot.models.GroupInfoModel;
import com.fa.grubot.objects.group.Group;

public class GroupInfoPresenter {
    private GroupInfoActivityBase activity;
    private GroupInfoModel model;

    public GroupInfoPresenter(GroupInfoActivityBase activity){
        this.activity = activity;
        this.model = new GroupInfoModel();
    }

    public void notifyViewCreated(Group group){
        activity.setupToolbar();
        activity.setupFab();
        activity.setupRecyclerView(model.loadButtons(group));
    }

    public void destroy(){
        activity = null;
        model = null;
    }
}
