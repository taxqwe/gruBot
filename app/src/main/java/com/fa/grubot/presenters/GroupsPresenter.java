package com.fa.grubot.presenters;


import com.fa.grubot.fragments.GroupsFragment;
import com.fa.grubot.models.GroupsModel;

public class GroupsPresenter {
    private GroupsFragment fragment;
    private GroupsModel model;

    public GroupsPresenter(GroupsFragment fragment){
        this.fragment = fragment;
        this.model = new GroupsModel();
    }

    public void notifyViewCreated(){
        fragment.setupRecyclerView(model.loadGroups());
    }

    public void updateGroupsRecyclerView(){
        fragment.setupRecyclerView(model.loadGroups());
    }

    public void destroy(){
        fragment = null;
        model = null;
    }
}
