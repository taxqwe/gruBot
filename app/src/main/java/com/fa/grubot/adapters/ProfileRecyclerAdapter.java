package com.fa.grubot.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.fa.grubot.App;
import com.fa.grubot.MainActivity;
import com.fa.grubot.R;
import com.fa.grubot.objects.misc.ProfileItem;
import com.fa.grubot.objects.users.User;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileRecyclerAdapter extends RecyclerView.Adapter<ProfileRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ProfileItem> items;
    private User user;

    public ProfileRecyclerAdapter(Context context, ArrayList<ProfileItem> items, User user) {
        this.context = context;
        this.items = items;
        this.user = user;
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
                Glide.with(context).load(R.drawable.ic_person_black_36dp).apply(new RequestOptions().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)).into(holder.itemImage);
                holder.itemText.setText(item.getText());
                holder.itemValue.setText(item.getValue());
                break;
            case 1:
                Glide.with(context).load(R.drawable.ic_person_black_36dp).apply(new RequestOptions().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)).into(holder.itemImage);
                holder.itemImage.setVisibility(View.INVISIBLE);
                holder.itemText.setText(item.getText());
                holder.itemValue.setText("@" + item.getValue());
                break;
            case 2:
                Glide.with(context).load(R.drawable.ic_phone_black_36dp).apply(new RequestOptions().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)).into(holder.itemImage);
                holder.itemText.setText(item.getText());
                holder.itemValue.setText(item.getValue());
                if (!user.getUserId().equals(App.INSTANCE.getCurrentUser().getTelegramUser().getId())) {
                    holder.itemImage.getRootView().setOnClickListener(view -> {
                        Dexter.withActivity((MainActivity) context)
                                .withPermission(Manifest.permission.CALL_PHONE)
                                .withListener(new PermissionListener() {
                                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse("tel:" + user.getPhoneNumber()));
                                        if (ContextCompat.checkSelfPermission( context, Manifest.permission.CALL_PHONE ) == PackageManager.PERMISSION_GRANTED)
                                            context.startActivity(callIntent);
                                        else
                                            Toast.makeText(context, "Доступ запрещен", Toast.LENGTH_SHORT).show();
                                    }
                                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                                        Toast.makeText(context, "Доступ запрещен", Toast.LENGTH_SHORT).show();
                                    }
                                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                        token.continuePermissionRequest();
                                    }
                                }).check();
                    });
                }
                break;
            case 3:
                Glide.with(context).load(R.drawable.ic_description_black_36dp).apply(new RequestOptions().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)).into(holder.itemImage);
                holder.itemText.setText(item.getText());
                holder.itemValue.setText(item.getValue());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return (items == null) ? 0 : items.size();
    }

    public void clearItems() {
        items.clear();
        notifyDataSetChanged();
    }

    public void addProfileItems(ArrayList<ProfileItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void updateProfileItems(ArrayList<String> changes, User user) {
        //TODO что-то сделать с этим дерьмом
        if (changes.contains("fullname"))
            updateItem("Имя", user.getFullname());
        if (changes.contains("username"))
            updateItem("Логин", user.getUserName());
        if (changes.contains("phoneNumber"))
            updateItem("Номер телефона", user.getPhoneNumber());
        if (changes.contains("desc"))
            updateItem("Описание", "");
    }

    private void updateItem(String itemName, String newValue) {
        for (int i = 0; i < items.size(); i++) {
            ProfileItem item = items.get(i);
            if (item.getText().equals(itemName)) {
                items.get(i).setValue(newValue);
                notifyItemChanged(i);
            }
        }
    }
}