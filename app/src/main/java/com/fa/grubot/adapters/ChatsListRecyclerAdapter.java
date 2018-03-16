package com.fa.grubot.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fa.grubot.R;
import com.fa.grubot.fragments.BaseFragment;
import com.fa.grubot.fragments.GroupInfoFragment;
import com.fa.grubot.objects.Chat;
import com.fa.grubot.util.Globals;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatsListRecyclerAdapter extends RecyclerView.Adapter<ChatsListRecyclerAdapter.ViewHolder>{

    private Context context;
    private int instance;
    private BaseFragment.FragmentNavigation fragmentNavigation;
    private final ArrayList<Chat> chats;

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.groupName) TextView groupName;
        @BindView(R.id.groupImage) ImageView groupImage;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ChatsListRecyclerAdapter(Context context, int instance, BaseFragment.FragmentNavigation fragmentNavigation, ArrayList<Chat> chats) {
        this.context = context;
        this.instance = instance;
        this.fragmentNavigation = fragmentNavigation;
        this.chats = chats;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        Chat chat = chats.get(position);

        holder.groupName.setText(chat.getName());
        holder.groupImage.setImageDrawable(Globals.ImageMethods.getRoundImage(context, chat.getName()));

        holder.groupImage.getRootView().setOnClickListener(v -> {
            fragmentNavigation.pushFragment(GroupInfoFragment.newInstance(instance + 1, chat));
        });
    }

    @Override
    public int getItemCount() {
        return (chats == null) ? 0 : chats.size();
    }

    public void clearItems() {
        chats.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        chats.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(int position, Chat chat) {
        chats.add(position, chat);
        notifyItemInserted(position);
    }

    public void updateItem(int oldPosition, int newPosition, Chat chat) {
        if (oldPosition == newPosition) {
            chats.set(oldPosition, chat);
            notifyItemChanged(oldPosition);
        } else {
            chats.remove(oldPosition);
            chats.add(newPosition, chat);
            notifyItemMoved(oldPosition, newPosition);
        }
    }
}