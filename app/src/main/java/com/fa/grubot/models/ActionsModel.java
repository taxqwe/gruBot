package com.fa.grubot.models;

import android.content.Context;

import com.fa.grubot.objects.dashboard.Action;
import com.fa.grubot.util.Globals;
import com.fa.grubot.helpers.TemporaryDataHelper;

import java.util.ArrayList;

public class ActionsModel {

    public ActionsModel(){

    }
    public ArrayList<Action> loadActions(int type) {
        return TemporaryDataHelper.getActionsByType(type);
    }

    public boolean isNetworkAvailable(Context context){
        return Globals.InternetMethods.isNetworkAvailable(context);
    }
}
