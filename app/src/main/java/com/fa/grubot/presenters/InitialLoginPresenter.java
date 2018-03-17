package com.fa.grubot.presenters;


import com.fa.grubot.abstractions.InitialLoginFragmentBase;

public class InitialLoginPresenter {
    private InitialLoginFragmentBase fragment;

    public InitialLoginPresenter(InitialLoginFragmentBase fragment){
        this.fragment = fragment;
    }

    public void notifyFragmentStarted() {
        fragment.setupViews();
    }

    public void destroy() {
        fragment = null;
    }
}
