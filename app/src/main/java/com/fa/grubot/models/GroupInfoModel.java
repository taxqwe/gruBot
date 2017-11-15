package com.fa.grubot.models;

import android.content.Context;

import com.fa.grubot.adapters.GroupInfoRecyclerAdapter;
import com.fa.grubot.fragments.ActionsFragment;
import com.fa.grubot.objects.dashboard.Action;
import com.fa.grubot.objects.group.Group;
import com.fa.grubot.objects.group.GroupInfoButton;
import com.fa.grubot.objects.group.User;
import com.fa.grubot.util.Globals;
import com.fa.grubot.helpers.TemporaryDataHelper;

import java.util.ArrayList;

public class GroupInfoModel {
    public GroupInfoModel(){

    }

    public ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> loadButtons(Group group){
        ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> groupInfoRecyclerItems = new ArrayList<>();

        groupInfoRecyclerItems.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(new GroupInfoButton(1, "Чат", new ArrayList<>())));

        //Объявления
        ArrayList<Action> entries = TemporaryDataHelper.getActionsByGroupAndType(ActionsFragment.TYPE_ANNOUNCEMENTS, group);
        ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> items = new ArrayList<>();
        for (Action entry : entries) {
            items.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(entry));
        }
        groupInfoRecyclerItems.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(new GroupInfoButton(2, "Объявления", items)));
        groupInfoRecyclerItems.addAll(items);

        //Голосования
        entries = TemporaryDataHelper.getActionsByGroupAndType(ActionsFragment.TYPE_VOTES, group);
        items = new ArrayList<>();
        for (Action entry : entries) {
            items.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(entry));
        }
        groupInfoRecyclerItems.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(new GroupInfoButton(3, "Голосования", items)));
        groupInfoRecyclerItems.addAll(items);

        //Пользователи
        ArrayList<User> users = TemporaryDataHelper.getUsersByGroup(group);
        items = new ArrayList<>();
        for (User user : users) {
            items.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(user));
        }
        groupInfoRecyclerItems.add(new GroupInfoRecyclerAdapter.GroupInfoRecyclerItem(new GroupInfoButton(4, "Список участников", items)));
        groupInfoRecyclerItems.addAll(items);

        return groupInfoRecyclerItems;
    }

    public boolean isNetworkAvailable(Context context){
        return Globals.InternetMethods.isNetworkAvailable(context);
    }
}
