package com.fa.grubot.models;

import com.fa.grubot.adapters.GroupInfoRecyclerAdapter;
import com.fa.grubot.objects.DashboardEntry;
import com.fa.grubot.objects.Group;
import com.fa.grubot.objects.GroupInfoButton;

import java.util.ArrayList;
import java.util.Date;

public class GroupInfoModel {

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
                entries.add(new DashboardEntry(1, DashboardEntry.TYPE_ANNOUNCEMENT, group, "Комлев Антон", "Собрание", new Date()));
                entries.add(new DashboardEntry(1, DashboardEntry.TYPE_ANNOUNCEMENT, group, "Комлев Антон", "Выходные дни", new Date()));
                break;
            case 2:
                break;
            case 3:
                entries.add(new DashboardEntry(3, DashboardEntry.TYPE_ANNOUNCEMENT, group, "Чехов А. П.", "Поездка", new Date()));
                entries.add(new DashboardEntry(3, DashboardEntry.TYPE_ANNOUNCEMENT, group, "Чехов А. П.", "Объявление", new Date()));
                entries.add(new DashboardEntry(3, DashboardEntry.TYPE_ANNOUNCEMENT, group, "Чехов А. П.", "Собрание", new Date()));
                break;
        }
        return entries;
    }

    private ArrayList<DashboardEntry> getVotesByGroup(Group group) {
        ArrayList<DashboardEntry> entries = new ArrayList<>();
        switch (group.getId()) {
            case 1:
                entries.add(new DashboardEntry(1, DashboardEntry.TYPE_VOTE, group, "Комлев Антон", "Новый год", new Date()));
                break;
            case 2:
                entries.add(new DashboardEntry(2, DashboardEntry.TYPE_VOTE, group, "Махин Семен", "Сбор денег", new Date()));
                entries.add(new DashboardEntry(2, DashboardEntry.TYPE_VOTE, group, "Махин Семен", "Удовлетворенность чем-то", new Date()));
                entries.add(new DashboardEntry(2, DashboardEntry.TYPE_VOTE, group, "Махин Семен", "Активность", new Date()));
                break;
            case 3:
                break;
        }
        return entries;
    }
}
