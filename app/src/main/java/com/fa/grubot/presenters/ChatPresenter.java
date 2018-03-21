package com.fa.grubot.presenters;

import android.content.Context;

import com.fa.grubot.App;
import com.fa.grubot.abstractions.ChatFragmentBase;
import com.fa.grubot.models.ChatModel;

public class ChatPresenter {

    private ChatFragmentBase fragment;
    private ChatModel model;
    private Context context;

    private ChatPresenter presenter = this;


    public ChatPresenter(ChatFragmentBase fragment, Context context) {
        this.fragment = fragment;
        this.context = context;
        model = new ChatModel();
    }

    public void notifyFragmentStarted() {
        fragment.setupToolbar("");
        //if (App.INSTANCE.getCurrentUser().hasTelegramUser())
            //model.sendChatsListRequest(context, presenter);
    }

    public void destroy() {
        fragment = null;
        model = null;
    }
}
