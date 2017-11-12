package com.fa.grubot.presenters;


import android.content.Context;
import android.view.View;

import com.fa.grubot.R;
import com.fa.grubot.abstractions.ProfileFragmentBase;
import com.fa.grubot.models.ProfileModel;
import com.fa.grubot.objects.group.User;
import com.fa.grubot.objects.misc.ProfileItem;

import java.util.ArrayList;

public class ProfilePresenter {
    private ProfileFragmentBase fragment;
    private ProfileModel model;

    private ArrayList<ProfileItem> items = new ArrayList<>();

    public ProfilePresenter(ProfileFragmentBase fragment){
        this.fragment = fragment;
        this.model = new ProfileModel();
    }

    public void notifyViewCreated(int layout, View v){
        switch (layout) {
            case R.layout.fragment_profile:
                fragment.setupToolbar();
                fragment.setupRecyclerView(items);
                break;
            case R.layout.fragment_no_internet_connection:
                fragment.setupRetryButton();
                break;
        }
    }

    public void notifyFragmentStarted(Context context, User user){
        boolean isNetworkAvailable = model.isNetworkAvailable(context);
        if (isNetworkAvailable)
            items = model.getItems(user);

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
