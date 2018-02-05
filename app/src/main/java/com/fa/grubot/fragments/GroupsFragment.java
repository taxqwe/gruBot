package com.fa.grubot.fragments;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;

import com.fa.grubot.App;
import com.fa.grubot.MainActivity;
import com.fa.grubot.R;
import com.fa.grubot.abstractions.GroupsFragmentBase;
import com.fa.grubot.adapters.GroupsRecyclerAdapter;
import com.fa.grubot.objects.group.Group;
import com.fa.grubot.presenters.GroupsPresenter;
import com.fa.grubot.util.Globals;
import com.google.firebase.firestore.DocumentChange;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class GroupsFragment extends BaseFragment implements GroupsFragmentBase, Serializable {

    @Nullable @BindView(R.id.recycler) RecyclerView groupsView;
    @Nullable @BindView(R.id.retryBtn) Button retryBtn;

    @Nullable @BindView(R.id.progressBar) ProgressBar progressBar;
    @Nullable @BindView(R.id.content) View content;
    @Nullable @BindView(R.id.noInternet) View noInternet;
    @Nullable @BindView(R.id.noData) View noData;

    private Unbinder unbinder;
    private GroupsPresenter presenter;
    private GroupsRecyclerAdapter groupsAdapter;

    private int state;
    private int instance = 0;

    public static GroupsFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("instance", instance);
        GroupsFragment fragment = new GroupsFragment();
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
        presenter = new GroupsPresenter(this);
        View v = inflater.inflate(R.layout.fragment_groups, container, false);

        setHasOptionsMenu(true);
        unbinder = ButterKnife.bind(this, v);
        instance = this.getArguments().getInt("instance");

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
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
        if (groupsAdapter != null)
            groupsAdapter.clearItems();
    }

    public void showRequiredViews() {
        progressBar.setVisibility(View.GONE);
        noInternet.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
        content.setVisibility(View.GONE);

        switch (state) {
            case Globals.FragmentState.STATE_CONTENT:
                content.setVisibility(View.VISIBLE);
                break;
            case Globals.FragmentState.STATE_NO_INTERNET_CONNECTION:
                noInternet.setVisibility(View.VISIBLE);
                break;
            case Globals.FragmentState.STATE_NO_DATA:
                noData.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setupLayouts(boolean isNetworkAvailable, boolean isHasData) {
        if (isNetworkAvailable) {
            if (isHasData)
                state = Globals.FragmentState.STATE_CONTENT;
            else {
                state = Globals.FragmentState.STATE_NO_DATA;
                groupsAdapter = null;
            }
        }
        else {
            state = Globals.FragmentState.STATE_NO_INTERNET_CONNECTION;
            groupsAdapter = null;
        }
    }

    public void setupToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("Чаты");

        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    public void setupRecyclerView(ArrayList<Group> groups) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        groupsView.setLayoutManager(mLayoutManager);
        groupsView.setHasFixedSize(false);

        if (groupsView.getItemDecorationCount() == 0) {
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                    this.getActivity(),
                    mLayoutManager.getOrientation()
            );

            groupsView.addItemDecoration(dividerItemDecoration);
        }

        if (App.INSTANCE.areAnimationsEnabled())
            groupsView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_from_right));

        groupsAdapter = new GroupsRecyclerAdapter(getActivity(), instance, fragmentNavigation, groups);
        groupsView.setAdapter(groupsAdapter);
        groupsAdapter.notifyDataSetChanged();
    }

    public void setupRetryButton() {
        retryBtn.setOnClickListener(view -> presenter.onRetryBtnClick());
    }

    public void handleListUpdate(DocumentChange.Type type, int newIndex, int oldIndex, Group group) {
        if (groupsAdapter != null) {
            switch (type) {
                case ADDED:
                    groupsAdapter.addItem(newIndex, group);
                    break;
                case MODIFIED:
                    groupsAdapter.updateItem(oldIndex, newIndex, group);
                    break;
                case REMOVED:
                    groupsAdapter.removeItem(oldIndex);
                    break;
            }
        }
    }

    public boolean isListEmpty() {
        return groupsAdapter == null || groupsAdapter.getItemCount() == 0;
    }

    public boolean isAdapterExists() {
        return groupsAdapter != null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        groupsAdapter = null;
        unbinder.unbind();
        presenter.destroy();
    }
}
