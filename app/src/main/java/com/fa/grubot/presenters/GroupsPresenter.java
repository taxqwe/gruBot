package com.fa.grubot.presenters;


import com.fa.grubot.fragments.GroupsFragment;
import com.fa.grubot.models.GroupsModel;
import com.fa.grubot.objects.Group;

import java.util.ArrayList;

public class GroupsPresenter {
    private GroupsFragment fragment;
    private GroupsModel model;

    public GroupsPresenter(GroupsFragment fragment){
        this.fragment = fragment;
        this.model = new GroupsModel();
    }

    public ArrayList<Group> getGroups(){
        ArrayList<Group> groups = model.loadGroups();
        //какая-то логика с groups
        return groups;
    }
}
