package com.fa.grubot.objects.dashboard;

public class DashboardChat extends DashboardItem {
    private int newMessagesCount;
    private int totalChatsCount;

    public DashboardChat(int newMessagesCount, int totalChatsCount) {
        this.newMessagesCount = newMessagesCount;
        this.totalChatsCount = totalChatsCount;
    }

    public int getNewMessagesCount() {
        return newMessagesCount;
    }

    public int getTotalChatsCount() {
        return totalChatsCount;
    }
}
