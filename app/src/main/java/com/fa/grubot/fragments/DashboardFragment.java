package com.fa.grubot.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fa.grubot.R;
import com.fa.grubot.abstractions.DashboardFragmentBase;
import com.fa.grubot.adapters.DashboardRecyclerAdapter;
import com.fa.grubot.objects.DashboardEntry;
import com.fa.grubot.presenters.DashboardPresenter;
import com.fa.grubot.util.RecyclerItemTouchHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.Nullable;

public class DashboardFragment extends Fragment implements DashboardFragmentBase, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    @Nullable @BindView(R.id.recycler) RecyclerView entriesView;
    @Nullable @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @Nullable @BindView(R.id.retryBtn) Button retryBtn;

    private Unbinder unbinder;
    private DashboardPresenter presenter;
    private ArrayList<DashboardEntry> entries;
    private DashboardRecyclerAdapter dashboardAdapter;
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

    public void setupLayouts(boolean isNetworkAvailable, boolean isHasData){
        if (isNetworkAvailable) {
            if (isHasData)
                layout = R.layout.fragment_dashboard;
            else
                layout = R.layout.fragment_no_data;
        }
        else
            layout = R.layout.fragment_no_internet_connection;
    }

    public void setupSwipeRefreshLayout(int layout){
        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.updateView(layout, getActivity());
            onItemsLoadComplete();
        });
    }

    public void setupRecyclerView(ArrayList<DashboardEntry> entries){
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        entriesView.setLayoutManager(mLayoutManager);
        entriesView.setItemAnimator(new DefaultItemAnimator());
        entriesView.setHasFixedSize(false);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(entriesView);

        this.entries = entries;
        dashboardAdapter = new DashboardRecyclerAdapter(getActivity(), entries);
        entriesView.setAdapter(dashboardAdapter);
        dashboardAdapter.notifyDataSetChanged();
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

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof DashboardRecyclerAdapter.ViewHolder) {
            Log.e("myTag", String.valueOf(viewHolder.getAdapterPosition()));
            final DashboardEntry deletedItem = entries.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            dashboardAdapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackbar = Snackbar.make(swipeRefreshLayout, deletedItem.getTypeText() + " отправлено в архив", Snackbar.LENGTH_LONG);
            snackbar.setAction(android.R.string.cancel, view -> {
                dashboardAdapter.restoreItem(deletedItem, deletedIndex);
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.destroy();
    }
}
