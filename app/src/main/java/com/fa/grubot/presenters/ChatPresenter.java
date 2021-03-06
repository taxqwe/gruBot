package com.fa.grubot.presenters;

import com.fa.grubot.abstractions.ChatFragmentBase;
import com.fa.grubot.models.ChatModel;
import com.fa.grubot.objects.chat.ChatMessage;

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


    public void onNotifyViewCreated() {
        //draw cached messages
        chatFragment.drawCachedMessages(model.getCachedMessages());

        chatFragment.subscribeOnNewMessages(model.getMessagesObservable());

        //todo get real id
        chatFragment.setUserId(1);
    }

    public void destroy(){
        chatFragment = null;
        model = null;
    }

    public void sendMessage(ChatMessage message) {
        model.sendMessage(message);
    }

    public void notifySmartFilterStatusChanged(boolean isSmartEnabled) {
    }
}
