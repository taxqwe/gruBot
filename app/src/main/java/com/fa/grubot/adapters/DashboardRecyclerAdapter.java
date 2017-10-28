package com.fa.grubot.adapters;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fa.grubot.R;
import com.fa.grubot.objects.DashboardEntry;
import com.fa.grubot.util.Globals;

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

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.groupImage) ImageView groupImage;
        @BindView(R.id.entryTypeText) TextView entryTypeText;
        @BindView(R.id.entryDate) TextView entryDate;
        @BindView(R.id.entryAuthor) TextView entryAuthor;
        @BindView(R.id.entryDesc) TextView entryDesc;
        @BindView(R.id.card_view) View cardView;


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
        holder.groupImage.setImageDrawable(Globals.ImageMethods.getRoundImage(context, entry.getGroup().getName()));
        holder.cardView.setBackgroundColor(getColorFromDashboardEntry(entry));

        holder.groupImage.getRootView().setOnClickListener(v -> {
            Toast.makeText(context, "КЛИК))))", Toast.LENGTH_SHORT).show();
            //// TODO: 19.10.2017
        });
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