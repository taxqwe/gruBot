package com.fa.grubot.presenters;

import android.content.Context;

import com.fa.grubot.App;
import com.fa.grubot.abstractions.ChatFragmentBase;
import com.fa.grubot.abstractions.MessagesListRequestResponse;
import com.fa.grubot.models.ChatModel;
import com.fa.grubot.objects.chat.ChatMessage;
import com.fa.grubot.util.FragmentState;
import com.github.badoualy.telegram.api.TelegramClient;

import java.util.ArrayList;

public class ChatPresenter implements MessagesListRequestResponse {

    private ChatFragmentBase fragment;
    private ChatModel model;
    private Context context;

    private String chatId;
    private ChatPresenter presenter = this;
    private TelegramClient client;

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
        this.messages = messages;

        if (fragment != null) {
            if (messages.isEmpty()) {
                fragment.setupLayouts(true, false);
                notifyViewCreated(FragmentState.STATE_NO_DATA);
                this.messages = messages;
            } else if (!fragment.isAdapterExists() || fragment.isListEmpty()) {
                this.messages = messages;
                fragment.setupLayouts(true, true);
                notifyViewCreated(FragmentState.STATE_CONTENT);
            } else if (fragment.isAdapterExists()) {
                fragment.updateMessagesList(messages, moveToTop);
            }

            //if (client == null || client.isClosed())
                //setUpdateCallback();
        }
    }

    private void notifyViewCreated(int state) {
        fragment.showRequiredViews();
        fragment.setupToolbar();

        switch (state) {
            case FragmentState.STATE_CONTENT:
                fragment.setupRecyclerView(messages);
                break;
            case FragmentState.STATE_NO_INTERNET_CONNECTION:
                fragment.setupRetryButton();
                break;
            case FragmentState.STATE_NO_DATA:
                break;
        }
    }

    public void onRetryBtnClick() {
        model.sendTelegramMessagesRequest(context, presenter, chatId);
    }

    public void destroy() {
        fragment = null;
        model = null;
    }
}
