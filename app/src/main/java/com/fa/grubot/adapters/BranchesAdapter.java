package com.fa.grubot.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fa.grubot.R;
import com.fa.grubot.objects.chat.BranchOfDiscussions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ni.petrov on 18/11/2017.
 */

public class BranchesAdapter extends RecyclerView.Adapter<BranchesAdapter.ViewHolder> {

    private ArrayList<BranchOfDiscussions> data;

    public BranchesAdapter(ArrayList<BranchOfDiscussions> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_branch, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.theme.setText(data.get(position).getTheme());
        holder.author.setText(String.valueOf(data.get(position).getAuthorsId()));
        holder.count.setText(String.valueOf(data.get(position).getMessagesCount()));
        holder.startDate.setText(data.get(position).getStartDate());
        holder.lastDate.setText(data.get(position).getLastDate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.theme)
        TextView theme;

        @BindView(R.id.author)
        TextView author;

        @BindView(R.id.messages_count)
        TextView count;

        @BindView(R.id.start_date)
        TextView startDate;

        @BindView(R.id.last_date)
        TextView lastDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
