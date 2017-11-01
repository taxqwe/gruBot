package com.fa.grubot.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fa.grubot.R;
import com.fa.grubot.abstractions.AnnouncementFragmentBase;
import com.fa.grubot.abstractions.GroupsFragmentBase;
import com.fa.grubot.adapters.GroupsRecyclerAdapter;
import com.fa.grubot.objects.Group;
import com.fa.grubot.presenters.GroupsPresenter;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AnnouncementFragment extends Fragment implements AnnouncementFragmentBase {

    private SwipeRefreshLayout swipeRefreshLayout;
    private Button retryBtn;

    private Unbinder unbinder;
    private GroupsPresenter presenter;
    private int layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //presenter = new GroupsPresenter(this);
        presenter.notifyFragmentStarted(getActivity());

        View v = inflater.inflate(layout, container, false);
        setRetainInstance(true);

        unbinder = ButterKnife.bind(this, v);
        presenter.notifyViewCreated(layout, v);

        return v;
    }

    public void setupLayouts(boolean isNetworkAvailable, boolean isHasData){
        if (isNetworkAvailable)
            layout = R.layout.fragment_announcement;
        else
            layout = R.layout.fragment_no_internet_connection;
    }

    public void setupViews(int layout, View v){
        switch (layout) {
            case R.layout.fragment_announcement:
                //groupsView = v.findViewById(R.id.recycler);
                swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
                break;
            case R.layout.fragment_no_internet_connection:
                retryBtn = v.findViewById(R.id.retryBtn);
                break;
            case R.layout.fragment_no_data:
                swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
                break;
        }
    }

    public void setupSwipeRefreshLayout(int layout){
        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.updateView(layout, getActivity());
            onItemsLoadComplete();
        });
    }


    public void setupRetryButton(){
        retryBtn.setOnClickListener(view -> presenter.onRetryBtnClick());
    }

    public void reloadFragment(){
        Fragment currentFragment = this;
        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
        fragTransaction.detach(currentFragment);
        fragTransaction.attach(currentFragment);
        fragTransaction.commit();
    }

    private void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }

    public static AnnouncementFragment newInstance() {
        return new AnnouncementFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.destroy();
    }
}
