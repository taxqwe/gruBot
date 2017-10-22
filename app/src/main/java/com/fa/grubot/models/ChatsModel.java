package com.fa.grubot.models;

import android.text.TextUtils;
import android.text.format.Time;

import com.fa.grubot.objects.Chat;
import com.fa.grubot.objects.Message;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ni.petrov on 22/10/2017.
 */

public class ChatsModel {

    public ChatsModel() {
    }

    public ArrayList<Message> getMessagesOfChatById(int id){
        ArrayList<Message> messages = new ArrayList<>();

        int randomNumberOfMessages = new Random().nextInt(25 - 10 + 1) + 10;

        Time time = new Time();
        time.setToNow();

        for (int i = 0; i < randomNumberOfMessages; i++) {
            messages.add(new Message(i, time, generateRandomString(10,35), generateRandomString(3,10)));
        }

        return messages;
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
}
