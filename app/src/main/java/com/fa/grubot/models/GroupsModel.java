package com.fa.grubot.models;

import android.content.Context;

import com.fa.grubot.objects.group.Group;
import com.fa.grubot.util.Globals;

import java.util.ArrayList;

public class GroupsModel {

    public GroupsModel(){

    }
    public ArrayList<Group> loadGroups(){
        ArrayList<Group> groups = new ArrayList<>();
        groups.add(new Group(1, "ПИ4-1", "https://2static3.fjcdn.com/comments/Fun+fact+the+flat+topped+great+helm+is+a+piece+_3cb2af934364bbe51707d55061d6aacb.jpg"));
        groups.add(new Group(2, "ПИ4-2", null));
        groups.add(new Group(3, "ГРУППА НАМБА ВАН НА РУСИ", null));
        return groups;
    }

    public boolean isNetworkAvailable(Context context){
        return Globals.InternetMethods.isNetworkAvailable(context);
    }
}
