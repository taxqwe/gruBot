package com.fa.grubot.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fa.grubot.R;
import com.fa.grubot.abstractions.DashboardFragmentBase;
import com.fa.grubot.adapters.DashboardRecyclerAdapter;
import com.fa.grubot.objects.dashboard.DashboardItem;
import com.fa.grubot.presenters.DashboardPresenter;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.Nullable;

public class DashboardFragment extends Fragment implements DashboardFragmentBase, Serializable {

    @Nullable @BindView(R.id.retryBtn) Button retryBtn;

    @Nullable @BindView(R.id.toolbar) transient Toolbar toolbar;
    @Nullable @BindView(R.id.recycler) transient RecyclerView dashboardView;

    private transient Unbinder unbinder;
    private transient DashboardPresenter presenter;
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
            layout = R.layout.content_no_internet_connection;
    }

    public void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Доска");
    }

    public void setupRecyclerView(ArrayList<DashboardItem> items) {
        int spanCount = 1;

        if (getActivity().getResources().getConfiguration().orientation == 2)
            spanCount = 2;

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
        dashboardView.setLayoutManager(layoutManager);
        dashboardView.setHasFixedSize(false);

        DashboardRecyclerAdapter groupsAdapter = new DashboardRecyclerAdapter(getActivity(), items);
        dashboardView.setAdapter(groupsAdapter);
        groupsAdapter.notifyDataSetChanged();
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
