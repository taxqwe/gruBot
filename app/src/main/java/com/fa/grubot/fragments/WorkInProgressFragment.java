package com.fa.grubot.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fa.grubot.R;

import java.io.Serializable;

public class WorkInProgressFragment extends Fragment implements Serializable {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_work_in_progress, container, false);

        return v;
    }
}
