package com.fa.grubot.presenters;


import com.fa.grubot.fragments.DashboardFragment;
import com.fa.grubot.models.DashboardModel;
import com.fa.grubot.objects.DashboardEntry;

import java.util.ArrayList;

public class DashboardPresenter {
    private DashboardFragment fragment;
    private DashboardModel model;

    public DashboardPresenter(DashboardFragment fragment){
        this.fragment = fragment;
        this.model = new DashboardModel();
    }

    public ArrayList<DashboardEntry> getGroups(){
        ArrayList<DashboardEntry> entries = model.loadDashboard();
        //какая-то логика с groups
        return entries;
    }
}
