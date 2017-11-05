package com.fa.grubot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fa.grubot.abstractions.GroupInfoActivityBase;
import com.fa.grubot.adapters.GroupInfoRecyclerAdapter;
import com.fa.grubot.objects.dashboard.Announcement;
import com.fa.grubot.objects.group.Group;
import com.fa.grubot.presenters.GroupInfoPresenter;
import com.fa.grubot.util.Globals;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;

public class GroupInfoActivity extends AppCompatActivity implements GroupInfoActivityBase {
    @BindView(R.id.root) CoordinatorLayout rootView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fam) FloatingActionMenu fam;
    @BindView(R.id.fab_add_announcement) FloatingActionButton announcementFab;
    @BindView(R.id.recycler) RecyclerView buttonsView;


    private GroupInfoRecyclerAdapter groupInfoAdapter;
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
        fam.setClosedOnTouchOutside(true);
        announcementFab.setOnClickListener(view -> {
            groupInfoAdapter.collapseAll(); //TODO не баг, а фича. Если убрать все сломается и мне сейчас лень это фиксить, когда можно просто написать эту строку. Если кто-то это прочитает, то ёбните меня.
            new MaterialDialog.Builder(this)
                    .title("Объявление")
                    .customView(R.layout.dialog_add_announcement, false)
                    .positiveText(android.R.string.ok)
                    .negativeText(android.R.string.cancel)
                    .onPositive((dialog, which) -> {
                        EditText desc = (EditText) dialog.findViewById(R.id.announcementDesc);
                        EditText text = (EditText) dialog.findViewById(R.id.announcementText);

                        if (!desc.toString().isEmpty() && !text.toString().isEmpty()){
                            Announcement announcement = new Announcement(1488, group, "Current User", desc.getText().toString(), new Date(), text.getText().toString());
                            groupInfoAdapter.insertItem(announcement);
                        }

                        fam.close(true);
                    })
                    .show();
        });
    }

    public void setupRecyclerView(ArrayList<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem> buttons){
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        buttonsView.setLayoutManager(mLayoutManager);
        buttonsView.setHasFixedSize(false);

        groupInfoAdapter = new GroupInfoRecyclerAdapter(this, buttons);
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
