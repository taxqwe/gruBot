package com.fa.grubot.objects;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DashboardEntry {
    public static final int TYPE_IMPORTANT_MESSAGE = 34;
    public static final int TYPE_VOTE = 36;

    private Map<Integer, String> typesPairList = new HashMap<>();

    private int id;
    private int type;
    private Group group;
    private String author;
    private String desc;
    private Date date;

    public DashboardEntry(int id, int type, Group group, String author, String desc, Date date) {
        this.id = id;
        this.type = type;
        this.group = group;
        this.author = author;
        this.desc = desc;
        this.date = date;

        typesPairList.put(TYPE_IMPORTANT_MESSAGE, "Важное сообщение");
        typesPairList.put(TYPE_VOTE, "Голосование");
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public Group getGroup() {
        return group;
    }

    public String getAuthor() {
        return author;
    }

    public String getDesc() {
        return desc;
    }

    public String getTypeText(){
        return typesPairList.get(this.type);
    }

    public String getDate() {
        Format formatter = new SimpleDateFormat("MMM dd, yyyy kk:mm", Locale.getDefault());
        return formatter.format(this.date);
    }
}
