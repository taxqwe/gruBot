package com.fa.grubot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.fa.grubot.objects.group.CurrentUser;
import com.fa.grubot.util.TmApiStorage;
import com.github.badoualy.telegram.api.Kotlogram;
import com.github.badoualy.telegram.api.TelegramClient;
import com.github.badoualy.telegram.tl.api.TLInputUserSelf;
import com.github.badoualy.telegram.tl.api.TLUser;
import com.github.badoualy.telegram.tl.api.TLUserFull;
import com.github.badoualy.telegram.tl.api.auth.TLAuthorization;
import com.github.badoualy.telegram.tl.exception.RpcErrorException;

import java.lang.ref.WeakReference;

import static com.fa.grubot.App.INSTANCE;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadPreferences();
        (new TryToLoginAsyncTask(this)).execute();
    }

    private void loadPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        INSTANCE.setAnimationsEnabled(prefs.getBoolean("animationsSwitch", false));
        INSTANCE.setBackstackEnabled(prefs.getBoolean("backstackSwitch", false));
        INSTANCE.setSlidrEnabled(prefs.getBoolean("slidrSwitch", true));
    }

    private static class TryToLoginAsyncTask extends AsyncTask<Void, Void, Object> {
        private WeakReference<Context> context;

        private TryToLoginAsyncTask(Context context) {
            this.context = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Void... params) {
            Object returnObject;

            TelegramClient client = App.INSTANCE.getNewTelegramClient();

            try {
                TLUserFull userFull = client.usersGetFullUser(new TLInputUserSelf());
                returnObject = userFull.getUser().getAsUser();
            } catch (RpcErrorException e) {
                returnObject = e;
            } catch (Exception e) {
                returnObject = e;
            } finally {
                //client.close();
            }
            return returnObject;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (result instanceof Exception) {
                Toast.makeText(context.get(), ((RpcErrorException) result).getTag(), Toast.LENGTH_SHORT).show();
                context.get().startActivity(new Intent(context.get(), LoginActivity.class));
            } else {
                App.INSTANCE.setCurrentUser(new CurrentUser((TLUser) result, null));
                App.INSTANCE.closeTelegramClient();
                context.get().startActivity(new Intent(context.get(), MainActivity.class));
            }

            ((SplashActivity) context.get()).finish();

            super.onPostExecute(result);
        }
    }
}
