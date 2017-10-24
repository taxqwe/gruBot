package com.fa.grubot.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fa.grubot.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.fa.grubot.adapters.ChatsAdapter.ViewHolder;
import com.fa.grubot.objects.Message;

import java.util.ArrayList;

/**
 * Created by ni.petrov on 22/10/2017.
 */

public class ChatsAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Context ctx;

    private ArrayList<Message> messages;

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.messageAuthor) TextView messageAuthor;
        @BindView(R.id.messageText) TextView messageText;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public ChatsAdapter(Context ctx, ArrayList<Message> messages) {
        this.ctx = ctx;
        this.messages = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.messageText.setText(messages.get(position).getText());
        holder.messageAuthor.setText(messages.get(position).getSender());
    }

    @Override
    public int getItemCount() {
        if (messages != null){
            return messages.size();
        } else{
            return 0;
        }
    }
}
