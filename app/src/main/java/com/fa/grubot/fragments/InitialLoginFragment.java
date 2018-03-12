package com.fa.grubot.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fa.grubot.R;
import com.fa.grubot.abstractions.InitialLoginFragmentBase;
import com.fa.grubot.presenters.InitialLoginPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class InitialLoginFragment extends Fragment implements InitialLoginFragmentBase {
    @Nullable @BindView(R.id.vkImage) ImageView vkBtn;
    @Nullable @BindView(R.id.telegramImage) ImageView telegramBtn;

    private InitialLoginPresenter presenter;
    private Unbinder unbinder;

    public static InitialLoginFragment newInstance() {
        Bundle args = new Bundle();
        InitialLoginFragment fragment = new InitialLoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new InitialLoginPresenter(this);
        View v = inflater.inflate(R.layout.fragment_initial_login, container, false);

        unbinder = ButterKnife.bind(this, v);

        presenter.notifyFragmentStarted();
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.destroy();
    }

    public void setupViews() {
        telegramBtn.setOnClickListener(v -> {
            Fragment telegramLoginFragment = TelegramLoginFragment.newInstance();

            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.content, telegramLoginFragment);
            transaction.commit();
        });

        vkBtn.setOnClickListener(v -> {

        });
    }
}
