package com.fa.grubot.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fa.grubot.ItemsActivity;
import com.fa.grubot.R;
import com.fa.grubot.fragments.ActionsFragment;
import com.fa.grubot.objects.dashboard.DashboardAnnouncement;
import com.fa.grubot.objects.dashboard.DashboardChat;
import com.fa.grubot.objects.dashboard.DashboardItem;
import com.fa.grubot.objects.dashboard.DashboardSettings;
import com.fa.grubot.objects.dashboard.DashboardVote;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_ANNOUNCEMENT = 796;
    private static final int TYPE_VOTE = 468;
    private static final int TYPE_CHAT = 338;
    private static final int TYPE_SETTINGS = 819;

    private Context context;
    private ArrayList<DashboardItem> items;

    public DashboardRecyclerAdapter(Context context, ArrayList<DashboardItem> items) {
        this.context = context;
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
                Intent intent = new Intent(context, ItemsActivity.class);
                intent.putExtra("type", ActionsFragment.TYPE_ANNOUNCEMENTS);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.right_in, R.anim.left_out);
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
                Intent intent = new Intent(context, ItemsActivity.class);
                intent.putExtra("type", ActionsFragment.TYPE_VOTES);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.right_in, R.anim.left_out);
            });
        }
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.newMessagesCount) TextView newMessagesCount;
        @BindView(R.id.totalChatsCount) TextView totalChatsCount;
        @BindView(R.id.chatsView) CardView chatsView;

        private ChatViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            chatsView.setOnClickListener(view1 -> {
                Intent intent = new Intent(context, ItemsActivity.class);
                intent.putExtra("type", 0);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.right_in, R.anim.left_out);
            });
        }
    }

    class SettingsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.totalSettingsCount) TextView totalSettingsCount;
        @BindView(R.id.settingsView) CardView settingsView;

        private SettingsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            settingsView.setOnClickListener(view1 -> {
                Intent intent = new Intent(context, ItemsActivity.class);
                intent.putExtra("type", 0);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.right_in, R.anim.left_out);
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
            case TYPE_CHAT:
                return (new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard_chat, parent, false)));
            default:
                return (new SettingsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard_settings, parent, false)));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();
        DashboardItem item = items.get(position);
        switch (holder.getItemViewType()) {
            case TYPE_ANNOUNCEMENT:
                AnnouncementViewHolder announcementViewHolder = (AnnouncementViewHolder) holder;
                DashboardAnnouncement announcement = (DashboardAnnouncement) item;
                announcementViewHolder.newAnnouncementsCount.setText("Новых: " + announcement.getNewAnnouncementsCount());
                announcementViewHolder.archiveAnnouncementsCount.setText("В архиве: " + announcement.getArchiveAnnouncementsCount());
                announcementViewHolder.totalAnnouncementsCount.setText("Всего: " + announcement.getTotalAnnouncementsCount());
                announcementViewHolder.announcementsView.setOnClickListener(view -> {
                    Intent intent = new Intent(context, ItemsActivity.class);
                    intent.putExtra("type", ActionsFragment.TYPE_ANNOUNCEMENTS);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.right_in, R.anim.left_out);
                });
                break;
            case TYPE_VOTE:
                VoteViewHolder voteViewHolder = (VoteViewHolder) holder;
                DashboardVote vote = (DashboardVote) item;
                voteViewHolder.newVotesCount.setText("Новых: " + vote.getNewVotesCount());
                voteViewHolder.archiveVotesCount.setText("В архиве: " + vote.getArchiveVotesCount());
                voteViewHolder.totalVotesCount.setText("Всего: " + vote.getTotalVotesCount());
                voteViewHolder.votesView.setOnClickListener(view -> {
                    Intent intent = new Intent(context, ItemsActivity.class);
                    intent.putExtra("type", ActionsFragment.TYPE_VOTES);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.right_in, R.anim.left_out);
                });
                break;
            case TYPE_CHAT:
                ChatViewHolder chatViewHolder = (ChatViewHolder) holder;
                DashboardChat chat = (DashboardChat) item;
                chatViewHolder.totalChatsCount.setText("Всего чатов: " + chat.getTotalChatsCount());
                chatViewHolder.newMessagesCount.setText("Новых сообщений: " + chat.getNewMessagesCount());
                chatViewHolder.chatsView.setOnClickListener(view -> {
                    Intent intent = new Intent(context, ItemsActivity.class);
                    intent.putExtra("type", 0);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.right_in, R.anim.left_out);
                });
                break;
            case TYPE_SETTINGS:
                SettingsViewHolder settingsViewHolder = (SettingsViewHolder) holder;
                DashboardSettings settings = (DashboardSettings) item;
                settingsViewHolder.totalSettingsCount.setText("Доступно настроек: " + settings.getTotalSettingsCount());
                settingsViewHolder.settingsView.setOnClickListener(view -> {
                    //TODO
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        DashboardItem item = items.get(position);
        if (item instanceof DashboardAnnouncement)
            return TYPE_ANNOUNCEMENT;
        else if (item instanceof DashboardVote)
            return TYPE_VOTE;
        else if (item instanceof DashboardChat)
            return TYPE_CHAT;
        else
            return TYPE_SETTINGS;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}