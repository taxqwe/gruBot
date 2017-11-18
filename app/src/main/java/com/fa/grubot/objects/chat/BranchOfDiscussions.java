package com.fa.grubot.objects.chat;

import java.util.Date;

/**
 * Created by ni.petrov on 18/11/2017.
 */

public class BranchOfDiscussions {
    private int id;

    private int authorsId;

    private String theme;

    private Date startDate;

    private Date lastDate;

    private int messagesCount;

    public BranchOfDiscussions(int id, int authorsId, String theme, Date startDate, Date lastDate, int messagesCount) {
        this.id = id;
        this.authorsId = authorsId;
        this.theme = theme;
        this.startDate = startDate;
        this.lastDate = lastDate;
        this.messagesCount = messagesCount;
    }

    public int getId() {
        return id;
    }

    public int getAuthorsId() {
        return authorsId;
    }

    public String getTheme() {
        return theme;
    }

    public String getStartDate() {
        return startDate.toString();
    }

    public String getLastDate() {
        return lastDate.toString();
    }

    public int getMessagesCount() {
        return messagesCount;
    }
}
