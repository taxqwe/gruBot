package com.fa.grubot.objects;

public class GroupInfoButton {
    private int id;
    private String text;
    private int notificationsCount;

    public GroupInfoButton(int id, String text, int notificationsCount) {
        this.id = id;
        this.text = text;
        this.notificationsCount = notificationsCount;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getNotificationsCount() {
        return notificationsCount;
    }
}
