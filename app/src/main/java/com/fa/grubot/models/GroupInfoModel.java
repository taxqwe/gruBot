package com.fa.grubot.models;

import com.fa.grubot.objects.GroupInfoButton;

import java.util.ArrayList;

public class GroupInfoModel {

    public GroupInfoModel(){

    }
    public ArrayList<GroupInfoButton> loadButtons(){
        ArrayList<GroupInfoButton> buttons = new ArrayList<>();
        buttons.add(new GroupInfoButton(1, "Чат", 0));
        buttons.add(new GroupInfoButton(2, "Важные сообщения", 3));
        buttons.add(new GroupInfoButton(3, "Голосования", 1));
        buttons.add(new GroupInfoButton(4, "Список участников", 24));
        return buttons;
    }
}
