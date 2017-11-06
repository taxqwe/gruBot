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
        switch (layout) {
            case R.layout.fragment_dashboard:
                fragment.setupViews();
                break;
            case R.layout.fragment_no_internet_connection:
                fragment.setupRetryButton();
                break;
        }
    }

    public void notifyFragmentStarted(Context context){
        boolean isNetworkAvailable = model.isNetworkAvailable(context);
        /*if (isNetworkAvailable)
            entries = model.loadDashboard();


        */
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
