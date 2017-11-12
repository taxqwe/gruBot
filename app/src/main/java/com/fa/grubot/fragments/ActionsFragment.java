package com.fa.grubot.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fa.grubot.R;
import com.fa.grubot.abstractions.ActionsFragmentBase;
import com.fa.grubot.adapters.ActionsRecyclerAdapter;
import com.fa.grubot.objects.dashboard.Action;
import com.fa.grubot.objects.dashboard.ActionAnnouncement;
import com.fa.grubot.presenters.ActionsPresenter;
import com.fa.grubot.util.RecyclerItemTouchHelper;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.Nullable;

public class ActionsFragment extends Fragment implements ActionsFragmentBase, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, Serializable {
    public static final int TYPE_ANNOUNCEMENTS = 389;
    public static final int TYPE_VOTES = 827;

    @Nullable @BindView(R.id.toolbar) transient Toolbar toolbar;
    @Nullable @BindView(R.id.recycler) transient  RecyclerView actionsView;
    @Nullable @BindView(R.id.swipeRefreshLayout) transient  SwipeRefreshLayout swipeRefreshLayout;
    @Nullable @BindView(R.id.retryBtn) Button retryBtn;

    private transient Unbinder unbinder;
    private transient ActionsPresenter presenter;
    private ArrayList<Action> actions;
    private transient ActionsRecyclerAdapter actionsAdapter;
    private int layout;
    private int type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new ActionsPresenter(this);
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
                layout = R.layout.fragment_actions;
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

    public void setupRecyclerView(ArrayList<Action> actions){
        int spanCount = 1;

        if (getActivity().getResources().getConfiguration().orientation == 2)
            spanCount = 2;

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);

        actionsView.setLayoutManager(layoutManager);
        actionsView.setItemAnimator(new DefaultItemAnimator());
        actionsView.setHasFixedSize(false);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(actionsView);

        this.actions = actions;
        actionsAdapter = new ActionsRecyclerAdapter(getActivity(), actions);
        actionsView.setAdapter(actionsAdapter);
        actionsAdapter.notifyDataSetChanged();
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

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ActionsRecyclerAdapter.ViewHolder) {
            final Action deletedItem = actions.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            actionsAdapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackbar;
            if (deletedItem instanceof ActionAnnouncement) {
                snackbar = Snackbar.make(swipeRefreshLayout, "Объявление отправлено в архив", Snackbar.LENGTH_LONG);
            } else {
                snackbar = Snackbar.make(swipeRefreshLayout, "Голосование отправлено в архив", Snackbar.LENGTH_LONG);
            }
            snackbar.setAction(android.R.string.cancel, view -> {
                if (actions.indexOf(deletedItem) == -1) {
                    actionsAdapter.restoreItem(deletedItem, deletedIndex);
                    actionsView.smoothScrollToPosition(deletedIndex);
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
