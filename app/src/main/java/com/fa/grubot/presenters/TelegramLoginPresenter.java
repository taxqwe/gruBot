package com.fa.grubot.presenters;


import com.fa.grubot.abstractions.TelegramLoginFragmentBase;

public class TelegramLoginPresenter {
    private TelegramLoginFragmentBase fragment;

    public TelegramLoginPresenter(TelegramLoginFragmentBase fragment){
        this.fragment = fragment;
    }

    public void notifyFragmentStarted() {
        fragment.setupToolbar();
        fragment.setupViews();
    }

    public void destroy() {
        fragment = null;
    }
}
