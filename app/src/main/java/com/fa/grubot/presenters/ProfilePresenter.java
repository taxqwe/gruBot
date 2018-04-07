package com.fa.grubot.presenters;

import android.content.Context;
import android.util.Log;

import com.fa.grubot.abstractions.ProfileItemFragmentBase;
import com.fa.grubot.models.ProfileModel;
import com.fa.grubot.util.Consts;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ni.petrov on 04/04/2018.
 */

public class ProfilePresenter {
    private ProfileItemFragmentBase fragment;
    private ProfileModel model;

    private Context context;

    public ProfilePresenter(ProfileItemFragmentBase fragment, Context context) {
        this.fragment = fragment;
        this.context = context;
        model = new ProfileModel();
    }

    public void requestVkUser(final int userId) {
        Observable.just(true).map(x -> {
            Log.d("PROFILE", "profile asks for model");
            return model.askForVkUserInfo(userId);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(vkUserObs -> {
                    vkUserObs.subscribe(vkusr -> fragment.showVkUser(vkusr));
                });
    }

    public void requestTelegramUser(final int userId) {
        Observable.just(userId)
            .map(id -> model.askForTelegramUserInfo(id, context))
            .filter(user -> user != null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(user -> fragment.showTelegramUser(user))
            .subscribe();
    }
}
