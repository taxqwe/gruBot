package com.fa.grubot.util;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import jp.wasabeef.glide.transformations.BlurTransformation;

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
            .transition(DrawableTransitionOptions.withCrossFade(400))
            .apply(new RequestOptions().centerCrop().fitCenter())
            .apply(RequestOptions.bitmapTransform(new BlurTransformation(25)))
            .into(imageView);
    }

    public String getUriOfDrawable(int drawable) {
        Context context = fragment.getActivity();
        Resources res = context.getResources();
        return  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(drawable)
                + '/' + res.getResourceTypeName(drawable)
                + '/' + res.getResourceEntryName(drawable)).toString();
    }
}
