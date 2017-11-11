package com.fa.grubot;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fa.grubot.fragments.DashboardFragment;
import com.fa.grubot.fragments.WorkInProgressFragment;
import com.fa.grubot.fragments.GroupsFragment;
import com.fa.grubot.util.BottomNavigationViewHelper;

import java.util.HashMap;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    //@BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;

    private HashMap<String, Stack<Fragment>> mStacks;
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
        //Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
        selectedTab(TAB_DASHBOARD);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Icepick.saveInstanceState(this, outState);
    }

    private void setupViews() {
        mStacks = new HashMap<>();
        mStacks.put(TAB_PROFILE, new Stack<>());
        mStacks.put(TAB_DASHBOARD, new Stack<>());
        mStacks.put(TAB_CHATS, new Stack<>());
        mStacks.put(TAB_SETTINGS, new Stack<>());

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);


        bottomNavigationView.setSelectedItemId(R.id.action_dashboard);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
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
                    case R.id.action_profile:
                        selectedTab(TAB_PROFILE);
                        break;
                    case R.id.action_dashboard:
                        selectedTab(TAB_DASHBOARD);
                        break;
                    case R.id.action_chats:
                        selectedTab(TAB_CHATS);
                        break;
                    case R.id.action_settings:
                        selectedTab(TAB_SETTINGS);
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

        if(mStacks.get(tabId).size() == 0){
            if(tabId.equals(TAB_PROFILE)){
                pushFragments(tabId, new WorkInProgressFragment(),true);
            } else if(tabId.equals(TAB_DASHBOARD)){
                pushFragments(tabId, new DashboardFragment(),true);
            }else if(tabId.equals(TAB_CHATS)){
                pushFragments(tabId, new GroupsFragment(),true);
            }else if(tabId.equals(TAB_SETTINGS)){
                pushFragments(tabId, new WorkInProgressFragment(),true);
            }
        }else {
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
        if(mStacks.get(mCurrentTab).size() == 1){
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                finish();
                return;
            } else {
                Toast.makeText(getBaseContext(), "Нажмите еще раз кнопку 'назад' для выхода", Toast.LENGTH_SHORT).show();
            }
            mBackPressed = System.currentTimeMillis();
            return;
        }

        popFragments();
    }
}
