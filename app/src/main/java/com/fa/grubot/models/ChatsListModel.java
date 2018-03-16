package com.fa.grubot.models;

import android.util.SparseArray;

import com.fa.grubot.App;
import com.fa.grubot.helpers.Telegram;
import com.fa.grubot.objects.Chat;
import com.github.badoualy.telegram.api.TelegramClient;
import com.github.badoualy.telegram.tl.api.TLAbsMessage;
import com.github.badoualy.telegram.tl.api.TLAbsMessageAction;
import com.github.badoualy.telegram.tl.api.TLInputPeerEmpty;
import com.github.badoualy.telegram.tl.api.TLMessage;
import com.github.badoualy.telegram.tl.api.TLMessageService;
import com.github.badoualy.telegram.tl.api.messages.TLAbsDialogs;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;

public class ChatsListModel {

    public ChatsListModel() {

    }

    public Observable<ArrayList<Chat>> getChatsObservable() {
        return ReplaySubject
                .interval(10, TimeUnit.SECONDS)
                .map(interval -> {
                    ArrayList<Chat> chatsList = new ArrayList<>();
                    TelegramClient client = App.INSTANCE.getNewTelegramClient();
                    Telegram telegram = new Telegram();

                    TLAbsDialogs tlAbsDialogs = client.messagesGetDialogs(false, 0, 0, new TLInputPeerEmpty(), 1000); //have no idea how to avoid the limit
                    SparseArray<String> namesMap = telegram.getChatNamesMap(tlAbsDialogs);

                    SparseArray<TLAbsMessage> messagesMap = new SparseArray<>();
                    tlAbsDialogs.getMessages().forEach(message -> messagesMap.put(message.getId(), message));

                    tlAbsDialogs.getDialogs().forEach(dialog -> {
                        Chat chat;
                        String lastMessageText = "";

                        int chatId = telegram.getId(dialog.getPeer());
                        String chatName = namesMap.get(chatId);

                        TLAbsMessage lastMessage = messagesMap.get(dialog.getTopMessage());

                        if (lastMessage instanceof TLMessage) {
                            lastMessageText = ((TLMessage) lastMessage).getMessage();
                            // The message could also be a file, a photo, a gif, ...
                        } else if (lastMessage instanceof TLMessageService) {
                            TLAbsMessageAction action = ((TLMessageService) lastMessage).getAction();
                            lastMessageText = action.toString();
                        }

                        chat = new Chat(String.valueOf(chatId), chatName, null, lastMessageText);
                        chatsList.add(chat);
                    });

                    return chatsList;
                })
                .subscribeOn(Schedulers.io())
                .timeout(30, TimeUnit.SECONDS);
    }
}
