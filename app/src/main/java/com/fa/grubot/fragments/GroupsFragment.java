package com.fa.grubot.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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

import com.fa.grubot.MainActivity;
import com.fa.grubot.R;
import com.fa.grubot.abstractions.GroupsFragmentBase;
import com.fa.grubot.adapters.GroupsRecyclerAdapter;
import com.fa.grubot.objects.group.Group;
import com.fa.grubot.presenters.GroupsPresenter;
import com.fa.grubot.util.Globals;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;
import io.reactivex.annotations.Nullable;

public class GroupsFragment extends Fragment implements GroupsFragmentBase, Serializable {

    @Nullable @BindView(R.id.recycler) transient RecyclerView groupsView;
    @Nullable @BindView(R.id.swipeRefreshLayout) transient SwipeRefreshLayout swipeRefreshLayout;
    @Nullable @BindView(R.id.retryBtn) transient Button retryBtn;

    @Nullable @BindView(R.id.progressBar) transient ProgressBar progressBar;
    @Nullable @BindView(R.id.content) transient View content;
    @Nullable @BindView(R.id.noInternet) transient View noInternet;
    @Nullable @BindView(R.id.noData) transient View noData;

    private transient Unbinder unbinder;
    private transient GroupsPresenter presenter;

    private int state;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new GroupsPresenter(this);
        View v = inflater.inflate(R.layout.fragment_groups, container, false);

        presenter.notifyFragmentStarted(getActivity());
        setHasOptionsMenu(true);

        unbinder = ButterKnife.bind(this, v);
        presenter.notifyViewCreated(state);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    public void setupViews() {
        new Handler().postDelayed(() -> {
            progressBar.setVisibility(View.GONE);

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
        }, Globals.Variables.delayTime);
    }

    public void setupLayouts(boolean isNetworkAvailable, boolean isHasData) {
        if (isNetworkAvailable) {
            if (isHasData)
                state = Globals.FragmentState.STATE_CONTENT;
            else
                state = Globals.FragmentState.STATE_NO_DATA;
        }
        else
            state = Globals.FragmentState.STATE_NO_INTERNET_CONNECTION;
    }

    public void setupToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("Чаты");

        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    public void setupSwipeRefreshLayout(int state) {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.updateView(state, getActivity());
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

        if (Globals.Variables.areAnimationsEnabled)
            groupsView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_from_right));

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
