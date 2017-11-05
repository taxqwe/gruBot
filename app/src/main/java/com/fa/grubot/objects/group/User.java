package com.fa.grubot.objects.group;

public class User {
    private int id;
    private String username;
    private String fullname;
    private String phoneNumber;
    private String desc;

    public User(int id, String username, String fullname, String phoneNumber, String desc) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDesc() {
        return desc;
    }
}
