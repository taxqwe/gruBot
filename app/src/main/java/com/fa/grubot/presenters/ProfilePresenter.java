package com.fa.grubot.presenters;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.fa.grubot.App;
import com.fa.grubot.abstractions.ProfileItemFragmentBase;
import com.fa.grubot.models.ProfileModel;
import com.fa.grubot.objects.users.User;
import com.fa.grubot.util.Consts;
import com.github.badoualy.telegram.tl.exception.RpcErrorException;

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
                    vkUserObs.subscribe(vkUser -> fragment.showUser(new User(String.valueOf(vkUser.getId()), Consts.VK, vkUser.getFirstName() + " " + vkUser.getLastName(), vkUser.getDomain(), vkUser.getPhoto100())));
                });
    }

    public void requestTelegramUser(final int userId) {
        Observable.defer(() -> Observable.just(model.askForTelegramUserInfo(userId, context)))
                .filter(user -> user != null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(user -> fragment.showUser(user))
                .subscribe();
    }
}
