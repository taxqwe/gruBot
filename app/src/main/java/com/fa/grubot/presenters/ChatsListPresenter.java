package com.fa.grubot.presenters;


import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.fa.grubot.App;
import com.fa.grubot.abstractions.ChatsListFragmentBase;
import com.fa.grubot.abstractions.ChatsListRequestResponse;
import com.fa.grubot.callbacks.TelegramEventCallback;
import com.fa.grubot.models.ChatsListModel;
import com.fa.grubot.objects.chat.Chat;
import com.fa.grubot.objects.events.telegram.TelegramMessageEvent;
import com.fa.grubot.objects.events.telegram.TelegramUpdateUserNameEvent;
import com.fa.grubot.objects.events.telegram.TelegramUpdateUserPhotoEvent;
import com.fa.grubot.util.FragmentState;
import com.github.badoualy.telegram.api.TelegramClient;

import java.util.ArrayList;

public class ChatsListPresenter implements ChatsListRequestResponse {

    private ChatsListFragmentBase fragment;
    private ChatsListModel model;
    private Context context;

    private TelegramEventCallback.TelegramEventListener telegramEventListener;
    private ChatsListPresenter presenter = this;

    private ArrayList<Chat> chats = new ArrayList<>();
    private TelegramClient client;

    public ChatsListPresenter(ChatsListFragmentBase fragment, Context context) {
        this.fragment = fragment;
        this.model = new ChatsListModel();
        this.context = context;
    }

    public void notifyFragmentStarted() {
        fragment.setupToolbar();
        if (App.INSTANCE.getCurrentUser().hasTelegramUser())
            model.sendChatsListRequest(context, presenter, client);

    }

    private void notifyViewCreated(int state) {
        fragment.showRequiredViews();

        switch (state) {
            case FragmentState.STATE_CONTENT:
                fragment.setupRecyclerView(chats);
                break;
            case FragmentState.STATE_NO_INTERNET_CONNECTION:
                fragment.setupRetryButton();
                break;
            case FragmentState.STATE_NO_DATA:
                break;
        }
    }

    public void onChatsListResult(ArrayList<Chat> chats) {
        this.chats = chats;

        if (fragment != null) {
            if (chats.isEmpty()) {
                fragment.setupLayouts(true, false);
                notifyViewCreated(FragmentState.STATE_NO_DATA);
                this.chats = chats;
            } else if (!fragment.isAdapterExists() || fragment.isListEmpty()) {
                this.chats = chats;
                fragment.setupLayouts(true, true);
                notifyViewCreated(FragmentState.STATE_CONTENT);
                setUpdateCallback();
            } else if (fragment.isAdapterExists()) {
                //App.INSTANCE.closeTelegramClient();
                fragment.updateChatsList(chats);
                setUpdateCallback();
            }
        }
    }

    private void setUpdateCallback() {
        AsyncTask.execute(() -> {
            telegramEventListener = new TelegramEventCallback.TelegramEventListener() {
                @Override
                public void onMessage(TelegramMessageEvent telegramMessageEvent) {
                    ((AppCompatActivity) context).runOnUiThread(() -> onChatsListResult(model.onNewMessage(chats, telegramMessageEvent)));
                }

                @Override
                public void onUserNameUpdate(TelegramUpdateUserNameEvent telegramUpdateUserNameEvent) {

                }

                @Override
                public void onUserPhotoUpdate(TelegramUpdateUserPhotoEvent telegramUpdateUserPhotoEvent) {

                }
            };
            client = App.INSTANCE.getNewTelegramClient(new TelegramEventCallback(telegramEventListener));
        });
    }

    public void onRetryBtnClick() {
        //setRegistration();
    }

    public void destroy() {
        fragment = null;
        model = null;
    }
}
