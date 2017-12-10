package com.fa.grubot;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.fa.grubot.fragments.BaseFragment;
import com.fa.grubot.fragments.DashboardFragment;
import com.fa.grubot.fragments.GroupsFragment;
import com.fa.grubot.fragments.ProfileFragment;
import com.fa.grubot.fragments.SettingsFragment;
import com.fa.grubot.fragments.WorkInProgressFragment;
import com.fa.grubot.helpers.BottomNavigationViewHelper;
import com.ncapdevi.fragnav.FragNavController;
import com.ncapdevi.fragnav.tabhistory.FragNavTabHistoryController;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import io.reactivex.annotations.Nullable;

public class MainActivity extends AppCompatActivity implements BaseFragment.FragmentNavigation, FragNavController.TransactionListener, FragNavController.RootFragmentListener {

    @Nullable @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;

    private final int TAB_SEARCH = FragNavController.TAB1;
    private final int TAB_PROFILE = FragNavController.TAB2;
    private final int TAB_DASHBOARD = FragNavController.TAB3;
    private final int TAB_CHATS = FragNavController.TAB4;
    private final int TAB_SETTINGS = FragNavController.TAB5;

    private FragNavController navController;

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setupViews(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
        if (navController != null) {
            navController.onSaveInstanceState(outState);
        }
    }

    private void setupViews(Bundle savedInstanceState) {
        navController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.content)
                .transactionListener(this)
                .rootFragmentListener(this, 5)
                .popStrategy(FragNavTabHistoryController.UNIQUE_TAB_HISTORY)
                .switchController((index, transactionOptions) -> bottomNavigationView.setSelectedItemId(index))
                .build();

        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_search:
                    navController.switchTab(TAB_SEARCH);
                    return true;
                case R.id.action_profile:
                    navController.switchTab(TAB_PROFILE);
                    return true;
                case R.id.action_dashboard:
                    navController.switchTab(TAB_DASHBOARD);
                    return true;
                case R.id.action_chats:
                    navController.switchTab(TAB_CHATS);
                    return true;
                case R.id.action_settings:
                    navController.switchTab(TAB_SETTINGS);
                    return true;
            }
            return true;
        });

        bottomNavigationView.setOnNavigationItemReselectedListener(item -> {
            navController.clearStack();
        });

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.action_dashboard);
        }
    }

    @Override
    public void onBackPressed() {
        if (!navController.popFragment()) {
            super.onBackPressed();
        }
    }

    @Override
    public void pushFragment(Fragment fragment) {
        if (navController != null) {
            if (App.INSTANCE.isBackstackEnabled())
                navController.pushFragment(fragment);
        }
    }

    @Override
    public void onTabTransaction(Fragment fragment, int index) {
        // If we have a backstack, show the back button
        if (getSupportActionBar() != null && navController != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(!navController.isRootFragment());
        }
    }

    @Override
    public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {
        //do fragmentty stuff. Maybe change title, I'm not going to tell you how to live your life
        // If we have a backstack, show the back button
        if (getSupportActionBar() != null && navController != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(!navController.isRootFragment());
        }
    }

    @Override
    public Fragment getRootFragment(int index) {
        switch (index) {
            case TAB_SEARCH:
                return new WorkInProgressFragment();
            case TAB_PROFILE:
                return ProfileFragment.newInstance(0, App.INSTANCE.getCurrentUser());
            case TAB_DASHBOARD:
                return DashboardFragment.newInstance(0);
            case TAB_CHATS:
                return GroupsFragment.newInstance(0);
            case TAB_SETTINGS:
                return new SettingsFragment();
        }
        throw new IllegalStateException("Need to send an index that we know");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navController.popFragment();
                break;
        }
        return true;
    }
}
