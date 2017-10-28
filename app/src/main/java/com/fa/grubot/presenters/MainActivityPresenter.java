package com.fa.grubot.presenters;


import com.fa.grubot.abstractions.MainActivityBase;
import com.fa.grubot.models.MainActivityModel;

public class MainActivityPresenter {
    private MainActivityBase activity;
    private MainActivityModel model;

    public MainActivityPresenter(MainActivityBase activity){
        this.activity = activity;
        this.model = new MainActivityModel();
    }

    public void notifyViewCreated(){
        activity.setupViews();
        activity.setupDrawerContent();
        activity.setDefaultFragment();
    }

    public void destroy(){
        activity = null;
        model = null;
    }
}
