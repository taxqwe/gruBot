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
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fa.grubot.abstractions.GroupInfoActivityBase;
import com.fa.grubot.adapters.GroupInfoRecyclerAdapter;
import com.fa.grubot.adapters.VoteRecyclerAdapter;
import com.fa.grubot.objects.dashboard.Announcement;
import com.fa.grubot.objects.dashboard.Vote;
import com.fa.grubot.objects.group.Group;
import com.fa.grubot.objects.misc.VoteOption;
import com.fa.grubot.presenters.GroupInfoPresenter;
import com.fa.grubot.util.Globals;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;

public class GroupInfoActivity extends AppCompatActivity implements GroupInfoActivityBase {
    @BindView(R.id.root) CoordinatorLayout rootView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler) RecyclerView buttonsView;

    @BindView(R.id.fam) FloatingActionMenu fam;
    @BindView(R.id.fab_add_announcement) FloatingActionButton announcementFab;
    @BindView(R.id.fab_add_vote) FloatingActionButton voteFab;

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
                    .canceledOnTouchOutside(false)
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

        voteFab.setOnClickListener(view -> {
            groupInfoAdapter.collapseAll(); //TODO не баг, а фича. Если убрать все сломается и мне сейчас лень это фиксить, когда можно просто написать эту строку. Если кто-то это прочитает, то ёбните меня.

            MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                    .title("Голосование")
                    .customView(R.layout.dialog_add_vote, false)
                    .canceledOnTouchOutside(false)
                    .positiveText(android.R.string.ok)
                    .negativeText(android.R.string.cancel)
                    .autoDismiss(false)
                    .neutralText("+ вариант")
                    .onNegative((dialog, which) -> {
                        dialog.dismiss();
                    })
                    .build();
            RecyclerView voteRecycler = materialDialog.getView().findViewById(R.id.vote_recycler);
            EditText desc = (EditText) materialDialog.getView().findViewById(R.id.voteDesc);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            VoteRecyclerAdapter voteAdapter = new VoteRecyclerAdapter(this, new ArrayList<VoteOption>(Arrays.asList(new VoteOption())));
            voteRecycler.setLayoutManager(mLayoutManager);
            voteRecycler.setHasFixedSize(false);

            voteRecycler.setAdapter(voteAdapter);
            voteAdapter.notifyDataSetChanged();

            MaterialDialog.SingleButtonCallback neutralCallback = (dialog, which) -> {
                voteAdapter.insertOption(new VoteOption());
                voteRecycler.smoothScrollToPosition(voteAdapter.getItemCount() - 1);
            };

            MaterialDialog.SingleButtonCallback positiveCallback = (dialog, which) -> {
                boolean hasEmpty = false;
                ArrayList<VoteOption> options = voteAdapter.getOptions();

                for (VoteOption option : options){
                    if (option.getText().isEmpty()){
                        hasEmpty = true;
                        break;
                    }
                }
                if (desc.getText().toString().isEmpty())
                    Toast.makeText(this, "Описание должно быть заполнено", Toast.LENGTH_SHORT).show();
                else
                    if (options.size() < 2)
                        Toast.makeText(this, "Должно быть не менее двух вариантов выбора", Toast.LENGTH_SHORT).show();
                    else
                        if (hasEmpty)
                            Toast.makeText(this, "Все варианты выбора должны быть заполнены", Toast.LENGTH_SHORT).show();
                        else {
                            Vote vote = new Vote(1488, group, "Current user", desc.getText().toString(), new Date(), options);
                            groupInfoAdapter.insertItem(vote);
                            dialog.dismiss();
                            fam.close(true);
                        }
            };
            materialDialog.getBuilder().onNeutral(neutralCallback).onPositive(positiveCallback);

            materialDialog.show();
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
