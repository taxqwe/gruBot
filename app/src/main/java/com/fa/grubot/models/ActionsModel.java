package com.fa.grubot.models;

import android.content.Context;

import com.fa.grubot.fragments.ActionsFragment;
import com.fa.grubot.objects.dashboard.Action;
import com.fa.grubot.objects.dashboard.ActionAnnouncement;
import com.fa.grubot.objects.dashboard.ActionVote;
import com.fa.grubot.objects.group.Group;
import com.fa.grubot.objects.misc.VoteOption;
import com.fa.grubot.util.Globals;

import java.util.ArrayList;
import java.util.Date;

public class ActionsModel {

    public ActionsModel(){

    }
    public ArrayList<Action> loadActions(int type){
        ArrayList<Action> actions = new ArrayList<>();

        String placeholder = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna " +
                "aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint " +
                "occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";


        if (type == ActionsFragment.TYPE_ANNOUNCEMENTS) {
            actions.add(new ActionAnnouncement(1, new Group(1, "ПИ4-1", null), "Комлев Антон", "Собрание", new Date(), placeholder));
            actions.add(new ActionAnnouncement(1, new Group(1, "ПИ4-1", null), "Комлев Антон", "Выходные дни", new Date(), placeholder));
            actions.add(new ActionAnnouncement(3, new Group(3, "ГРУППА НАМБА ВАН НА РУСИ", null), "Чехов А. П.", "Поездка", new Date(), placeholder));
            actions.add(new ActionAnnouncement(3, new Group(3, "ГРУППА НАМБА ВАН НА РУСИ", null), "Чехов А. П.", "Объявление", new Date(), placeholder));
            actions.add(new ActionAnnouncement(3, new Group(3, "ГРУППА НАМБА ВАН НА РУСИ", null), "Чехов А. П.", "Собрание", new Date(), placeholder));
        } else {
            actions.add(new ActionVote(1, new Group(1, "ПИ4-1", null), "Комлев Антон", "Новый год", new Date(), new ArrayList<VoteOption>()));

            actions.add(new ActionVote(2, new Group(2, "ПИ4-2", null), "Махин Семен", "Сбор денег", new Date(), new ArrayList<VoteOption>()));
            actions.add(new ActionVote(2, new Group(2, "ПИ4-2", null), "Махин Семен", "Удовлетворенность чем-то", new Date(), new ArrayList<VoteOption>()));
            actions.add(new ActionVote(2, new Group(2, "ПИ4-2", null), "Махин Семен", "Активность", new Date(), new ArrayList<VoteOption>()));

        }

        return actions;
    }

    public boolean isNetworkAvailable(Context context){
        return Globals.InternetMethods.isNetworkAvailable(context);
    }
}
