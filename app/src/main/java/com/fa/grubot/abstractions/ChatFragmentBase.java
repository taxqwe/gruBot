package com.fa.grubot.abstractions;

import com.fa.grubot.objects.ChatMessage;

import io.reactivex.Observable;

/**
 * Created by ni.petrov on 22/10/2017.
 */

public interface ChatFragmentBase {

    void subscribeOnNewMessages(Observable<ChatMessage> messagesObservable);

    void setUserId(int id);

}
