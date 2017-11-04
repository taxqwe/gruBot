package com.fa.grubot.objects;

import java.util.ArrayList;

/**
 * Created by ni.petrov on 22/10/2017.
 */

public class ToDoRemove {

    private int id;

    private ArrayList<ChatMessage> messages;

    private String name;

    public ToDoRemove(int id, String name, ArrayList<ChatMessage> messages) {
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
        return null;
    }

}
