package com.fa.grubot.adapters;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fa.grubot.MainActivity;
import com.fa.grubot.R;
import com.fa.grubot.fragments.GroupInfoFragment;
import com.fa.grubot.objects.group.Group;
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
        Group group = groups.get(position);

        holder.groupName.setText(group.getName());
        holder.groupImage.setImageDrawable(Globals.ImageMethods.getRoundImage(context, group.getName()));

        holder.groupImage.getRootView().setOnClickListener(v -> {
            Fragment fragment = new GroupInfoFragment();
            Bundle args = new Bundle();
            args.putSerializable("group", group);
            fragment.setArguments(args);
            ((MainActivity)context).pushFragments(MainActivity.TAB_CHATS, fragment,true);
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}