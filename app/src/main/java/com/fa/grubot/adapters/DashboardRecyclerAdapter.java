package com.fa.grubot.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fa.grubot.App;
import com.fa.grubot.R;
import com.fa.grubot.fragments.ActionsFragment;
import com.fa.grubot.fragments.ActionsTabFragment;
import com.fa.grubot.fragments.BaseFragment;
import com.fa.grubot.objects.dashboard.DashboardAnnouncement;
import com.fa.grubot.objects.dashboard.DashboardItem;
import com.fa.grubot.objects.dashboard.DashboardVote;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_ANNOUNCEMENT = 796;
    private static final int TYPE_VOTE = 468;

    private Context context;
    private int instance;
    private BaseFragment.FragmentNavigation fragmentNavigation;
    private ArrayList<DashboardItem> items;

    public DashboardRecyclerAdapter(Context context, int instance, BaseFragment.FragmentNavigation fragmentNavigation, ArrayList<DashboardItem> items) {
        this.context = context;
        this.instance = instance;
        this.fragmentNavigation = fragmentNavigation;
        this.items = items;
    }

    class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.newAnnouncementsCount) TextView newAnnouncementsCount;
        @BindView(R.id.archiveAnnouncementsCount) TextView archiveAnnouncementsCount;
        @BindView(R.id.totalAnnouncementsCount) TextView totalAnnouncementsCount;
        @BindView(R.id.announcementsView) CardView announcementsView;

        private AnnouncementViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            announcementsView.setOnClickListener(view1 -> {
                if (App.INSTANCE.isBackstackEnabled())
                    fragmentNavigation.pushFragment(ActionsTabFragment.newInstance(instance + 1, ActionsFragment.TYPE_ANNOUNCEMENTS));
                else
                    fragmentNavigation.pushFragment(ActionsTabFragment.newInstance(0, ActionsFragment.TYPE_ANNOUNCEMENTS));
            });
        }
    }

    class VoteViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.newVotesCount) TextView newVotesCount;
        @BindView(R.id.archiveVotesCount) TextView archiveVotesCount;
        @BindView(R.id.totalVotesCount) TextView totalVotesCount;
        @BindView(R.id.votesView) CardView votesView;

        private VoteViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            votesView.setOnClickListener(view1 -> {
                fragmentNavigation.pushFragment(ActionsTabFragment.newInstance(instance + 1, ActionsFragment.TYPE_VOTES));
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ANNOUNCEMENT:
                return (new AnnouncementViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard_announcement, parent, false)));
            case TYPE_VOTE:
                return (new VoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard_vote, parent, false)));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();
        DashboardItem item = items.get(position);
        switch (holder.getItemViewType()) {
            case TYPE_ANNOUNCEMENT:
                AnnouncementViewHolder announcementViewHolder = (AnnouncementViewHolder) holder;
                DashboardAnnouncement announcement = (DashboardAnnouncement) item;
                announcementViewHolder.newAnnouncementsCount.setText("Новых: " + announcement.getNewCount());
                announcementViewHolder.archiveAnnouncementsCount.setText("В архиве: " + announcement.getArchiveCount());
                announcementViewHolder.totalAnnouncementsCount.setText("Всего: " + announcement.getTotalCount());
                break;
            case TYPE_VOTE:
                VoteViewHolder voteViewHolder = (VoteViewHolder) holder;
                DashboardVote vote = (DashboardVote) item;
                voteViewHolder.newVotesCount.setText("Новых: " + vote.getNewCount());
                voteViewHolder.archiveVotesCount.setText("В архиве: " + vote.getArchiveCount());
                voteViewHolder.totalVotesCount.setText("Всего: " + vote.getTotalCount());
                break;
        }
    }

    public void updateItem(int count, int type) {
        switch (type) {
            case ActionsFragment.TYPE_ANNOUNCEMENTS:
                items.get(0).updateNewCount(count);
                notifyItemChanged(0);
                break;
            case ActionsFragment.TYPE_ANNOUNCEMENTS_ARCHIVE:
                items.get(0).updateArchiveCount(count);
                notifyItemChanged(0);
                break;
            case ActionsFragment.TYPE_VOTES:
                items.get(1).updateNewCount(count);
                notifyItemChanged(1);
                break;
            case ActionsFragment.TYPE_VOTES_ARCHIVE:
                items.get(1).updateArchiveCount(count);
                notifyItemChanged(1);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        DashboardItem item = items.get(position);
        if (item instanceof DashboardAnnouncement)
            return TYPE_ANNOUNCEMENT;
        else
            return TYPE_VOTE;
    }

    @Override
    public int getItemCount() {
        return (items == null) ? 0 : items.size();
    }

    public void clearItems() {
        items.clear();
        notifyDataSetChanged();
    }
}