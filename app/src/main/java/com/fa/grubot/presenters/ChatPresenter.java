package com.fa.grubot.presenters;

import android.content.Context;

import com.fa.grubot.App;
import com.fa.grubot.abstractions.ChatFragmentBase;
import com.fa.grubot.abstractions.MessagesListRequestResponse;
import com.fa.grubot.models.ChatModel;
import com.fa.grubot.objects.chat.ChatMessage;
import com.fa.grubot.util.FragmentState;

import java.util.ArrayList;

public class ChatPresenter implements MessagesListRequestResponse {

    private ChatFragmentBase fragment;
    private ChatModel model;
    private Context context;

    private String chatId;
    private ChatPresenter presenter = this;

    private ArrayList<ChatMessage> messages = new ArrayList<>();

    public ChatPresenter(ChatFragmentBase fragment, Context context) {
        this.fragment = fragment;
        this.context = context;
        model = new ChatModel();
    }

    public void notifyFragmentStarted(String chatId) {
        this.chatId = chatId;
        if (App.INSTANCE.getCurrentUser().hasTelegramUser())
            model.sendTelegramMessagesRequest(context, presenter, chatId);

        //if (App.INSTANCE.getCurrentUser().hasVkUser())
        //    model.sendVkChatListRequest(this);
    }

    public void onMessagesListResult(ArrayList<ChatMessage> messages, boolean moveToTop) {

    }

    private void notifyViewCreated(int state) {
        fragment.showRequiredViews();

        switch (state) {
            case FragmentState.STATE_CONTENT:
                //fragment.setupRecyclerView(messages);
                fragment.setupToolbar(chatId);
                break;
            case FragmentState.STATE_NO_INTERNET_CONNECTION:
                fragment.setupRetryButton();
                break;
            case FragmentState.STATE_NO_DATA:
                break;
        }
    }

    public void destroy() {
        fragment = null;
        model = null;
    }
}
