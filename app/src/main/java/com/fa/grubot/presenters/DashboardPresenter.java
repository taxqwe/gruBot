package com.fa.grubot.presenters;


import com.fa.grubot.fragments.DashboardFragment;
import com.fa.grubot.models.DashboardModel;

public class DashboardPresenter {
    private DashboardFragment fragment;
    private DashboardModel model;

    public DashboardPresenter(DashboardFragment fragment){
        this.fragment = fragment;
        this.model = new DashboardModel();
    }

    public void notifyViewCreated(){
        fragment.setupRecyclerView(model.loadDashboard());
    }

    public void updateDashboardRecyclerView(){
        fragment.setupRecyclerView(model.loadDashboard());
    }

    public void destroy(){
        fragment = null;
        model = null;
    }
}
