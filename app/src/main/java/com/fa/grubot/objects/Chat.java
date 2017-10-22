package com.fa.grubot.objects;

import java.util.ArrayList;

/**
 * Created by ni.petrov on 22/10/2017.
 */

public class Chat {

    private int id;

    private ArrayList<Message> messages;

    private String name;

    public Chat(int id, String name, ArrayList<Message> messages) {
        this.id = id;
        this.name = name;
        this.messages = messages;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public String getLastMessageText() {
        return messages.get(messages.size()).getText();
    }

    public String getLastMessageDate() {
        return messages.get(messages.size()).getTime().toString();
    }

}
