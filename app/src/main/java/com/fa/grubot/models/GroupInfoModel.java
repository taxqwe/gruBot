package com.fa.grubot.models;

import com.fa.grubot.adapters.GroupInfoRecyclerAdapter;
import com.fa.grubot.objects.DashboardEntry;
import com.fa.grubot.objects.Group;
import com.fa.grubot.objects.GroupInfoButton;

import java.util.ArrayList;
import java.util.Date;

public class GroupInfoModel {

    String placeholder = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna " +
            "aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
            "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint " +
            "occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";

    public GroupInfoModel(){

    }
    public ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> loadButtons(Group group){
        ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> groupInfoRecyclerItems = new ArrayList<>();

        groupInfoRecyclerItems.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(new GroupInfoButton(1, "Чат", new ArrayList<>())));

        ArrayList<DashboardEntry> entries = getAnnouncementsByGroup(group);
        groupInfoRecyclerItems.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(new GroupInfoButton(2, "Объявления", entries)));
        for (DashboardEntry entry : entries) {
            groupInfoRecyclerItems.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(entry));
        }

        entries = getVotesByGroup(group);
        groupInfoRecyclerItems.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(new GroupInfoButton(3, "Голосования", entries)));
        for (DashboardEntry entry : entries) {
            groupInfoRecyclerItems.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(entry));
        }

        groupInfoRecyclerItems.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(new GroupInfoButton(4, "Список участников", new ArrayList<>())));

        return groupInfoRecyclerItems;
    }

    private ArrayList<DashboardEntry> getAnnouncementsByGroup(Group group) {
        ArrayList<DashboardEntry> entries = new ArrayList<>();

        switch (group.getId()) {
            case 1:
                entries.add(new DashboardEntry(1, DashboardEntry.TYPE_ANNOUNCEMENT, group, "Комлев Антон", "Собрание", placeholder, new Date()));
                entries.add(new DashboardEntry(1, DashboardEntry.TYPE_ANNOUNCEMENT, group, "Комлев Антон", "Выходные дни", placeholder, new Date()));
                break;
            case 2:
                break;
            case 3:
                entries.add(new DashboardEntry(3, DashboardEntry.TYPE_ANNOUNCEMENT, group, "Чехов А. П.", "Поездка", placeholder, new Date()));
                entries.add(new DashboardEntry(3, DashboardEntry.TYPE_ANNOUNCEMENT, group, "Чехов А. П.", "Объявление", placeholder, new Date()));
                entries.add(new DashboardEntry(3, DashboardEntry.TYPE_ANNOUNCEMENT, group, "Чехов А. П.", "Собрание", placeholder, new Date()));
                break;
        }
        return entries;
    }

    private ArrayList<DashboardEntry> getVotesByGroup(Group group) {
        ArrayList<DashboardEntry> entries = new ArrayList<>();
        switch (group.getId()) {
            case 1:
                entries.add(new DashboardEntry(1, DashboardEntry.TYPE_VOTE, group, "Комлев Антон", "Новый год", placeholder, new Date()));
                break;
            case 2:
                entries.add(new DashboardEntry(2, DashboardEntry.TYPE_VOTE, group, "Махин Семен", "Сбор денег", placeholder, new Date()));
                entries.add(new DashboardEntry(2, DashboardEntry.TYPE_VOTE, group, "Махин Семен", "Удовлетворенность чем-то", placeholder, new Date()));
                entries.add(new DashboardEntry(2, DashboardEntry.TYPE_VOTE, group, "Махин Семен", "Активность", placeholder, new Date()));
                break;
            case 3:
                break;
        }
        return entries;
    }
}
