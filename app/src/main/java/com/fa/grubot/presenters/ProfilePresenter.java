package com.fa.grubot.presenters;

import android.util.Log;

import com.fa.grubot.fragments.ProfileItemFragment;
import com.fa.grubot.models.ProfileModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ni.petrov on 04/04/2018.
 */

public class ProfilePresenter {
    private ProfileItemFragment view;

    private ProfileModel model;


    public ProfilePresenter(ProfileItemFragment view) {
        this.view = view;
        model = new ProfileModel(this);
    }

    public void askForVkStuff() {
        Observable.just(true).map(x -> {
            Log.d("PROFILE", "profile asks for model");
            return model.askForMyInfo();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(vkUserObs -> {
                    vkUserObs.subscribe(vkusr -> view.drawVkUser(vkusr));
                });
    }
}
