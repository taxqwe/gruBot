package com.fa.grubot.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fa.grubot.R;
import com.fa.grubot.objects.Group;
import com.fa.grubot.util.Globals;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupsRecyclerAdapter extends RecyclerView.Adapter<GroupsRecyclerAdapter.ViewHolder>{

    private Context context;
    private final ArrayList<Group> groups;

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.groupName) TextView groupName;
        @BindView(R.id.groupImage) ImageView groupImage;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public GroupsRecyclerAdapter(Context context, ArrayList<Group> groups) {
        this.context = context;
        this.groups = groups;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();

        holder.groupName.setText(groups.get(position).getName());
        holder.groupImage.setImageDrawable(Globals.ImageMethods.getRoundImage(context, groups.get(position).getName()));

        holder.groupImage.getRootView().setOnClickListener(v -> {
            Toast.makeText(context, "КЛИК))))", Toast.LENGTH_SHORT).show();
            //// TODO: 19.10.2017
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}