package com.fa.grubot.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.fa.grubot.objects.group.User;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

public class Globals {
    public static class Variables {
        public static boolean isBackstackEnabled = true;
        public static boolean areAnimationsEnabled = true;
    }

    public static class ImageMethods {
        /**
         * Метод, создающий круглую картинку первой
         * @param context Контекст Activity.
         * @param name Отображаемое имя.
         * @return Возвращает картинку (класс TextDrawable) с первой буквой по центру.
         */
        public static TextDrawable getRoundImage(Context context, String name) {
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(name);

            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .useFont(Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf"))
                    .bold()
                    .withBorder(2)
                    .endConfig()
                    .buildRound(String.valueOf(name.charAt(0)).toUpperCase(), color);
            return drawable;
        }
    }

    public static class InternetMethods {
        public static boolean isNetworkAvailable(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        }
    }

    public static class FragmentState {
        public static final int STATE_NO_INTERNET_CONNECTION = 61;
        public static final int STATE_NO_DATA = 52;
        public static final int STATE_CONTENT = 44;
    }

    public static User getMe() {
        return new User(0, "DOMINATOR48RUS", "The First One", "+71903322233", "NOONE CAN STOP ME", "http://www.netlore.ru/upload/files/1307/3_321.jpg");
    }

    public static class Config {
        public static SlidrConfig getSlidrConfig() {
            return new SlidrConfig.Builder()
                .position(SlidrPosition.LEFT)
                .sensitivity(0.7f)
                .scrimColor(Color.BLACK)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(2400)
                .distanceThreshold(0.5f)
                .edge(false)
                .build();
        }
    }
}
