package com.fa.grubot.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fa.grubot.App;
import com.fa.grubot.R;
import com.fa.grubot.fragments.BaseFragment;
import com.fa.grubot.fragments.GroupInfoFragment;
import com.fa.grubot.objects.group.Group;
import com.fa.grubot.util.Globals;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupsRecyclerAdapter extends RecyclerView.Adapter<GroupsRecyclerAdapter.ViewHolder>{

    private Context context;
    private int instance;
    private BaseFragment.FragmentNavigation fragmentNavigation;
    private final ArrayList<Group> groups;

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.groupName) TextView groupName;
        @BindView(R.id.groupImage) ImageView groupImage;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public GroupsRecyclerAdapter(Context context, int instance, BaseFragment.FragmentNavigation fragmentNavigation, ArrayList<Group> groups) {
        this.context = context;
        this.instance = instance;
        this.fragmentNavigation = fragmentNavigation;
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
            fragmentNavigation.pushFragment(GroupInfoFragment.newInstance(instance + 1, group));
        });
    }

    @Override
    public int getItemCount() {
        return (groups == null) ? 0 : groups.size();
    }

    public void clearItems() {
        groups.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        groups.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(int position, Group group) {
        groups.add(position, group);
        notifyItemInserted(position);
    }

    public void updateItem(int oldPosition, int newPosition, Group group) {
        if (oldPosition == newPosition) {
            groups.set(oldPosition, group);
            notifyItemChanged(oldPosition);
        } else {
            groups.remove(oldPosition);
            groups.add(newPosition, group);
            notifyItemMoved(oldPosition, newPosition);
        }
    }
}