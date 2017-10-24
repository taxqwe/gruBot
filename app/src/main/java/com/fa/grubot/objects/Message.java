package com.fa.grubot.objects;


import android.text.format.Time;

/**
 * Created by ni.petrov on 22/10/2017.
 */

public class Message {

    private int id;

    private Time time;

    private String text;

    private String sender;

    public Message(int id, Time time, String text, String sender) {
        this.id = id;
        this.time = time;
        this.text = text;
        this.sender = sender;
    }

    public int getId() {
        return id;
    }

    public Time getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    public String getSender() {
        return sender;
    }
}
