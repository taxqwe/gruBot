package com.fa.grubot.presenters;


import com.fa.grubot.abstractions.GroupsFragmentBase;
import com.fa.grubot.models.GroupsModel;

public class GroupsPresenter {
    private GroupsFragmentBase fragment;
    private GroupsModel model;

    public GroupsPresenter(GroupsFragmentBase fragment){
        this.fragment = fragment;
        this.model = new GroupsModel();
    }

    public void notifyViewCreated(){
        fragment.setupRecyclerView(model.loadGroups());
        fragment.setupSwipeRefreshLayout();
    }

    public void updateGroupsRecyclerView(){
        fragment.setupRecyclerView(model.loadGroups());
    }

    public void destroy(){
        fragment = null;
        model = null;
    }
}
