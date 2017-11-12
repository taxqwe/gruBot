package com.fa.grubot.presenters;


import android.content.Context;
import android.view.View;

import com.fa.grubot.R;
import com.fa.grubot.abstractions.ActionsFragmentBase;
import com.fa.grubot.abstractions.ProfileFragmentBase;
import com.fa.grubot.models.ActionsModel;
import com.fa.grubot.objects.dashboard.Action;

import java.util.ArrayList;

public class ProfilePresenter {
    private ProfileFragmentBase fragment;
    private ActionsModel model;

    public ProfilePresenter(ProfileFragmentBase fragment){
        this.fragment = fragment;
        this.model = new ActionsModel();
    }

    public void notifyViewCreated(int layout, View v){
        switch (layout) {
            case R.layout.fragment_profile:
                fragment.setupToolbar();
                fragment.setupViews();
                break;
            case R.layout.fragment_no_internet_connection:
                fragment.setupRetryButton();
                break;
        }
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
