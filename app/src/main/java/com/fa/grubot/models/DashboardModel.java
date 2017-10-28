package com.fa.grubot.models;

import android.content.Context;

import com.fa.grubot.objects.DashboardEntry;
import com.fa.grubot.objects.Group;
import com.fa.grubot.util.Globals;

import java.util.ArrayList;
import java.util.Date;

public class DashboardModel {

    public DashboardModel(){

    }
    public ArrayList<DashboardEntry> loadDashboard(){
        ArrayList<DashboardEntry> entries = new ArrayList<>();
        entries.add(new DashboardEntry(1, DashboardEntry.TYPE_ANNOUNCEMENT, new Group(1, "ПИ4-1", null), "Комлев Антон", "Важное сообщение", new Date()));
        entries.add(new DashboardEntry(2, DashboardEntry.TYPE_VOTE, new Group(2, "ПИ4-2", null), "Махин Семен", "Голосование", new Date()));
        entries.add(new DashboardEntry(3, DashboardEntry.TYPE_ANNOUNCEMENT, new Group(3, "ГРУППА НАМБА ВАН НА РУСИ", null), "Чехов А. П.", "АЗАЗЗАЗА ЗАТРАЛЕЛ", new Date()));
        return entries;
    }

    public boolean isNetworkAvailable(Context context){
        return Globals.InternetMethods.isNetworkAvailable(context);
    }
}
