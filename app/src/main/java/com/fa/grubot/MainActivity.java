package com.fa.grubot;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.fa.grubot.fragments.DashboardFragment;
import com.fa.grubot.fragments.GroupsFragment;
import com.fa.grubot.fragments.ProfileFragment;
import com.fa.grubot.fragments.SettingsFragment;
import com.fa.grubot.fragments.WorkInProgressFragment;
import com.fa.grubot.helpers.BottomNavigationViewHelper;
import com.fa.grubot.util.Globals;

import java.util.HashMap;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import io.reactivex.annotations.Nullable;

public class MainActivity extends AppCompatActivity {

    @Nullable @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;

    private HashMap<String, Stack<Fragment>> mStacks;
    public static final String TAB_SEARCH  = "tab_search";
    public static final String TAB_PROFILE  = "tab_profile";
    public static final String TAB_DASHBOARD  = "tab_dashboard";
    public static final String TAB_CHATS  = "tab_chats";
    public static final String TAB_SETTINGS  = "tab_settings";

    private String mCurrentTab;

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setupViews();
        if (savedInstanceState != null) {
            mCurrentTab = savedInstanceState.getString("currentTab");
            mStacks = (HashMap<String, Stack<Fragment>>) savedInstanceState.getSerializable("stacks");
        } else
            selectedTab(TAB_DASHBOARD);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("stacks", mStacks);
        outState.putString("currentTab", mCurrentTab);
        Icepick.saveInstanceState(this, outState);
    }

    private void setupViews() {
        mStacks = new HashMap<>();
        mStacks.put(TAB_SEARCH, new Stack<>());
        mStacks.put(TAB_PROFILE, new Stack<>());
        mStacks.put(TAB_DASHBOARD, new Stack<>());
        mStacks.put(TAB_CHATS, new Stack<>());
        mStacks.put(TAB_SETTINGS, new Stack<>());

        bottomNavigationView.setSelectedItemId(R.id.action_dashboard);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (!Globals.Variables.isBackstackEnabled) {
                mStacks.get(TAB_SEARCH).clear();
                mStacks.get(TAB_PROFILE).clear();
                mStacks.get(TAB_DASHBOARD).clear();
                mStacks.get(TAB_CHATS).clear();
                mStacks.get(TAB_SETTINGS).clear();
            }

            switch (item.getItemId()) {
                case R.id.action_search:
                    selectedTab(TAB_SEARCH);
                    return true;
                case R.id.action_profile:
                    selectedTab(TAB_PROFILE);
                    return true;
                case R.id.action_dashboard:
                    selectedTab(TAB_DASHBOARD);
                    return true;
                case R.id.action_chats:
                    selectedTab(TAB_CHATS);
                    return true;
                case R.id.action_settings:
                    selectedTab(TAB_SETTINGS);
                    return true;
            }
            return true;
        });

        bottomNavigationView.setOnNavigationItemReselectedListener(item -> {
            if (mStacks.get(mCurrentTab).size() != 1) {
                mStacks.get(mCurrentTab).clear();
                switch (item.getItemId()) {
                    case R.id.action_search:
                        selectedTab(TAB_SEARCH);
                        break;
                    case R.id.action_profile:
                        selectedTab(TAB_PROFILE);
                        break;
                    case R.id.action_dashboard:
                        selectedTab(TAB_DASHBOARD);
                        break;
                    case R.id.action_chats:
                        selectedTab(TAB_CHATS);
                        break;
                }
            }
        });
    }

    private void gotoFragment(Fragment selectedFragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, selectedFragment);
        fragmentTransaction.commit();
    }

    private void selectedTab(String tabId) {
        mCurrentTab = tabId;

        if(mStacks.get(tabId).size() == 0) {
            switch (tabId) {
                case TAB_SEARCH:
                    pushFragments(tabId, new WorkInProgressFragment(),true);
                    break;
                case TAB_PROFILE:
                    Fragment fragment = new ProfileFragment();
                    Bundle args = new Bundle();
                    args.putSerializable("user", Globals.getMe());
                    fragment.setArguments(args);
                    pushFragments(tabId, fragment,true);
                    break;
                case TAB_DASHBOARD:
                    pushFragments(tabId, new DashboardFragment(),true);
                    break;
                case TAB_CHATS:
                    pushFragments(tabId, new GroupsFragment(),true);
                    break;
                case TAB_SETTINGS:
                    pushFragments(tabId, new SettingsFragment(),true);
                    break;
            }
        } else {
            pushFragments(tabId, mStacks.get(tabId).lastElement(),false);
        }
    }

    public void pushFragments(String tag, Fragment fragment, boolean shouldAdd){
        if(shouldAdd)
            mStacks.get(tag).push(fragment);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.content, fragment);
        ft.commit();
    }

    public void popFragments(){
        Fragment fragment = mStacks.get(mCurrentTab).elementAt(mStacks.get(mCurrentTab).size() - 2);

        mStacks.get(mCurrentTab).pop();

        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.content, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if(mStacks.get(mCurrentTab).size() == 1) {
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                finish();
                return;
            } else {
                Toast.makeText(getBaseContext(), "Нажмите еще раз кнопку 'назад' для выхода", Toast.LENGTH_SHORT).show();
            }
            mBackPressed = System.currentTimeMillis();
        } else {
            popFragments();
        }
    }
}
