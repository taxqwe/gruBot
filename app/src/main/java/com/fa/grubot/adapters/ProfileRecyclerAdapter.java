package com.fa.grubot.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.fa.grubot.R;
import com.fa.grubot.objects.misc.ProfileItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileRecyclerAdapter extends RecyclerView.Adapter<ProfileRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ProfileItem> items;

    public ProfileRecyclerAdapter(Context context, ArrayList<ProfileItem> items) {
        this.context = context;
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemImage) ImageView itemImage;
        @BindView(R.id.itemValue) TextView itemValue;
        @BindView(R.id.itemText) TextView itemText;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public ProfileRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return (new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, parent, false)));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int pos) {
        int position = holder.getAdapterPosition();
        ProfileItem item = items.get(position);

        switch (position) {
            case 0:
                Glide.with(context).load(R.drawable.ic_description_black_36dp).apply(new RequestOptions().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)).into(holder.itemImage);
                holder.itemText.setText(item.getText());
                holder.itemValue.setText(item.getValue());
                break;
            case 1:
                Glide.with(context).load(R.drawable.ic_person_black_36dp).apply(new RequestOptions().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)).into(holder.itemImage);
                holder.itemText.setText(item.getText());
                holder.itemValue.setText(item.getText());
                break;
            case 2:
                Glide.with(context).load(R.drawable.ic_person_black_36dp).apply(new RequestOptions().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)).into(holder.itemImage);
                holder.itemImage.setVisibility(View.INVISIBLE);
                holder.itemText.setText(item.getText());
                holder.itemValue.setText("@" + item.getValue());
                break;
            case 3:
                Glide.with(context).load(R.drawable.ic_phone_black_36dp).apply(new RequestOptions().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)).into(holder.itemImage);
                holder.itemText.setText(item.getText());
                holder.itemValue.setText(item.getValue());
                break;
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}