package com.fa.grubot.util;

import com.fa.grubot.fragments.ActionsFragment;
import com.fa.grubot.objects.dashboard.Action;
import com.fa.grubot.objects.dashboard.ActionAnnouncement;
import com.fa.grubot.objects.dashboard.ActionVote;
import com.fa.grubot.objects.group.Group;
import com.fa.grubot.objects.group.User;
import com.fa.grubot.objects.misc.VoteOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class TemporaryDataHelper {
    private static final String placeholder = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna " +
            "aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
            "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint " +
            "occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";
    private static final ArrayList<VoteOption> options = new ArrayList<>(Arrays.asList(new VoteOption("Первый вариант"), new VoteOption("Второй вариант"), new VoteOption("Третий вариант")));

    private static ArrayList<Action> announcementsList = new ArrayList<>();
    private static ArrayList<Action> votesList = new ArrayList<>();
    private static ArrayList<User> usersList = new ArrayList<>();
    private static ArrayList<Group> groupsList = new ArrayList<>();

    public static void setLists() {
        setUsers();
        setGroups();
        setActions();
    }

    public static ArrayList<Action> getActionsByType(int type) {
        if (type == ActionsFragment.TYPE_ANNOUNCEMENTS) {
            return announcementsList;
        } else {
            return votesList;
        }
    }

    public static ArrayList<Action> getActionsByGroupAndType(int type, Group group) {
        ArrayList<Action> actions = new ArrayList<>();

        if (type == ActionsFragment.TYPE_ANNOUNCEMENTS) {
            for (Action action : announcementsList) {
                if (action.getGroup().equals(group))
                    actions.add(action);
            }
            return actions;
        } else {
            for (Action action : votesList) {
                if (action.getGroup().equals(group))
                    actions.add(action);
            }
            return actions;
        }
    }

    public static void addNewActionByType(int type, Action action) {
        if (type == ActionsFragment.TYPE_ANNOUNCEMENTS) {
            announcementsList.add(action);
        } else {
            votesList.add(action);
        }
    }

    public static ArrayList<User> getUsersByGroup(Group group) {
        return group.getUsers();
    }

    public static ArrayList<Group> getGroups() {
        return groupsList;
    }

    private static void setActions() {
        announcementsList.add(new ActionAnnouncement(1, groupsList.get(0), "Комлев Антон", "Собрание", new Date(),  placeholder));
        announcementsList.add(new ActionAnnouncement(1, groupsList.get(0), "Комлев Антон", "Выходные дни", new Date(), placeholder));
        announcementsList.add(new ActionAnnouncement(3, groupsList.get(2), "Чехов А. П.", "Поездка", new Date(), placeholder));
        announcementsList.add(new ActionAnnouncement(3, groupsList.get(2), "Чехов А. П.", "Объявление", new Date(), placeholder));
        announcementsList.add(new ActionAnnouncement(3, groupsList.get(2), "Чехов А. П.", "Собрание", new Date(), placeholder));

        votesList.add(new ActionVote(1, groupsList.get(0), "Комлев Антон", "Новый год", new Date(), options));
        votesList.add(new ActionVote(2, groupsList.get(1), "Махин Семен", "Сбор денег", new Date(), options));
        votesList.add(new ActionVote(2, groupsList.get(1), "Махин Семен", "Удовлетворенность чем-то", new Date(), options));
        votesList.add(new ActionVote(2, groupsList.get(1), "Махин Семен", "Активность", new Date(), options));
    }

    private static void setUsers() {
        usersList.add(new User(1, "pussyStealer", "Антон Комлев", "7(903)869-14-82", "Кружка", null));
        usersList.add(new User(2, "actuallyStalin", "Петров Николай", "7(903)322-14-88", "OHHHHHHHHHHHHHHHHHHHHHHHH", null));
        usersList.add(new User(3, "dip", "Прахов Владислав", "7(903)869-22-77", "123", null));
        usersList.add(new User(4, "just_a_goat", "Елена Головач", "7(903)334-33-32", "TIPA OPISANIE", "http://www.goat-simulator.com/ffa11.jpg"));
        usersList.add(new User(5, "deus_vult", "Конрад Цёлльнер фон Ротенштайн", "7(903)132-81-39", "TIPA OPISANIE EWE RAZ", "https://upload.wikimedia.org/wikipedia/commons/f/fa/Wg_zoellner.gif"));
        usersList.add(new User(6, "just_a_painter", "Адольф Гитлер", "7(903)132-11-11", "TIPA TOJE", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Bundesarchiv_Bild_183-H1216-0500-002%2C_Adolf_Hitler.jpg"));
        usersList.add(new User(7, "semen", "Махин Семен", "7(903)322-14-88", "4343", null));

    }

    private static void setGroups() {
        groupsList.add(new Group(1, "ПИ4-1", new ArrayList<>(Arrays.asList(usersList.get(0), usersList.get(2), usersList.get(1))), "https://2static3.fjcdn.com/comments/Fun+fact+the+flat+topped+great+helm+is+a+piece+_3cb2af934364bbe51707d55061d6aacb.jpg"));
        groupsList.add(new Group(2, "ПИ4-2", new ArrayList<>(Arrays.asList(usersList.get(6), usersList.get(3))),null));
        groupsList.add(new Group(3, "ГРУППА НАМБА ВАН НА РУСИ", new ArrayList<>(Arrays.asList(usersList.get(5), usersList.get(4), usersList.get(3))),null));
    }
}
