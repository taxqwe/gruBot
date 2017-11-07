package com.fa.grubot.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fa.grubot.R;
import com.fa.grubot.abstractions.DashboardSpecificFragmentBase;
import com.fa.grubot.adapters.DashboardRecyclerAdapter;
import com.fa.grubot.objects.dashboard.Announcement;
import com.fa.grubot.objects.dashboard.DashboardEntry;
import com.fa.grubot.presenters.DashboardSpecificPresenter;
import com.fa.grubot.util.RecyclerItemTouchHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.Nullable;

public class DashboardSpecificFragment extends Fragment implements DashboardSpecificFragmentBase, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{
    public static final int TYPE_ANNOUNCEMENTS = 389;
    public static final int TYPE_VOTES = 827;

    @Nullable @BindView(R.id.toolbar) Toolbar toolbar;
    @Nullable @BindView(R.id.recycler) RecyclerView entriesView;
    @Nullable @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @Nullable @BindView(R.id.retryBtn) Button retryBtn;

    private Unbinder unbinder;
    private DashboardSpecificPresenter presenter;
    private ArrayList<DashboardEntry> entries;
    private DashboardRecyclerAdapter dashboardAdapter;
    private int layout;
    private int type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new DashboardSpecificPresenter(this);
        type = this.getArguments().getInt("type");
        setHasOptionsMenu(true);
        presenter.notifyFragmentStarted(getActivity(), type);
        View v = inflater.inflate(layout, container, false);

        unbinder = ButterKnife.bind(this, v);
        presenter.notifyViewCreated(layout, v);

        return v;
    }

    public void setupLayouts(boolean isNetworkAvailable, boolean isHasData){
        if (isNetworkAvailable) {
            if (isHasData)
                layout = R.layout.fragment_dashboard_specific;
            else
                layout = R.layout.fragment_no_data;
        }
        else
            layout = R.layout.fragment_no_internet_connection;
    }

    public void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        String title = "";
        if (type == TYPE_ANNOUNCEMENTS)
            title = "Объявления";
        else
            title = "Голосования";

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void setupSwipeRefreshLayout(int layout){
        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.updateView(layout, getActivity(), type);
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
        Bundle args = new Bundle();
        args.putInt("type", type);
        currentFragment.setArguments(args);
        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
        fragTransaction.detach(currentFragment);
        fragTransaction.attach(currentFragment);
        fragTransaction.commit();
    }

    private void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof DashboardRecyclerAdapter.ViewHolder) {
            Log.e("myTag", String.valueOf(viewHolder.getAdapterPosition()));
            final DashboardEntry deletedItem = entries.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            dashboardAdapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackbar;
            if (deletedItem instanceof Announcement) {
                snackbar = Snackbar.make(swipeRefreshLayout, "Объявление отправлено в архив", Snackbar.LENGTH_LONG);
            } else {
                snackbar = Snackbar.make(swipeRefreshLayout, "Голосование отправлено в архив", Snackbar.LENGTH_LONG);
            }
            snackbar.setAction(android.R.string.cancel, view -> {
                if (entries.indexOf(deletedItem) == -1) {
                    dashboardAdapter.restoreItem(deletedItem, deletedIndex);
                    entriesView.smoothScrollToPosition(deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            getActivity().onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.destroy();
    }
}
