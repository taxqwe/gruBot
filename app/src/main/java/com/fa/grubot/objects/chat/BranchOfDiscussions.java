package com.fa.grubot.objects.chat;

/**
 * Created by ni.petrov on 18/11/2017.
 */

public class BranchOfDiscussions {
    private int id;

    private int authorsId;

    private int theme;

    public BranchOfDiscussions(int id, int authorsId, int theme) {
        this.id = id;
        this.authorsId = authorsId;
        this.theme = theme;
    }
}
