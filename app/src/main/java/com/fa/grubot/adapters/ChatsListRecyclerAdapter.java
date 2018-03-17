package com.fa.grubot.adapters;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fa.grubot.R;
import com.fa.grubot.fragments.BaseFragment;
import com.fa.grubot.fragments.GroupInfoFragment;
import com.fa.grubot.helpers.ChatsListDiffCallback;
import com.fa.grubot.objects.chat.Chat;
import com.fa.grubot.util.DataType;
import com.fa.grubot.util.Globals;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatsListRecyclerAdapter extends RecyclerView.Adapter<ChatsListRecyclerAdapter.ViewHolder>{

    private Context context;
    private int instance;
    private BaseFragment.FragmentNavigation fragmentNavigation;
    private final ArrayList<Chat> chats;

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chatName) TextView chatName;
        @BindView(R.id.lastMessage) TextView lastMessage;
        @BindView(R.id.chatImage) ImageView chatImage;
        @BindView(R.id.chatTypeImage) ImageView chatTypeImage;

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
    public @NotNull ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        Chat chat = chats.get(position);

        holder.chatName.setText(chat.getName());
        holder.lastMessage.setText(chat.getLastMessage());

        String imgUri = chat.getImgURI();
        if (imgUri == null)
            holder.chatImage.setImageDrawable(Globals.ImageMethods.getRoundImage(context, chat.getName()));
        else
            Glide.with(context).load(imgUri).apply(RequestOptions.circleCropTransform()).into(holder.chatImage);

        if (chat.getType().equals(DataType.Telegram))
            Glide.with(context).load(R.drawable.ic_telegram).into(holder.chatTypeImage);

        holder.chatImage.getRootView().setOnClickListener(v -> {
            fragmentNavigation.pushFragment(GroupInfoFragment.newInstance(instance + 1, chat));
        });
    }

    public void updateChatsList(ArrayList<Chat> chats) {
        final ChatsListDiffCallback diffCallback = new ChatsListDiffCallback(this.chats, chats);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.chats.clear();
        this.chats.addAll(chats);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return (chats == null) ? 0 : chats.size();
    }
}