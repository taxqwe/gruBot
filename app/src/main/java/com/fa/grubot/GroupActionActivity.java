package com.fa.grubot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.fa.grubot.objects.DashboardEntry;
import com.r0adkll.slidr.Slidr;

import butterknife.ButterKnife;
import icepick.Icepick;

public class GroupActionActivity extends AppCompatActivity {

    private DashboardEntry dashboardEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Slidr.attach(this);
        Icepick.restoreInstanceState(this, savedInstanceState);
        ButterKnife.bind(this);

        dashboardEntry = (DashboardEntry) getIntent().getExtras().getSerializable("dashboardEntry");

        Fragment fragment = null;
        Class fragmentClass;

        switch (dashboardEntry.getType()) {
            case (DashboardEntry.TYPE_ANNOUNCEMENT):
                //fragmentClass = AnnouncementFragment.class;
                break;
            case (DashboardEntry.TYPE_VOTE):
                //fragmentClass = VoteFragment.class;
                break;
            default:
                fragmentClass = null;
                break;
        }

        try {
            //fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }
}
