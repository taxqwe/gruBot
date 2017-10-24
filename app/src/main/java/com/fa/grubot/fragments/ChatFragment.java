package com.fa.grubot.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fa.grubot.R;
import com.fa.grubot.abstractions.FragmentBase;
import com.fa.grubot.adapters.ChatsAdapter;
import com.fa.grubot.presenters.ChatsPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ni.petrov on 22/10/2017.
 */

public class ChatFragment extends Fragment implements FragmentBase {

    private ChatsPresenter presenter;

    private ChatsAdapter adapter;

    private LinearLayoutManager mLayoutManager;

    @BindView(R.id.chatRecycler) RecyclerView chatRecycler;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        unbinder = ButterKnife.bind(this, v);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        presenter = new ChatsPresenter(this);
        presenter.notifyViewCreated();

        return v;
    }


    @Override
    public void setupRecyclerView(ArrayList data) {
        chatRecycler.setLayoutManager(mLayoutManager);
        chatRecycler.setHasFixedSize(false);

        adapter = new ChatsAdapter(getActivity(), data);
        chatRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void smth(){}

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        presenter.destroy();
    }
}
