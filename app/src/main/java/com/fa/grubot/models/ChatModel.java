package com.fa.grubot.models;

import com.fa.grubot.objects.chat.ChatMessage;
import com.fa.grubot.objects.group.User;

import java.util.ArrayList;
import java.util.Date;
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
                            ChatMessage message = new ChatMessage(String.valueOf(interval),
                                    "message#" + interval + " : " + generateRandomString(3, 15),
                                    new User("2",
                                            "PWND",
                                            "Комлев Антон",
                                            "123",
                                            "123",
                                            "https://img00.deviantart.net/fc89/i/2014/245/a/5/stalin_the_cat_23_by_kurogn-d7xngqa.jpg"),
                                    new Date());
                            return message;
                        }
                );
    }

    public void sendMessage(ChatMessage message) {
        // message goes to server
    }

    public ArrayList<ChatMessage> getCachedMessages() {
        ArrayList<ChatMessage> cachedMessages = new ArrayList<>();
        cachedMessages.add(new ChatMessage("-3", "Cached message #1",
                new User("2",
                        "PWND",
                        "Комлев Антон",
                        "123",
                        "123",
                        "https://img00.deviantart.net/fc89/i/2014/245/a/5/stalin_the_cat_23_by_kurogn-d7xngqa.jpg"),
                new Date()));
        cachedMessages.add(new ChatMessage("-2", "Cached message #2",
                new User("2",
                        "PWND",
                        "Комлев Антон",
                        "123",
                        "123",
                        "https://img00.deviantart.net/fc89/i/2014/245/a/5/stalin_the_cat_23_by_kurogn-d7xngqa.jpg"),
                new Date()));
        cachedMessages.add(new ChatMessage("-1", "Cached message #3",
                new User("2",
                        "PWND",
                        "Комлев Антон",
                        "123",
                        "123",
                        "https://img00.deviantart.net/fc89/i/2014/245/a/5/stalin_the_cat_23_by_kurogn-d7xngqa.jpg"),
                new Date()));

        return cachedMessages;
    }
}
