package com.fa.grubot.objects.group;

import java.io.Serializable;

public class Group implements Serializable {
    private int id;
    private String name;
    private String imgURL;

    public Group(int id, String name, String imgURL) {
        this.id = id;
        this.name = name;
        this.imgURL = imgURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getImgURL() {
        return imgURL;
    }
}
