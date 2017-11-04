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

        String placeholder = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna " +
                "aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint " +
                "occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";
      
        entries.add(new DashboardEntry(1, DashboardEntry.TYPE_ANNOUNCEMENT, new Group(1, "ПИ4-1", null), "Комлев Антон", "Собрание", placeholder, new Date()));
        entries.add(new DashboardEntry(1, DashboardEntry.TYPE_ANNOUNCEMENT, new Group(1, "ПИ4-1", null), "Комлев Антон", "Выходные дни", placeholder, new Date()));
        entries.add(new DashboardEntry(1, DashboardEntry.TYPE_VOTE, new Group(1, "ПИ4-1", null), "Комлев Антон", "Новый год", placeholder, new Date()));

        entries.add(new DashboardEntry(2, DashboardEntry.TYPE_VOTE, new Group(2, "ПИ4-2", null), "Махин Семен", "Сбор денег", placeholder, new Date()));
        entries.add(new DashboardEntry(2, DashboardEntry.TYPE_VOTE, new Group(2, "ПИ4-2", null), "Махин Семен", "Удовлетворенность чем-то", placeholder, new Date()));
        entries.add(new DashboardEntry(2, DashboardEntry.TYPE_VOTE, new Group(2, "ПИ4-2", null), "Махин Семен", "Активность", placeholder, new Date()));


        entries.add(new DashboardEntry(3, DashboardEntry.TYPE_ANNOUNCEMENT, new Group(3, "ГРУППА НАМБА ВАН НА РУСИ", null), "Чехов А. П.", "Поездка", placeholder, new Date()));
        entries.add(new DashboardEntry(3, DashboardEntry.TYPE_ANNOUNCEMENT, new Group(3, "ГРУППА НАМБА ВАН НА РУСИ", null), "Чехов А. П.", "Объявление", placeholder, new Date()));
        entries.add(new DashboardEntry(3, DashboardEntry.TYPE_ANNOUNCEMENT, new Group(3, "ГРУППА НАМБА ВАН НА РУСИ", null), "Чехов А. П.", "Собрание", placeholder, new Date()));

        return entries;
    }

    public boolean isNetworkAvailable(Context context){
        return Globals.InternetMethods.isNetworkAvailable(context);
    }
}
