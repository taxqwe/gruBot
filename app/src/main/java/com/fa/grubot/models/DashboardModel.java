package com.fa.grubot.models;

import android.content.Context;

import com.fa.grubot.App;
import com.fa.grubot.objects.dashboard.DashboardItem;
import com.fa.grubot.util.Globals;

import java.util.ArrayList;

import io.reactivex.Observable;

public class DashboardModel {

    public DashboardModel(){

    }

    public ArrayList<DashboardItem> getItems() {
        return null;
    }

    private int getNewAnnouncementsCount() {
        return App.INSTANCE.getDataHelper().getAnnouncemtsCount();
    }

    private int getNewVotesCount() {
        return App.INSTANCE.getDataHelper().getVotesCount();
    }

    public Observable<Boolean> isNetworkAvailable(Context context) {
        return Globals.InternetMethods.getNetworkObservable(context);
    }
}
