package com.fa.grubot.models;

import android.content.Context;

import com.fa.grubot.objects.misc.ProfileItem;
import com.fa.grubot.objects.users.User;
import com.fa.grubot.util.Globals;

import java.util.ArrayList;

import io.reactivex.Observable;

public class ProfileModel {

    public ProfileModel(){

    }
    public ArrayList<ProfileItem> getItems(User user){
        ArrayList<ProfileItem> items = new ArrayList<>();
        items.add(new ProfileItem(user.getFullname(), "Имя"));
        items.add(new ProfileItem(user.getUserName(), "Логин"));
        items.add(new ProfileItem(user.getPhoneNumber(), "Номер телефона"));

        return items;
    }

    public Observable<Boolean> isNetworkAvailable(Context context) {
        return Globals.InternetMethods.getNetworkObservable(context);
    }
}
