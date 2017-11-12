package com.fa.grubot.util;

import android.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by ni.petrov on 04/11/2017.
 */

public class ImageLoader implements com.stfalcon.chatkit.commons.ImageLoader {

    Fragment fragment;

    public ImageLoader(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void loadImage(ImageView imageView, String url) {
        Glide.with(fragment).load(url).into(imageView);
    }

    public void loadToolbarImage(ImageView imageView, String url) {
        Glide
                .with(fragment)
                .load(url)
                .apply(new RequestOptions().centerCrop().fitCenter())
                .into(imageView);
    }
}
