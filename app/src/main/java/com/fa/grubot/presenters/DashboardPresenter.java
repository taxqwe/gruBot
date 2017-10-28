package com.fa.grubot.presenters;


import android.content.Context;
import android.view.View;

import com.fa.grubot.R;
import com.fa.grubot.abstractions.DashboardFragmentBase;
import com.fa.grubot.models.DashboardModel;
import com.fa.grubot.objects.DashboardEntry;

import java.util.ArrayList;

public class DashboardPresenter {
    private DashboardFragmentBase fragment;
    private DashboardModel model;
    private ArrayList<DashboardEntry> entries = new ArrayList<>();

    public DashboardPresenter(DashboardFragmentBase fragment){
        this.fragment = fragment;
        this.model = new DashboardModel();
    }

    public void notifyViewCreated(int layout, View v){
        fragment.setupViews(layout, v);

        switch (layout) {
            case R.layout.fragment_dashboard:
                fragment.setupRecyclerView(entries);
                fragment.setupSwipeRefreshLayout(layout);
                break;
            case R.layout.fragment_no_internet_connection:
                fragment.setupRetryButton();
                break;
            case R.layout.fragment_no_data:
                fragment.setupSwipeRefreshLayout(layout);
                break;
        }
    }

    public void updateView(int layout, Context context){
        entries = model.loadDashboard();
        if (model.isNetworkAvailable(context)) {
            if (layout == R.layout.fragment_dashboard && entries.size() > 0)
                updateDashboardRecyclerView(entries);
            else
                fragment.reloadFragment();
        } else
            fragment.reloadFragment();
    }

    private void updateDashboardRecyclerView(ArrayList<DashboardEntry> entries){
        fragment.setupRecyclerView(entries);
    }

    public void notifyFragmentStarted(Context context){
        boolean isNetworkAvailable = model.isNetworkAvailable(context);
        boolean isHasData = false;
        if (isNetworkAvailable)
            entries = model.loadDashboard();

        if (entries.size() > 0)
            isHasData = true;

        fragment.setupLayouts(isNetworkAvailable, isHasData);
    }

    public void onRetryBtnClick(){
        fragment.reloadFragment();
    }

    public void destroy(){
        fragment = null;
        model = null;
    }
}
