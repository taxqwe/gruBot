package com.fa.grubot.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fa.grubot.MainActivity;
import com.fa.grubot.R;
import com.fa.grubot.adapters.ActionsPagerAdapter;
import com.fa.grubot.util.NoSwipeViewPager;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;

import static com.fa.grubot.fragments.ActionsFragment.TYPE_ANNOUNCEMENTS;

public class ActionsTabFragment extends BaseFragment implements Serializable {

    @BindView(R.id.viewPager) NoSwipeViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.pagerToolbar) Toolbar pagerToolbar;

    private Unbinder unbinder;
    private int instance = 0;
    private int type;

    public static ActionsTabFragment newInstance(int instance, int type) {
        Bundle args = new Bundle();
        args.putInt("instance", instance);
        args.putInt("type", type);
        ActionsTabFragment fragment = new ActionsTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_actions_tab, container, false);

        setHasOptionsMenu(true);
        type = this.getArguments().getInt("type");
        instance = this.getArguments().getInt("instance");
        unbinder = ButterKnife.bind(this, v);

        hideMainToolbar();
        setupToolbar();
        setupViewPager();
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    private void hideMainToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);

        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    private void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(pagerToolbar);

        String title;
        if (type == TYPE_ANNOUNCEMENTS)
            title = "Объявления";
        else
            title = "Голосования";

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setupViewPager() {
        ActionsPagerAdapter actionsPagerAdapter = new ActionsPagerAdapter(getChildFragmentManager(), type);

        viewPager.setAdapter(actionsPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        unbinder.unbind();
    }
}
