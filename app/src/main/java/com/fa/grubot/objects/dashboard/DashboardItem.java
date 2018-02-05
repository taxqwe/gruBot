package com.fa.grubot.objects.dashboard;

public class DashboardItem {
    private int newCount;
    private int archiveCount;

    public DashboardItem(int newCount, int archiveCount) {
        this.newCount = newCount;
        this.archiveCount = archiveCount;
    }

    public int getNewCount() {
        return newCount;
    }

    public int getArchiveCount() {
        return archiveCount;
    }

    public void updateNewCount(int count) {
        this.newCount += count;
    }

    public void updateArchiveCount(int count) {
        this.archiveCount += count;
    }

    public int getTotalCount() {
        return newCount + archiveCount;
    }
}
