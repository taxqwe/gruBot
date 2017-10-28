package com.fa.grubot.presenters;


import android.os.Bundle;

import com.fa.grubot.abstractions.MainActivityBase;
import com.fa.grubot.models.MainActivityModel;

public class MainActivityPresenter {
    private MainActivityBase activity;
    private MainActivityModel model;

    public MainActivityPresenter(MainActivityBase activity){
        this.activity = activity;
        this.model = new MainActivityModel();
    }

    public void notifyViewCreated(Bundle savedInstanceState){
        activity.setupViews();
        activity.setupDrawerContent();
        if (savedInstanceState == null)
            activity.setDefaultFragment();
    }

    public void destroy(){
        activity = null;
        model = null;
    }
}
