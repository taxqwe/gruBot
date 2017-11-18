package com.fa.grubot;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fa.grubot.fragments.ActionsFragment;
import com.fa.grubot.fragments.GroupsFragment;
import com.fa.grubot.util.Globals;
import com.r0adkll.slidr.Slidr;

import icepick.Icepick;

public class ItemsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

        setContentView(R.layout.activity_dashboard);
        Slidr.attach(this, Globals.Config.getSlidrConfig());

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        int type = getIntent().getExtras().getInt("type");

        Fragment fragment;
        switch (type) {
            case 0:
                fragment = new GroupsFragment();
                break;
            default:
                fragment = new ActionsFragment();
                Bundle args = new Bundle();
                args.putInt("type", type);
                fragment.setArguments(args);
                break;
        }


        fragmentTransaction.replace(R.id.content, fragment).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}
