package com.fa.grubot.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fa.grubot.R;
import com.fa.grubot.adapters.DashboardRecyclerAdapter;
import com.fa.grubot.objects.DashboardEntry;
import com.fa.grubot.presenters.DashboardPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DashboardFragment extends Fragment {
    private Unbinder unbinder;
    private DashboardPresenter presenter;

    @BindView(R.id.recycler)RecyclerView groupsView;
    @BindView(R.id.swipeRefreshLayout)SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_groups, container, false);
        unbinder = ButterKnife.bind(this, v);
        presenter = new DashboardPresenter(this);

        presenter.notifyViewCreated();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.updateDashboardRecyclerView();
            onItemsLoadComplete();
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.destroy();
    }

    public void setupRecyclerView(ArrayList<DashboardEntry> entries){
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        groupsView.setLayoutManager(mLayoutManager);
        groupsView.setHasFixedSize(false);

        DashboardRecyclerAdapter dashboardAdapter = new DashboardRecyclerAdapter(getActivity(), entries);
        groupsView.setAdapter(dashboardAdapter);
        dashboardAdapter.notifyDataSetChanged();
    }

    private void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }

    public static DashboardFragment newInstance() {
        DashboardFragment f = new DashboardFragment();
        return f;
    }
}
