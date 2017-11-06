package com.fa.grubot.models;

import android.content.Context;

import com.fa.grubot.util.Globals;

public class DashboardModel {

    public DashboardModel(){

    }

    public boolean isNetworkAvailable(Context context){
        return Globals.InternetMethods.isNetworkAvailable(context);
    }
}
