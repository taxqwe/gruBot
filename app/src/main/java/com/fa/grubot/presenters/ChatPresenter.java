package com.fa.grubot.presenters;

import com.fa.grubot.abstractions.ChatFragmentBase;
import com.fa.grubot.models.ChatModel;
import com.fa.grubot.objects.chat.ChatMessage;

public class ChatPresenter {

    ChatFragmentBase chatFragment;
    ChatModel model;


    public ChatPresenter(ChatFragmentBase chatFragment) {
        this.chatFragment = chatFragment;
        model = new ChatModel();
    }



}
