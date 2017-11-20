package com.fa.grubot.objects.group;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    private int id;
    private String name;
    private ArrayList<User> users;
    private String imgURL;

    public Group(int id, String name, ArrayList<User> users, String imgURL) {
        this.id = id;
        this.name = name;
        this.users = users;
        this.imgURL = imgURL;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImgURL() {
        return imgURL;
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
