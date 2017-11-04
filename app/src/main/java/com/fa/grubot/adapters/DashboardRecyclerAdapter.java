package com.fa.grubot.adapters;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fa.grubot.R;
import com.fa.grubot.objects.DashboardEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fa.grubot.objects.DashboardEntry.TYPE_ANNOUNCEMENT;
import static com.fa.grubot.objects.DashboardEntry.TYPE_VOTE;

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

        holder.entryTypeText.setText(entry.getTypeText());
        holder.entryDate.setText(entry.getDate());
        holder.entryAuthor.setText(entry.getAuthor());
        holder.entryDesc.setText(entry.getDesc());
        holder.entryGroup.setText(entry.getGroup().getName());
        holder.viewForeground.setBackgroundColor(getColorFromDashboardEntry(entry));

        holder.viewForeground.setOnClickListener(v -> {
            if (entry.getType() == DashboardEntry.TYPE_ANNOUNCEMENT)
                new MaterialDialog.Builder(context)
                        .title(entry.getGroup().getName() + ": " + entry.getDesc())
                        .content(entry.getText())
                        .positiveText(android.R.string.ok)
                        .show();
        });
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
        Map<Integer, Integer> colorsPairList = new HashMap<>();

        colorsPairList.put(TYPE_ANNOUNCEMENT, ResourcesCompat.getColor(context.getResources(), R.color.colorAnnouncement, null));
        colorsPairList.put(TYPE_VOTE, ResourcesCompat.getColor(context.getResources(), R.color.colorVote, null));

        return colorsPairList.get(entry.getType());
    }
}