package com.fa.grubot.objects.group;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    private String id;
    private String name;
    private ArrayList<DocumentReference> users;
    private String imgURL;

    public Group(String id, String name, ArrayList<DocumentReference> users, String imgURL) {
        this.id = id;
        this.name = name;
        this.users = users;
        this.imgURL = imgURL;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImgURL() {
        return imgURL;
    }

    public ArrayList<DocumentReference> getUsers() {
        return users;
    }
}
