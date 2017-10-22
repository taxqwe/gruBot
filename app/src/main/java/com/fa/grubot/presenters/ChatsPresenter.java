package com.fa.grubot.presenters;

import com.fa.grubot.abstractions.FragmentBase;
import com.fa.grubot.fragments.ChatFragment;
import com.fa.grubot.models.ChatsModel;

/**
 * Created by ni.petrov on 22/10/2017.
 */

public class ChatsPresenter {

    FragmentBase chatFragment;
    ChatsModel model;


    public ChatsPresenter(FragmentBase chatFragment) {
        this.chatFragment = chatFragment;
        model = new ChatsModel();
    }


    public void notifyViewCreated(){
        chatFragment.setupRecyclerView(model.getMessagesOfChatById(1488));
    }

    public void destroy(){
        chatFragment = null;
        model = null;
    }
}
