package com.fa.grubot.objects.misc;

public class VoteOption {
    private String text;

    public VoteOption() {
        this.text = "";
    }

    public VoteOption(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
