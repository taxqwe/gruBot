package com.fa.grubot.objects.group;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    private String id;
    private String name;
    private ArrayList<String> users;
    private String imgURL;

    public Group(String id, String name, ArrayList<String> users, String imgURL) {
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

    public ArrayList<String> getUsers() {
        return users;
    }
}
