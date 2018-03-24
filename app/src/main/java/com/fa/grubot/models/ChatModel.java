package com.fa.grubot.models;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import com.fa.grubot.App;
import com.fa.grubot.abstractions.MessagesListRequestResponse;
import com.fa.grubot.helpers.TelegramHelper;
import com.fa.grubot.objects.chat.ChatMessage;
import com.fa.grubot.objects.users.User;
import com.fa.grubot.presenters.ChatPresenter;
import com.fa.grubot.util.DataType;
import com.github.badoualy.telegram.api.TelegramClient;
import com.github.badoualy.telegram.tl.api.TLAbsChat;
import com.github.badoualy.telegram.tl.api.TLAbsInputChannel;
import com.github.badoualy.telegram.tl.api.TLAbsInputPeer;
import com.github.badoualy.telegram.tl.api.TLAbsInputUser;
import com.github.badoualy.telegram.tl.api.TLAbsPeer;
import com.github.badoualy.telegram.tl.api.TLChannel;
import com.github.badoualy.telegram.tl.api.TLDialog;
import com.github.badoualy.telegram.tl.api.TLInputChannel;
import com.github.badoualy.telegram.tl.api.TLInputPeerChannel;
import com.github.badoualy.telegram.tl.api.TLInputPeerChat;
import com.github.badoualy.telegram.tl.api.TLInputPeerEmpty;
import com.github.badoualy.telegram.tl.api.TLInputUser;
import com.github.badoualy.telegram.tl.api.TLMessage;
import com.github.badoualy.telegram.tl.api.TLPeerChannel;
import com.github.badoualy.telegram.tl.api.TLPeerChat;
import com.github.badoualy.telegram.tl.api.TLPeerUser;
import com.github.badoualy.telegram.tl.api.TLUser;
import com.github.badoualy.telegram.tl.api.TLUserFull;
import com.github.badoualy.telegram.tl.api.messages.TLAbsDialogs;
import com.github.badoualy.telegram.tl.api.messages.TLAbsMessages;
import com.github.badoualy.telegram.tl.api.messages.TLChatFull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ChatModel {

    public ChatModel() {
    }

    public void sendTelegramMessagesRequest(Context context, ChatPresenter presenter, String chatId) {
        GetMessagesList request = new GetMessagesList(context, chatId);
        request.response = presenter;
        request.execute();
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

                tlAbsMessages.getMessages().forEach(message -> {
                    if (message instanceof TLMessage) {
                        TLMessage tlMessage = (TLMessage) message;


                        System.out.println(tlMessage.getFromId() + ": " + tlMessage.getMessage());
                    }
                    else
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
