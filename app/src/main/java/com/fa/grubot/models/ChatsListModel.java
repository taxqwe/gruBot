package com.fa.grubot.models;

import android.content.Context;
import android.os.AsyncTask;
import android.util.SparseArray;

import com.fa.grubot.App;
import com.fa.grubot.abstractions.ChatsListRequestResponse;
import com.fa.grubot.helpers.TelegramHelper;
import com.fa.grubot.objects.chat.Chat;
import com.fa.grubot.objects.misc.TelegramPhoto;
import com.fa.grubot.presenters.ChatsListPresenter;
import com.fa.grubot.util.DataType;
import com.github.badoualy.telegram.api.TelegramClient;
import com.github.badoualy.telegram.tl.api.TLAbsMessage;
import com.github.badoualy.telegram.tl.api.TLAbsMessageAction;
import com.github.badoualy.telegram.tl.api.TLAbsPeer;
import com.github.badoualy.telegram.tl.api.TLInputPeerEmpty;
import com.github.badoualy.telegram.tl.api.TLMessage;
import com.github.badoualy.telegram.tl.api.TLMessageService;
import com.github.badoualy.telegram.tl.api.messages.TLAbsDialogs;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ChatsListModel {

    public ChatsListModel() {

    }

    public void sendChatsListRequest(Context context, ChatsListPresenter presenter) {
        GetChatsList request = new GetChatsList(context);
        request.response = presenter;

        request.execute();
    }

    public static class GetChatsList extends AsyncTask<Void, Void, ArrayList<Chat>> {
        private WeakReference<Context> context;
        public ChatsListRequestResponse response = null;

        private GetChatsList(Context context) {
            this.context = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Chat> doInBackground(Void... params) {
            ArrayList<Chat> chatsList = new ArrayList<>();
            TelegramClient client = App.INSTANCE.getNewTelegramClient(null);

            try {
                TLAbsDialogs tlAbsDialogs = client.messagesGetDialogs(false, 0, 0, new TLInputPeerEmpty(), 10000); //have no idea how to avoid the limit without a huge number

                SparseArray<String> namesMap = TelegramHelper.Chats.getChatNamesMap(tlAbsDialogs);
                SparseArray<TelegramPhoto> photoMap = TelegramHelper.Chats.getPhotoMap(tlAbsDialogs);
                SparseArray<TLAbsMessage> messagesMap = new SparseArray<>();

                tlAbsDialogs.getMessages().forEach(message -> messagesMap.put(message.getId(), message));

                tlAbsDialogs.getDialogs().forEach(dialog -> {
                    TLAbsPeer peer = dialog.getPeer();
                    Chat chat;
                    String lastMessageText = "";

                    int chatId = TelegramHelper.Chats.getId(peer);
                    String chatName = namesMap.get(chatId);

                    TLAbsMessage lastMessage = messagesMap.get(dialog.getTopMessage());

                    if (lastMessage instanceof TLMessage) {
                        lastMessageText = ((TLMessage) lastMessage).getMessage();
                    } else if (lastMessage instanceof TLMessageService) {
                        TLAbsMessageAction action = ((TLMessageService) lastMessage).getAction();
                        lastMessageText = action.toString();
                    }

                    TelegramPhoto telegramPhoto = photoMap.get(chatId);
                    String imgUri = TelegramHelper.Files.getImgById(client, telegramPhoto, context.get());

                    chat = new Chat(String.valueOf(chatId), chatName, null, imgUri, lastMessageText, DataType.Telegram);
                    chatsList.add(chat);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            return chatsList;
        }

        @Override
        protected void onPostExecute(ArrayList<Chat> chats) {
            if (response != null)
                response.onChatsListResult(chats);
        }
    }
}
