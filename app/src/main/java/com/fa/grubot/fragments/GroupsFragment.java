package com.fa.grubot.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fa.grubot.R;
import com.fa.grubot.adapters.GroupsRecyclerAdapter;
import com.fa.grubot.objects.Group;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GroupsFragment extends Fragment{
    private Unbinder unbinder;

    @BindView(R.id.recycler)RecyclerView groupsView;
    @BindView(R.id.swipeRefreshLayout)SwipeRefreshLayout swipeRefreshLayout;

    GroupsRecyclerAdapter groupsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_groups, container, false);
        unbinder = ButterKnife.bind(this, v);

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
        ArrayList<Group> groups = new ArrayList<>();

        groups.add(new Group(1, "ПИ4-1"));
        groups.add(new Group(2, "ПИ4-2"));
        groups.add(new Group(3, "ГРУППА НАМБА ВАН НА РУСИ"));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        groupsView.setLayoutManager(mLayoutManager);
        groupsView.setHasFixedSize(false);

        groupsAdapter = new GroupsRecyclerAdapter(getActivity(), groups);
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
