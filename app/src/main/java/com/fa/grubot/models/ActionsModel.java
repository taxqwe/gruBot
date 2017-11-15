package com.fa.grubot.models;

import android.content.Context;

import com.fa.grubot.fragments.ActionsFragment;
import com.fa.grubot.objects.dashboard.Action;
import com.fa.grubot.objects.dashboard.ActionAnnouncement;
import com.fa.grubot.objects.dashboard.ActionVote;
import com.fa.grubot.objects.group.Group;
import com.fa.grubot.objects.misc.VoteOption;
import com.fa.grubot.util.Globals;
import com.fa.grubot.util.TemporaryDataHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ActionsModel {

    public ActionsModel(){

    }
    public ArrayList<Action> loadActions(int type) {
        return TemporaryDataHelper.getActionsByType(type);
    }

    public boolean isNetworkAvailable(Context context){
        return Globals.InternetMethods.isNetworkAvailable(context);
    }
}
