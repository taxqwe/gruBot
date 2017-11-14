package com.fa.grubot.presenters;


import android.content.Context;

import com.fa.grubot.abstractions.DashboardFragmentBase;
import com.fa.grubot.models.DashboardModel;
import com.fa.grubot.util.Globals;

public class DashboardPresenter {
    private DashboardFragmentBase fragment;
    private DashboardModel model;

    public DashboardPresenter(DashboardFragmentBase fragment){
        this.fragment = fragment;
        this.model = new DashboardModel();
    }

    public void notifyViewCreated(int state) {
        fragment.setupViews();

        switch (state) {
            case Globals.FragmentState.STATE_CONTENT:
                fragment.setupToolbar();
                fragment.setupRecyclerView(model.getItems());
                break;
            case Globals.FragmentState.STATE_NO_INTERNET_CONNECTION:
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
