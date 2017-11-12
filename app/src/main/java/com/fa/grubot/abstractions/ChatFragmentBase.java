package com.fa.grubot.abstractions;

import com.fa.grubot.objects.chat.ChatMessage;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ni.petrov on 22/10/2017.
 */

public interface ChatFragmentBase {

    void subscribeOnNewMessages(Observable<ChatMessage> messagesObservable);

    void setUserId(int id);

    void drawMessage(ChatMessage msg, boolean needToScroll);

    void drawCachedMessages(List<ChatMessage> messages);

}
