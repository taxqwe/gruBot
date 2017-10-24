package com.fa.grubot.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fa.grubot.R;
import com.fa.grubot.adapters.GroupsRecyclerAdapter;
import com.fa.grubot.presenters.GroupsPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GroupsFragment extends Fragment{
    private Unbinder unbinder;
    private GroupsPresenter presenter;

    @BindView(R.id.recycler)RecyclerView groupsView;
    @BindView(R.id.swipeRefreshLayout)SwipeRefreshLayout swipeRefreshLayout;

    private GroupsRecyclerAdapter groupsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_groups, container, false);
        unbinder = ButterKnife.bind(this, v);
        presenter = new GroupsPresenter(this);

        setupRecyclerView();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshItems();
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setupRecyclerView(){
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        groupsView.setLayoutManager(mLayoutManager);
        groupsView.setHasFixedSize(false);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                this.getContext(),
                mLayoutManager.getOrientation()
        );
        groupsView.addItemDecoration(dividerItemDecoration);



        groupsAdapter = new GroupsRecyclerAdapter(getActivity(), presenter.getGroups());
        groupsView.setAdapter(groupsAdapter);
        groupsAdapter.notifyDataSetChanged();
    }


    private void refreshItems() {
        setupRecyclerView();
        onItemsLoadComplete();
    }

    private void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }

    public static GroupsFragment newInstance() {
        GroupsFragment f = new GroupsFragment();
        return f;
    }
}
