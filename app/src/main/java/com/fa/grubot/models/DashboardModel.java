package com.fa.grubot.models;

import android.content.Context;

import com.fa.grubot.objects.dashboard.DashboardAnnouncement;
import com.fa.grubot.objects.dashboard.DashboardItem;
import com.fa.grubot.objects.dashboard.DashboardVote;
import com.fa.grubot.util.Globals;

import java.util.ArrayList;

public class DashboardModel {

    public DashboardModel(){

    }

    public ArrayList<DashboardItem> getItems() {
        ArrayList<DashboardItem> items = new ArrayList<>();
        items.add(new DashboardAnnouncement(5, 14, 19));
        items.add(new DashboardVote(5, 7, 12));
        return items;
    }

    public boolean isNetworkAvailable(Context context) {
        return Globals.InternetMethods.isNetworkAvailable(context);
    }
}
