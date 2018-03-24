package com.fa.grubot.models;

import android.content.Context;
import android.os.AsyncTask;
import android.util.SparseArray;

import com.fa.grubot.App;
import com.fa.grubot.abstractions.MessagesListRequestResponse;
import com.fa.grubot.helpers.TelegramHelper;
import com.fa.grubot.objects.chat.ChatMessage;
import com.fa.grubot.objects.users.User;
import com.fa.grubot.presenters.ChatPresenter;
import com.github.badoualy.telegram.api.TelegramClient;
import com.github.badoualy.telegram.tl.api.TLAbsInputPeer;
import com.github.badoualy.telegram.tl.api.TLInputPeerEmpty;
import com.github.badoualy.telegram.tl.api.TLMessage;
import com.github.badoualy.telegram.tl.api.TLPeerChannel;
import com.github.badoualy.telegram.tl.api.messages.TLAbsDialogs;
import com.github.badoualy.telegram.tl.api.messages.TLAbsMessages;

import java.lang.ref.WeakReference;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Random;

public class ChatModel {

    public ChatModel() {
    }

    public void sendTelegramMessagesRequest(Context context, ChatPresenter presenter, String chatId) {
        GetMessagesList request = new GetMessagesList(context, chatId);
        request.response = presenter;
        request.execute();
    }

    public void sendMessage(Context context, String chatId, String message) {
        SendMessage request = new SendMessage(context, chatId, message);
        request.execute();
    }

    public static class SendMessage extends AsyncTask<Void, Void, Object> {
        private WeakReference<Context> context;
        private String message;
        private String chatId;
        private MessagesListRequestResponse response = null;

        private SendMessage(Context context, String chatId, String message) {
            this.context = new WeakReference<>(context);
            this.message = message;
            this.chatId = chatId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Void... params) {
            TelegramClient client = App.INSTANCE.getNewTelegramClient(null).getDownloaderClient();

            try {
                TLAbsDialogs tlAbsDialogs = client.messagesGetDialogs(false, 0, 0, new TLInputPeerEmpty(), 0);
                TLAbsInputPeer inputPeer = TelegramHelper.Chats.getInputPeer(tlAbsDialogs, chatId);
                client.messagesSendMessage(inputPeer, message, Math.abs(new Random().nextLong()));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                client.close(false);
            }
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onPostExecute(Object result) {
            //if (response != null && result instanceof ArrayList<?>)
                //response.onMessagesListResult((ArrayList<ChatMessage>) result, true);
        }
    }

    public static class GetMessagesList extends AsyncTask<Void, Void, Object> {
        private WeakReference<Context> context;
        private String chatId;
        private MessagesListRequestResponse response = null;

        private GetMessagesList(Context context, String chatId) {
            this.context = new WeakReference<>(context);
            this.chatId = chatId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Void... params) {
            ArrayList<ChatMessage> messages = new ArrayList<>();
            TelegramClient client = App.INSTANCE.getNewTelegramClient(null).getDownloaderClient();

            try {
                TLAbsDialogs tlAbsDialogs = client.messagesGetDialogs(false, 0, 0, new TLInputPeerEmpty(), 10000);
                TLAbsInputPeer inputPeer = TelegramHelper.Chats.getInputPeer(tlAbsDialogs, chatId);
                TLAbsMessages tlAbsMessages = client.messagesGetHistory(inputPeer, 0, 0, 0, 40, 0, 0); //TODO add an offset here
                SparseArray<User> users = TelegramHelper.Chats.getChatUsers(client, tlAbsMessages, context.get());

                tlAbsMessages.getMessages().forEach(message -> {
                    if (message instanceof TLMessage) {
                        TLMessage tlMessage = (TLMessage) message;

                        User user;
                        try {
                            user = users.get(tlMessage.getFromId());
                        } catch (Exception e) {
                            int chatId = ((TLPeerChannel) tlMessage.getToId()).getChannelId();
                            user = TelegramHelper.Chats.getChatAsUser(client, chatId, context.get());
                        }

                        ChatMessage chatMessage = new ChatMessage(String.valueOf(tlMessage.getId()),
                                tlMessage.getMessage(),
                                user,
                                new Date(((long) tlMessage.getDate()) * 1000));

                        messages.add(chatMessage);
                    } else
                        System.out.println("Service message");
                });

                return messages;
            } catch (Exception e) {
                e.printStackTrace();
                return e;
            } finally {
                client.close(false);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onPostExecute(Object result) {
            if (response != null && result instanceof ArrayList<?>)
                response.onMessagesListResult((ArrayList<ChatMessage>) result, true);
        }
    }
}
