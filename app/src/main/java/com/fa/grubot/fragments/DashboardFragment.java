package com.fa.grubot.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fa.grubot.ListActivity;
import com.fa.grubot.R;
import com.fa.grubot.abstractions.DashboardFragmentBase;
import com.fa.grubot.presenters.DashboardPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.Nullable;

public class DashboardFragment extends Fragment implements DashboardFragmentBase {

    @Nullable @BindView(R.id.retryBtn) Button retryBtn;

    @Nullable @BindView(R.id.toolbar) Toolbar toolbar;

    @Nullable @BindView(R.id.announcementsView) CardView announcementsView;
    @Nullable @BindView(R.id.votesView) CardView votesView;
    @Nullable @BindView(R.id.chatsView) CardView chatsView;
    @Nullable @BindView(R.id.settingsView) CardView settingsView;

    private Unbinder unbinder;
    private DashboardPresenter presenter;
    private int layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new DashboardPresenter(this);
        presenter.notifyFragmentStarted(getActivity());

        View v = inflater.inflate(layout, container, false);
        setRetainInstance(true);

        unbinder = ButterKnife.bind(this, v);
        presenter.notifyViewCreated(layout, v);

        return v;
    }

    public void setupLayouts(boolean isNetworkAvailable) {
        if (isNetworkAvailable)
            layout = R.layout.fragment_dashboard;
        else
            layout = R.layout.fragment_no_internet_connection;
    }

    public void setupViews() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Главная страница");

        votesView.setOnClickListener(view -> {
            Intent intent = new Intent(this.getActivity(), ListActivity.class);
            intent.putExtra("type", DashboardSpecificFragment.TYPE_VOTES);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
        });

        announcementsView.setOnClickListener(view -> {
            Intent intent = new Intent(this.getActivity(), ListActivity.class);
            intent.putExtra("type", DashboardSpecificFragment.TYPE_ANNOUNCEMENTS);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
        });

        chatsView.setOnClickListener(view -> {
            Intent intent = new Intent(this.getActivity(), ListActivity.class);
            intent.putExtra("type", 0);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
        });
    }

    public void setupRetryButton() {
        retryBtn.setOnClickListener(view -> presenter.onRetryBtnClick());
    }

    public void reloadFragment() {
        Fragment currentFragment = this;
        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
        fragTransaction.detach(currentFragment);
        fragTransaction.attach(currentFragment);
        fragTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.destroy();
    }
}
