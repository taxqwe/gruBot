package com.fa.grubot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.fa.grubot.objects.group.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadPreferences();
        loadCurrentUser();
    }

    private void loadCurrentUser() {
        FirebaseFirestore.getInstance().collection("users").document("rPcwOpbwWWPMwf0UGz1W").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                User user = new User(doc.getId(),
                        doc.get("username").toString(),
                        doc.get("fullname").toString(),
                        doc.get("phoneNumber").toString(),
                        doc.get("desc").toString(),
                        doc.get("imgUrl").toString());
                App.INSTANCE.setCurrentUser(user);
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            } else {
                Toast.makeText(this, "Ошибка подключения", Toast.LENGTH_LONG).show();
                this.finishAffinity();
            }
        });
    }

    private void loadPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        App.INSTANCE.setAnimationsEnabled(prefs.getBoolean("animationsSwitch", false));
        App.INSTANCE.setBackstackEnabled(prefs.getBoolean("backstackSwitch", false));
        App.INSTANCE.setSlidrEnabled(prefs.getBoolean("slidrSwitch", true));
    }
}
