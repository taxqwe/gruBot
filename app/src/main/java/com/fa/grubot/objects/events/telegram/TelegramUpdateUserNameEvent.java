package com.fa.grubot.objects.events.telegram;

import java.io.Serializable;

public class TelegramUpdateUserNameEvent implements Serializable {
    private int userId;
    private String firstName;
    private String lastName;
    private String userName;

    public TelegramUpdateUserNameEvent(int userId, String firstName, String lastName, String userName) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }
}
