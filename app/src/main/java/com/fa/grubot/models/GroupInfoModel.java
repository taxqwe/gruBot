package com.fa.grubot.models;

import com.fa.grubot.adapters.GroupInfoRecyclerAdapter;
import com.fa.grubot.objects.dashboard.Action;
import com.fa.grubot.objects.dashboard.ActionAnnouncement;
import com.fa.grubot.objects.dashboard.ActionVote;
import com.fa.grubot.objects.group.Group;
import com.fa.grubot.objects.group.GroupInfoButton;
import com.fa.grubot.objects.group.User;
import com.fa.grubot.objects.misc.VoteOption;

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

        //Объявления
        ArrayList<Action> entries = getAnnouncementsByGroup(group);
        ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> items = new ArrayList<>();
        for (Action entry : entries) {
            items.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(entry));
        }
        groupInfoRecyclerItems.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(new GroupInfoButton(2, "Объявления", items)));
        groupInfoRecyclerItems.addAll(items);

        //Голосования
        entries = getVotesByGroup(group);
        items = new ArrayList<>();
        for (Action entry : entries) {
            items.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(entry));
        }
        groupInfoRecyclerItems.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(new GroupInfoButton(3, "Голосования", items)));
        groupInfoRecyclerItems.addAll(items);

        //Пользователи
        ArrayList<User> users = getUsersByGroup(group);
        items = new ArrayList<>();
        for (User user : users) {
            items.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(user));
        }
        groupInfoRecyclerItems.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(new GroupInfoButton(4, "Список участников", items)));
        groupInfoRecyclerItems.addAll(items);

        return groupInfoRecyclerItems;
    }

    private ArrayList<Action> getAnnouncementsByGroup(Group group) {
        ArrayList<Action> entries = new ArrayList<>();

        switch (group.getId()) {
            case 1:
                entries.add(new ActionAnnouncement(1, new Group(1, "ПИ4-1", null), "Комлев Антон", "Собрание", new Date(),  placeholder));
                entries.add(new ActionAnnouncement(1, new Group(1, "ПИ4-1", null), "Комлев Антон", "Выходные дни", new Date(), placeholder));
                break;
            case 2:
                break;
            case 3:
                entries.add(new ActionAnnouncement(3, new Group(3, "ГРУППА НАМБА ВАН НА РУСИ", null), "Чехов А. П.", "Поездка", new Date(), placeholder));
                entries.add(new ActionAnnouncement(3, new Group(3, "ГРУППА НАМБА ВАН НА РУСИ", null), "Чехов А. П.", "Объявление", new Date(), placeholder));
                entries.add(new ActionAnnouncement(3, new Group(3, "ГРУППА НАМБА ВАН НА РУСИ", null), "Чехов А. П.", "Собрание", new Date(), placeholder));
                break;
        }
        return entries;
    }

    private ArrayList<Action> getVotesByGroup(Group group) {
        ArrayList<Action> entries = new ArrayList<>();
        switch (group.getId()) {
            case 1:
                entries.add(new ActionVote(1, new Group(1, "ПИ4-1", null), "Комлев Антон", "Новый год", new Date(), new ArrayList<VoteOption>()));
                break;
            case 2:
                entries.add(new ActionVote(2, new Group(2, "ПИ4-2", null), "Махин Семен", "Сбор денег", new Date(), new ArrayList<VoteOption>()));
                entries.add(new ActionVote(2, new Group(2, "ПИ4-2", null), "Махин Семен", "Удовлетворенность чем-то", new Date(), new ArrayList<VoteOption>()));
                entries.add(new ActionVote(2, new Group(2, "ПИ4-2", null), "Махин Семен", "Активность", new Date(), new ArrayList<VoteOption>()));
                break;
            case 3:
                break;
        }
        return entries;
    }

    private ArrayList<User> getUsersByGroup(Group group) {
        ArrayList<User> users = new ArrayList<>();
        switch (group.getId()) {
            case 1:
                users.add(new User(1, "pussyStealer", "Антон Комлев", "7(903)869-14-82", "Кружка"));
                users.add(new User(2, "actuallyStalin", "Петров Николай", "7(903)322-14-88", "OHHHHHHHHHHHHHHHHHHHHHHHH"));
                users.add(new User(3, "dip", "Прахов Владислав", "7(903)869-22-77", "123"));
                break;
            case 2:
                users.add(new User(1, "pussyStealer", "Антон Комлев", "7(903)869-14-82", "Кружка"));
                users.add(new User(2, "actuallyStalin", "Петров Николай", "7(903)322-14-88", "OHHHHHHHHHHHHHHHHHHHHHHHH"));
                break;
            case 3:
                users.add(new User(1, "pussyStealer", "Антон Комлев", "7(903)869-14-82", "Кружка"));
                users.add(new User(3, "dip", "Прахов Владислав", "7(903)869-22-77", "123"));
                break;
        }
        return users;
    }
}
