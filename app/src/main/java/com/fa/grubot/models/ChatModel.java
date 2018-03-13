package com.fa.grubot.models;

import com.fa.grubot.objects.chat.ChatMessage;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.subjects.ReplaySubject;

/**
 * Created by ni.petrov on 22/10/2017.
 */

public class ChatModel {

    public ChatModel() {
    }

    private String generateRandomString(int min, int max){
        int randomLength = new Random().nextInt(max - min + 1) + max;

        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < randomLength; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }


    public Observable<ChatMessage> getMessagesObservable() {
        return ReplaySubject
                .interval(5, TimeUnit.SECONDS)
                .map(interval -> {

                            return null;
                        }
                );
    }

    public void sendMessage(ChatMessage message) {
        // message goes to server
    }

    public ArrayList<ChatMessage> getCachedMessages() {
        ArrayList<ChatMessage> cachedMessages = new ArrayList<>();


        return cachedMessages;
    }
}
