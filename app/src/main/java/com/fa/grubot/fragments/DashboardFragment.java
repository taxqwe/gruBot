package com.fa.grubot.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.fa.grubot.MainActivity;
import com.fa.grubot.R;
import com.fa.grubot.abstractions.DashboardFragmentBase;
import com.fa.grubot.adapters.DashboardRecyclerAdapter;
import com.fa.grubot.objects.dashboard.DashboardItem;
import com.fa.grubot.presenters.DashboardPresenter;
import com.fa.grubot.util.Globals;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;
import io.reactivex.annotations.Nullable;

public class DashboardFragment extends Fragment implements DashboardFragmentBase, Serializable {

    @Nullable @BindView(R.id.retryBtn) transient Button retryBtn;
    @Nullable @BindView(R.id.recycler) transient RecyclerView dashboardView;

    @Nullable @BindView(R.id.progressBar) transient ProgressBar progressBar;
    @Nullable @BindView(R.id.content) transient View content;
    @Nullable @BindView(R.id.noInternet) transient View noInternet;

    private transient Unbinder unbinder;
    private transient DashboardPresenter presenter;
    private transient DashboardRecyclerAdapter dashboardAdapter;

    private int state;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new DashboardPresenter(this);
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        unbinder = ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        dashboardAdapter = null;
        presenter.notifyFragmentStarted();
    }

    @Override
    public void onPause() {
        super.onPause();
        terminateRegistration();
    }

    @Override
    public void onStop() {
        super.onStop();
        terminateRegistration();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    private void terminateRegistration() {
        presenter.removeRegistration();
        dashboardAdapter.clearItems();
    }

    public void showRequiredViews() {
        progressBar.setVisibility(View.GONE);
        noInternet.setVisibility(View.GONE);
        content.setVisibility(View.GONE);

        switch (state) {
            case Globals.FragmentState.STATE_CONTENT:
                content.setVisibility(View.VISIBLE);
                break;
            case Globals.FragmentState.STATE_NO_INTERNET_CONNECTION:
                noInternet.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setupLayouts(boolean isNetworkAvailable) {
        if (isNetworkAvailable)
            state = Globals.FragmentState.STATE_CONTENT;
        else {
            state = Globals.FragmentState.STATE_NO_INTERNET_CONNECTION;
            dashboardAdapter = null;
        }
    }

    public void setupToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("Доска");

        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    public void setupRecyclerView(ArrayList<DashboardItem> items) {
        int spanCount = 1;

        if (getActivity().getResources().getConfiguration().orientation == 2)
            spanCount = 2;

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
        dashboardView.setLayoutManager(layoutManager);
        dashboardView.setHasFixedSize(false);

        dashboardAdapter = new DashboardRecyclerAdapter(getActivity(), items);
        dashboardView.setAdapter(dashboardAdapter);
        dashboardAdapter.notifyDataSetChanged();
    }

    public void setupRetryButton() {
        retryBtn.setOnClickListener(view -> presenter.onRetryBtnClick());
    }

    public void handleListUpdate(int count, int type) {
        if (dashboardAdapter != null) {
            dashboardAdapter.updateItem(count, type);
        }
    }

    public boolean isAdapterExists() {
        return dashboardAdapter != null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dashboardAdapter = null;
        unbinder.unbind();
        presenter.destroy();
    }
}
