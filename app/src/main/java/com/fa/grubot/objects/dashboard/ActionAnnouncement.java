package com.fa.grubot.objects.dashboard;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public class ActionAnnouncement extends Action {
    private String text;

    public ActionAnnouncement(String id, DocumentReference group, DocumentReference author, String desc, Date date, String text) {
        super(id, group, author, desc, date);

        this.text = text;
    }

    public String getText() {
        return text;
    }
}
