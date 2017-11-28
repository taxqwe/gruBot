package com.fa.grubot.objects.dashboard;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;
import java.util.Map;

public class ActionAnnouncement extends Action {
    private String text;

    public ActionAnnouncement(String group, DocumentReference author, String desc, Date date, String text, Map<String, Boolean> users) {
        super(group, author, desc, date, users);

        this.text = text;
    }

    public String getText() {
        return text;
    }
}
