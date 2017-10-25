package com.fa.grubot.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fa.grubot.ChatActivity;
import com.fa.grubot.R;
import com.fa.grubot.objects.GroupInfoButton;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupInfoRecyclerAdapter extends RecyclerView.Adapter<GroupInfoRecyclerAdapter.ViewHolder>{

    private Context context;
    private final ArrayList<GroupInfoButton> buttons;

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.buttonText) TextView buttonText;
        @BindView(R.id.buttonNotificationsText) TextView buttonNotificationsText;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public GroupInfoRecyclerAdapter(Context context, ArrayList<GroupInfoButton> buttons) {
        this.context = context;
        this.buttons = buttons;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_info_button, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        GroupInfoButton button = buttons.get(position);

        holder.buttonText.setText(button.getText());
        if (button.getNotificationsCount() > 0) {
            holder.buttonNotificationsText.setText(String.valueOf(button.getNotificationsCount()));
            holder.buttonNotificationsText.setVisibility(View.VISIBLE);
        }

        if (button.getId() == 1) {
            holder.buttonText.getRootView().setOnClickListener(v -> {
                Intent intent = new Intent(context, ChatActivity.class);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return buttons.size();
    }
}