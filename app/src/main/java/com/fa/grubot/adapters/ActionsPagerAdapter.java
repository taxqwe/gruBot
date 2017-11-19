package com.fa.grubot.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.fa.grubot.fragments.ActionsFragment;

public class ActionsPagerAdapter extends FragmentStatePagerAdapter {

    private int numberOfTabs;
    private int type;

    public ActionsPagerAdapter(FragmentManager fragmentManager, int numberOfTabs, int type) {
        super(fragmentManager);
        this.numberOfTabs = numberOfTabs;
        this.type = type;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Fragment actionsFragment = new ActionsFragment();
                Bundle args = new Bundle();

                if (type == ActionsFragment.TYPE_ANNOUNCEMENTS)
                    args.putInt("type", ActionsFragment.TYPE_ANNOUNCEMENTS);
                else
                    args.putInt("type", ActionsFragment.TYPE_VOTES);

                actionsFragment.setArguments(args);
                return actionsFragment;
            case 1:
                Fragment actionsArchiveFragment = new ActionsFragment();
                Bundle args1 = new Bundle();

                if (type == ActionsFragment.TYPE_ANNOUNCEMENTS)
                    args1.putInt("type", ActionsFragment.TYPE_ANNOUNCEMENTS_ARCHIVE);
                else
                    args1.putInt("type", ActionsFragment.TYPE_VOTES_ARCHIVE);

                actionsArchiveFragment.setArguments(args1);
                return actionsArchiveFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Текущие";
            case 1:
                return "Архив";
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
