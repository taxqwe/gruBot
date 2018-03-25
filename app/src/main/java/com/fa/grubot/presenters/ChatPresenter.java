package com.fa.grubot.presenters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fa.grubot.App;
import com.fa.grubot.abstractions.ChatFragmentBase;
import com.fa.grubot.abstractions.ChatMessageSendRequestResponse;
import com.fa.grubot.abstractions.MessagesListRequestResponse;
import com.fa.grubot.callbacks.TelegramEventCallback;
import com.fa.grubot.helpers.TelegramHelper;
import com.fa.grubot.models.ChatModel;
import com.fa.grubot.objects.chat.ChatMessage;
import com.fa.grubot.objects.events.telegram.TelegramMessageEvent;
import com.fa.grubot.objects.events.telegram.TelegramUpdateUserNameEvent;
import com.fa.grubot.objects.events.telegram.TelegramUpdateUserPhotoEvent;
import com.fa.grubot.objects.users.User;
import com.fa.grubot.util.FragmentState;
import com.github.badoualy.telegram.api.TelegramClient;

import java.sql.Date;
import java.util.ArrayList;

public class ChatPresenter implements MessagesListRequestResponse, ChatMessageSendRequestResponse {

    private ChatFragmentBase fragment;
    private ChatModel model;
    private Context context;

    private TelegramEventCallback.TelegramEventListener telegramEventListener;

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

    public void sendMessage(String message) {
        model.sendMessage(context, chatId, presenter, message);
    }


    @Override
    public void onMessageSent(ChatMessage chatMessage) {
        fragment.onMessageReceived(chatMessage);
    }

    @Override
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

            if (client == null || client.isClosed())
                setUpdateCallback();
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

    public void setUpdateCallback() {
        AsyncTask.execute(() -> {
            telegramEventListener = new TelegramEventCallback.TelegramEventListener() {
                @Override
                public void onMessage(TelegramMessageEvent telegramMessageEvent) {
                    Log.d("debug", "Received a message: " + telegramMessageEvent.getMessage());
                    if (telegramMessageEvent.getToId() == Integer.valueOf(chatId)) {
                        int messageId = telegramMessageEvent.getMessageId();
                        String messageText = telegramMessageEvent.getMessage();
                        Date createdAt = new Date(telegramMessageEvent.getDate());
                        int userId = telegramMessageEvent.getFromId();

                        User user;
                        try {
                            user = TelegramHelper.Chats.getChatUser(client.getDownloaderClient(), userId, context);
                        } catch (Exception e) {
                            user = TelegramHelper.Chats.getChatAsUser(client.getDownloaderClient(), userId, context);
                        }

                        ChatMessage chatMessage = new ChatMessage(String.valueOf(messageId), messageText, user, createdAt);
                        ((AppCompatActivity) context).runOnUiThread(() -> fragment.onMessageReceived(chatMessage));
                    }
                }

                @Override
                public void onUserNameUpdate(TelegramUpdateUserNameEvent telegramUpdateUserNameEvent) {
                }

                @Override
                public void onUserPhotoUpdate(TelegramUpdateUserPhotoEvent telegramUpdateUserPhotoEvent) {
                }
            };
            client = App.INSTANCE.getNewTelegramClient(new TelegramEventCallback(telegramEventListener, context));
        });
    }

    public void onRetryBtnClick() {
        model.sendTelegramMessagesRequest(context, presenter, chatId);
    }

    public void destroy() {
        if (client != null)
            client.close(false);
        fragment = null;
        model = null;
    }
}
