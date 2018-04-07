package com.fa.grubot.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fa.grubot.App;
import com.fa.grubot.R;
import com.fa.grubot.objects.pojos.VkUserResponseWithPhoto;
import com.fa.grubot.presenters.ProfilePresenter;
import com.fa.grubot.util.Consts;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ni.petrov on 03/04/2018.
 */

public class ProfileItemFragment extends Fragment {
    private String type;

    @BindView(R.id.btn_profile_exit)
    Button mExitBtn;

    @BindView(R.id.image_profile)
    ImageView mImage;

    @BindView(R.id.name_profile)
    EditText mNameEditText;

    @BindView(R.id.name_profile_layout)
    TextInputLayout mNameEditTextLayout;

    @BindView(R.id.progressBar_profile)
    View mProgressBar;

    private ProfilePresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_profile, container, false);
        ButterKnife.bind(this, v);

        type = getArguments().getString("type");

        mPresenter = new ProfilePresenter(this);

        init();

        return v;
    }

    private void init() {
        if (checkIfCurrentTypeLoginned()){
            showProgressBar(true);
            if (type.equals(Consts.VK)){
                doVkStuff();
            } else if (type.equals(Consts.Telegram)){
                doTelegramStuff();
            }
        }
    }

    private void doTelegramStuff() {
        // todo
    }

    private void doVkStuff() {
        mPresenter.askForVkStuff();
    }

    private boolean checkIfCurrentTypeLoginned() {
        if ((type.equals(Consts.VK) && !App.INSTANCE.getCurrentUser().hasVkUser()) ||
                (type.equals(Consts.Telegram) && !App.INSTANCE.getCurrentUser().hasTelegramUser())){
            showNotLoginnedMessage();
            return false;
        }
        return true;
    }

    private void showNotLoginnedMessage() {
        mExitBtn.setVisibility(View.GONE);
        mImage.setVisibility(View.INVISIBLE);
        mNameEditText.setText("Вход не выполнен");
        mNameEditTextLayout.setError(null);
    }

    private void showProgressBar(boolean needProgressBar) {
        mExitBtn.setVisibility(needProgressBar ? View.GONE : View.VISIBLE);
        mImage.setVisibility(needProgressBar ? View.GONE : View.VISIBLE);
        mNameEditText.setVisibility(needProgressBar ? View.GONE : View.VISIBLE);
        mProgressBar.setVisibility(needProgressBar ? View.VISIBLE : View.GONE);
    }

    public void drawVkUser(VkUserResponseWithPhoto userVk) {
        showProgressBar(false);
        Glide.with(this).load(userVk.getPhoto100()).apply(RequestOptions.circleCropTransform()).into(mImage);
        mNameEditText.setText(userVk.getFirstName() + " " + userVk.getLastName());
        mNameEditTextLayout.setError("Имя пользователя");
    }
}
