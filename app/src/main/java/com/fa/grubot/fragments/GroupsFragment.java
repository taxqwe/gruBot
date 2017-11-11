package com.fa.grubot.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fa.grubot.R;
import com.fa.grubot.abstractions.GroupsFragmentBase;
import com.fa.grubot.adapters.GroupsRecyclerAdapter;
import com.fa.grubot.objects.group.Group;
import com.fa.grubot.presenters.GroupsPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.Nullable;

public class GroupsFragment extends Fragment implements GroupsFragmentBase{

    @Nullable @BindView(R.id.toolbar) Toolbar toolbar;
    @Nullable @BindView(R.id.recycler) RecyclerView groupsView;
    @Nullable @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @Nullable @BindView(R.id.retryBtn) Button retryBtn;

    private Unbinder unbinder;
    private GroupsPresenter presenter;
    private int layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new GroupsPresenter(this);
        presenter.notifyFragmentStarted(getActivity());
        setHasOptionsMenu(true);

        View v = inflater.inflate(layout, container, false);
        setRetainInstance(true);

        unbinder = ButterKnife.bind(this, v);
        presenter.notifyViewCreated(layout, v);

        return v;
    }

    public void setupLayouts(boolean isNetworkAvailable, boolean isHasData) {
        if (isNetworkAvailable) {
            if (isHasData)
                layout = R.layout.fragment_groups;
            else
                layout = R.layout.fragment_no_data;
        }
        else
            layout = R.layout.fragment_no_internet_connection;
    }

    public void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Чаты");
    }

    public void setupSwipeRefreshLayout(int layout) {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.updateView(layout, getActivity());
            onItemsLoadComplete();
        });
    }

    public void setupRecyclerView(ArrayList<Group> groups) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        groupsView.setLayoutManager(mLayoutManager);
        groupsView.setHasFixedSize(false);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration (
                this.getActivity(),
                mLayoutManager.getOrientation()
        );
        groupsView.addItemDecoration(dividerItemDecoration);

        GroupsRecyclerAdapter groupsAdapter = new GroupsRecyclerAdapter(getActivity(), groups);
        groupsView.setAdapter(groupsAdapter);
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

    private void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.destroy();
    }
}
