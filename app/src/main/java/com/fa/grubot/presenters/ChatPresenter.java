package com.fa.grubot.presenters;

import com.fa.grubot.abstractions.ChatFragmentBase;
import com.fa.grubot.models.ChatModel;

/**
 * Created by ni.petrov on 22/10/2017.
 */

public class ChatPresenter {

    ChatFragmentBase chatFragment;
    ChatModel model;


    public ChatPresenter(ChatFragmentBase chatFragment) {
        this.chatFragment = chatFragment;
        model = new ChatModel();
    }


    public void notifyViewCreated(){
        chatFragment.setupRecyclerView(model.getMessagesOfChatById(1488));
    }

    public void destroy(){
        chatFragment = null;
        model = null;
    }
}
