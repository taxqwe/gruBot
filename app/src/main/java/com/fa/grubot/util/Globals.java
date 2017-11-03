package com.fa.grubot.util;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

public class Globals {
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
}
