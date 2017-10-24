package com.fa.grubot.presenters;


import com.fa.grubot.abstractions.DashboardFragmentBase;
import com.fa.grubot.models.DashboardModel;

public class DashboardPresenter {
    private DashboardFragmentBase fragment;
    private DashboardModel model;

    public DashboardPresenter(DashboardFragmentBase fragment){
        this.fragment = fragment;
        this.model = new DashboardModel();
    }

    public void notifyViewCreated(){
        fragment.setupRecyclerView(model.loadDashboard());
        fragment.setupSwipeRefreshLayout();
    }

    public void updateDashboardRecyclerView(){
        fragment.setupRecyclerView(model.loadDashboard());
    }

    public void destroy(){
        fragment = null;
        model = null;
    }
}
