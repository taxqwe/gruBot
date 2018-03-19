package com.fa.grubot.objects.events.telegram;

import java.io.Serializable;

public class TelegramMessageEvent implements Serializable {
    private String message;
    private int fromId;
    private int toId;
    private long date;
    private String nameFrom;

    public TelegramMessageEvent(String message, int fromId, int toId, long date, String nameFrom) {
        this.message = message;
        this.fromId = fromId;
        this.toId = toId;
        this.date = date;
        this.nameFrom = nameFrom;
    }

    public String getMessage() {
        return message;
    }

    public int getFromId() {
        return fromId;
    }

    public int getToId() {
        return toId;
    }

    public long getDate() {
        return date;
    }

    public String getNameFrom() {
        return nameFrom;
    }
}
