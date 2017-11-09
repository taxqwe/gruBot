package com.fa.grubot.objects.dashboard;

public class DashboardVote extends DashboardItem{
    private int newVotesCount;
    private int archiveVotesCount;
    private int totalVotesCount;

    public DashboardVote(int newVotesCount, int archiveVotesCount, int totalVotesCount) {
        this.newVotesCount = newVotesCount;
        this.archiveVotesCount = archiveVotesCount;
        this.totalVotesCount = totalVotesCount;
    }

    public int getNewVotesCount() {
        return newVotesCount;
    }

    public int getArchiveVotesCount() {
        return archiveVotesCount;
    }

    public int getTotalVotesCount() {
        return totalVotesCount;
    }
}
