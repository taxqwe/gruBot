package com.fa.grubot.models;

import android.content.Context;
import android.os.Handler;

import com.fa.grubot.objects.group.Group;
import com.fa.grubot.util.Globals;
import com.fa.grubot.util.TemporaryDataHelper;

import java.util.ArrayList;

public class GroupsModel {

    public GroupsModel(){

    }
    public ArrayList<Group> loadGroups(){
        return TemporaryDataHelper.getGroups();
    }

    public boolean isNetworkAvailable(Context context){
        return Globals.InternetMethods.isNetworkAvailable(context);
    }
}
