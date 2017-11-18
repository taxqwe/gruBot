package com.fa.grubot.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;

import com.fa.grubot.App;
import com.fa.grubot.MainActivity;
import com.fa.grubot.R;
import com.fa.grubot.abstractions.ActionsFragmentBase;
import com.fa.grubot.adapters.ActionsRecyclerAdapter;
import com.fa.grubot.helpers.RecyclerItemTouchHelper;
import com.fa.grubot.objects.dashboard.Action;
import com.fa.grubot.objects.dashboard.ActionAnnouncement;
import com.fa.grubot.presenters.ActionsPresenter;
import com.fa.grubot.util.Globals;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;
import io.reactivex.annotations.Nullable;

public class ActionsFragment extends Fragment implements ActionsFragmentBase, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, Serializable {
    public static final int TYPE_ANNOUNCEMENTS = 389;
    public static final int TYPE_VOTES = 827;

    @Nullable @BindView(R.id.recycler) transient  RecyclerView actionsView;
    @Nullable @BindView(R.id.swipeRefreshLayout) transient  SwipeRefreshLayout swipeRefreshLayout;
    @Nullable @BindView(R.id.retryBtn) transient Button retryBtn;

    @Nullable @BindView(R.id.progressBar) transient ProgressBar progressBar;
    @Nullable @BindView(R.id.content) transient View content;
    @Nullable @BindView(R.id.noInternet) transient View noInternet;
    @Nullable @BindView(R.id.noData) transient View noData;

    private transient Unbinder unbinder;
    private transient ActionsPresenter presenter;
    private ArrayList<Action> actions;
    private transient ActionsRecyclerAdapter actionsAdapter;

    private int state;
    private int type;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new ActionsPresenter(this);
        View v = inflater.inflate(R.layout.fragment_actions, container, false);

        type = this.getArguments().getInt("type");
        setHasOptionsMenu(true);
        presenter.notifyFragmentStarted(getActivity(), type);

        unbinder = ButterKnife.bind(this, v);
        presenter.notifyViewCreated(state);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    public void showRequiredViews() {
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
        }, App.INSTANCE.getDelayTime());
    }

    public void showLoadingView() {
        content.setVisibility(View.GONE);
        noInternet.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void setupLayouts(boolean isNetworkAvailable, boolean isHasData){
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
        String title;
        if (type == TYPE_ANNOUNCEMENTS)
            title = "Объявления";
        else
            title = "Голосования";

        toolbar.setTitle(title);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void setupSwipeRefreshLayout(int state){
        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.updateView(state, getActivity(), type);
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

        if (App.INSTANCE.areAnimationsEnabled())
            actionsView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_from_bottom));

        this.actions = actions;
        actionsAdapter = new ActionsRecyclerAdapter(getActivity(), actions);
        actionsView.setAdapter(actionsAdapter);
        actionsAdapter.notifyDataSetChanged();
    }

    public void setupRetryButton(){
        retryBtn.setOnClickListener(view -> presenter.onRetryBtnClick(getActivity(), type));
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
