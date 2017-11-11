package com.fa.grubot.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fa.grubot.R;
import com.fa.grubot.objects.group.Group;
import com.fa.grubot.presenters.GroupInfoPresenter;

import butterknife.ButterKnife;

public class WorkInProgressFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_work_in_progress, container, false);

        return v;
    }
}
