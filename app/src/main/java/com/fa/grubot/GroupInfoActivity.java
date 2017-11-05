package com.fa.grubot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.fa.grubot.abstractions.GroupInfoActivityBase;
import com.fa.grubot.adapters.GroupInfoRecyclerAdapter;
import com.fa.grubot.objects.Group;
import com.fa.grubot.presenters.GroupInfoPresenter;
import com.fa.grubot.util.Globals;
import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;

public class GroupInfoActivity extends AppCompatActivity implements GroupInfoActivityBase {
    @BindView(R.id.toolbar) Toolbar toolbar;
    //@BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.recycler) RecyclerView buttonsView;

    private GroupInfoPresenter presenter;
    private Unbinder unbinder;

    private Group group;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        Slidr.attach(this, Globals.Config.getSlidrConfig());
        Icepick.restoreInstanceState(this, savedInstanceState);

        unbinder = ButterKnife.bind(this);

        group = (Group) getIntent().getExtras().getSerializable("group");
        presenter = new GroupInfoPresenter(this);
        presenter.notifyViewCreated(group);
    }

    public void setupToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(group.getName());
    }

    public void setupFab(){
        //fab.setOnClickListener(view -> {
            //click
        //});
    }

    public void setupRecyclerView(ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> buttons){
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        buttonsView.setLayoutManager(mLayoutManager);
        buttonsView.setHasFixedSize(false);

        GroupInfoRecyclerAdapter groupInfoAdapter = new GroupInfoRecyclerAdapter(this, buttons);
        groupInfoAdapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        buttonsView.setAdapter(groupInfoAdapter);
        groupInfoAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        presenter.destroy();
    }
}
