package com.fa.grubot.presenters;


import com.fa.grubot.abstractions.TelegramVerificationFragmentBase;

public class TelegramVerificationPresenter {
    private TelegramVerificationFragmentBase fragment;

    public TelegramVerificationPresenter(TelegramVerificationFragmentBase fragment){
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
