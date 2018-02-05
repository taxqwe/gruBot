package com.fa.grubot.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
    FragmentNavigation fragmentNavigation;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigation) {
            fragmentNavigation = (FragmentNavigation) context;
        }
    }

    public interface FragmentNavigation {
        public void pushFragment(Fragment fragment);
    }
}
