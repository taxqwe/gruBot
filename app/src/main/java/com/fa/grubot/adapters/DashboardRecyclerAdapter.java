package com.fa.grubot.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fa.grubot.R;
import com.fa.grubot.objects.dashboard.Announcement;
import com.fa.grubot.objects.dashboard.DashboardEntry;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardRecyclerAdapter extends RecyclerView.Adapter<DashboardRecyclerAdapter.ViewHolder>{

    private Context context;
    private final ArrayList<DashboardEntry> entries;

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.entryTypeText) TextView entryTypeText;
        @BindView(R.id.entryDate) TextView entryDate;
        @BindView(R.id.entryGroup) TextView entryGroup;
        @BindView(R.id.entryAuthor) TextView entryAuthor;
        @BindView(R.id.entryDesc) TextView entryDesc;

        @BindView(R.id.view_background) RelativeLayout viewBackground;
        public @BindView(R.id.view_foreground) RelativeLayout viewForeground;


        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public DashboardRecyclerAdapter(Context context, ArrayList<DashboardEntry> entries) {
        this.context = context;
        this.entries = entries;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard_entry, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        DashboardEntry entry = entries.get(position);

        holder.entryDate.setText(entry.getDate());
        holder.entryAuthor.setText(entry.getAuthor());
        holder.entryDesc.setText(entry.getDesc());
        holder.entryGroup.setText(entry.getGroup().getName());
        holder.viewForeground.setBackgroundColor(getColorFromDashboardEntry(entry));

        if (entry instanceof Announcement) {
            holder.entryTypeText.setText("Объявление");
            holder.viewForeground.setOnClickListener(v -> {
                new MaterialDialog.Builder(context)
                        .title(entry.getGroup().getName() + ": " + entry.getDesc())
                        .content(((Announcement) entry).getText())
                        .positiveText(android.R.string.ok)
                        .show();
            });
        } else {
            holder.entryTypeText.setText("Голосование");
            holder.viewForeground.setOnClickListener(v -> {
                new MaterialDialog.Builder(context)
                        .title(entry.getGroup().getName() + ": " + entry.getDesc())
                        .items(new String[] {"1", "2", "3"})
                        .itemsCallbackSingleChoice(-1, (MaterialDialog.ListCallbackSingleChoice) (dialog, view, which, text) -> {
                            /**
                             * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                             * returning false here won't allow the newly selected radio button to actually be selected.
                             **/
                            return true;
                        })
                        .positiveText(android.R.string.ok)
                        .show();
            });
        }
    }

    public void removeItem(int position) {
        entries.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(DashboardEntry entry, int position) {
        entries.add(position, entry);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    private int getColorFromDashboardEntry(DashboardEntry entry){
        if (entry instanceof Announcement)
            return context.getResources().getColor(R.color.colorAnnouncement);
        else
            return context.getResources().getColor(R.color.colorVote);
    }
}