package com.fa.grubot.presenters;


import android.content.Context;
import android.view.View;

import com.fa.grubot.R;
import com.fa.grubot.abstractions.DashboardFragmentBase;
import com.fa.grubot.models.DashboardModel;

public class DashboardPresenter {
    private DashboardFragmentBase fragment;
    private DashboardModel model;

    public DashboardPresenter(DashboardFragmentBase fragment){
        this.fragment = fragment;
        this.model = new DashboardModel();
    }

    public void notifyViewCreated(int layout, View v){
        fragment.setupViews(layout, v);
        if (layout == R.layout.fragment_dashboard) {
            fragment.setupRecyclerView(model.loadDashboard());
            fragment.setupSwipeRefreshLayout();
        } else {
            fragment.setupRetryButton();
        }
    }

    public void updateDashboardRecyclerView(Context context){
        if (model.isNetworkAvailable(context))
            fragment.setupRecyclerView(model.loadDashboard());
        else
            fragment.reloadFragment();
    }

    public void notifyFragmentStarted(Context context){
        fragment.setupLayouts(model.isNetworkAvailable(context));
    }

    public void onRetryBtnClick(){
        fragment.reloadFragment();
    }

    public void destroy(){
        fragment = null;
        model = null;
    }
}
