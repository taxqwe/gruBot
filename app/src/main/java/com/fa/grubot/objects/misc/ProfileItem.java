package com.fa.grubot.objects.misc;

public class ProfileItem {
    private String value;
    private String text;

    public ProfileItem(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
