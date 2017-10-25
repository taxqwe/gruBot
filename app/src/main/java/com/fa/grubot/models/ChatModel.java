package com.fa.grubot.models;

import android.text.format.Time;

import com.fa.grubot.objects.Message;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ni.petrov on 22/10/2017.
 */

public class ChatModel {

    public ChatModel() {
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
