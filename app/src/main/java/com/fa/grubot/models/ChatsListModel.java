package com.fa.grubot.models;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import com.fa.grubot.App;
import com.fa.grubot.abstractions.ChatsListRequestResponse;
import com.fa.grubot.helpers.TelegramHelper;
import com.fa.grubot.objects.chat.Chat;
import com.fa.grubot.objects.events.telegram.TelegramMessageEvent;
import com.fa.grubot.objects.misc.TelegramPhoto;
import com.fa.grubot.presenters.ChatsListPresenter;
import com.fa.grubot.util.DataType;
import com.github.badoualy.telegram.api.TelegramClient;
import com.github.badoualy.telegram.tl.api.TLAbsMessage;
import com.github.badoualy.telegram.tl.api.TLAbsMessageAction;
import com.github.badoualy.telegram.tl.api.TLAbsPeer;
import com.github.badoualy.telegram.tl.api.TLAbsUser;
import com.github.badoualy.telegram.tl.api.TLInputPeerEmpty;
import com.github.badoualy.telegram.tl.api.TLMessage;
import com.github.badoualy.telegram.tl.api.TLMessageService;
import com.github.badoualy.telegram.tl.api.TLPeerChannel;
import com.github.badoualy.telegram.tl.api.TLPeerChat;
import com.github.badoualy.telegram.tl.api.TLPeerUser;
import com.github.badoualy.telegram.tl.api.TLUser;
import com.github.badoualy.telegram.tl.api.messages.TLAbsDialogs;
import com.github.badoualy.telegram.tl.exception.RpcErrorException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;

public class ChatsListModel {

    public ChatsListModel() {

    }

    public void sendChatsListRequest(Context context, ChatsListPresenter presenter, TelegramClient client) {
        GetChatsList request = new GetChatsList(context, client);
        request.response = presenter;

        request.execute();
        /*if (client == null)
            request.execute();
        else
            ((AppCompatActivity) context).runOnUiThread(() -> {
                (new Handler()).postDelayed(request::execute, 1000);
            });*/
    }

    public static class GetChatsList extends AsyncTask<Void, Void, Object> {
        private WeakReference<Context> context;
        private ChatsListRequestResponse response = null;
        private TelegramClient client;

        private GetChatsList(Context context, TelegramClient client) {
            this.context = new WeakReference<>(context);
            this.client = client;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Void... params) {
            ArrayList<Chat> chatsList = new ArrayList<>();
            if (client == null || client.isClosed())
                client = App.INSTANCE.getNewTelegramClient(null);

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
                    long lastMessageDate = 0;
                    String chatName = namesMap.get(chatId);
                    String fromName = null;

                    TLAbsMessage lastMessage = messagesMap.get(dialog.getTopMessage());

                    if (lastMessage instanceof TLMessage) {
                        TLMessage message = (TLMessage) lastMessage;
                        lastMessageDate = ((TLMessage) lastMessage).getDate();

                        if (peer instanceof TLPeerChat) {
                            TLUser user = TelegramHelper.Users.getUser(client, message.getFromId()).getUser().getAsUser();
                            fromName = user.getFirstName() + " " + user.getLastName();
                            fromName = fromName.replace("null", "").trim();
                        }

                        if (message.getMedia() == null)
                            lastMessageText = message.getMessage();
                        else
                            lastMessageText = TelegramHelper.Chats.extractMediaType(message.getMedia());
                    } else if (lastMessage instanceof TLMessageService) {
                        TLAbsMessageAction action = ((TLMessageService) lastMessage).getAction();
                        lastMessageText = TelegramHelper.Chats.extractActionType(action);
                        lastMessageDate = ((TLMessageService) lastMessage).getDate();
                    }

                    TelegramPhoto telegramPhoto = photoMap.get(chatId);
                    String imgUri = TelegramHelper.Files.getImgById(client, telegramPhoto, context.get());

                    chat = new Chat(String.valueOf(chatId), chatName, null, imgUri, lastMessageText, DataType.Telegram, lastMessageDate * 1000, fromName);
                    chatsList.add(chat);
                });
                return chatsList;
            } catch (Exception e) {
                e.printStackTrace();
                return e;
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onPostExecute(Object result) {
            if (response != null) {
                if (result instanceof ArrayList<?>)
                    response.onChatsListResult((ArrayList<Chat>) result);
                else if (result instanceof RpcErrorException) {
                    RpcErrorException exception = (RpcErrorException) result;
                    String tag = exception.getTag();
                    if (tag.contains("FLOOD_WAIT_")) {
                        String waitTime = tag.substring(tag.lastIndexOf('_') + 1);

                        GetChatsList request = new GetChatsList(context.get(), client);
                        request.response = response;
                        //((AppCompatActivity) context.get()).runOnUiThread(() -> {
                            (new Handler()).postDelayed(request::execute, Integer.valueOf(waitTime) * 1000 + 1000);
                        //});
                    }
                }
            }
        }
    }

    public ArrayList<Chat> onNewMessage(ArrayList<Chat> chats, TelegramMessageEvent event) {
        Chat chatToChange = null;
        int index = -1;
        int idToFind = -1;

        //if (event.getFromId() == App.INSTANCE.getCurrentUser().getTelegramUser().getId())
            idToFind = event.getToId();
        //else
        //    idToFind = event.getFromId();

        for (Chat chat : chats) {
            if (Integer.valueOf(chat.getId()) == idToFind) {
                chatToChange = chat;
                index = chats.indexOf(chat);
                break;
            }
        }

        if (chatToChange != null) {
            chatToChange.setLastMessage(event.getMessage());
            chatToChange.setLastMessageDate(event.getDate() * 1000);
            chatToChange.setLastMessageFrom(event.getNameFrom());
            chats.remove(index);
            chats.add(0, chatToChange);
        }

        return chats;
    }
}
