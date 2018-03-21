package com.fa.grubot.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fa.grubot.App;
import com.fa.grubot.R;
import com.fa.grubot.abstractions.ChatFragmentBase;
import com.fa.grubot.objects.chat.ChatMessage;
import com.fa.grubot.objects.chat.MessagesListParcelable;
import com.fa.grubot.presenters.ChatPresenter;
import com.fa.grubot.util.FragmentState;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;

public class ChatFragment extends Fragment implements ChatFragmentBase, Serializable {

    @BindView(R.id.messagesList) MessagesListParcelable messagesListView;
    @BindView(R.id.input) MessageInput inputView;
    @BindView(R.id.toolbar) Toolbar chatToolbar;

    private ChatPresenter presenter;
    private MessagesListAdapter<ChatMessage> messageAdapter;
    private Unbinder unbinder;

    private int state;
    String chatId;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    public static ChatsListFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("chatId", id);
        ChatsListFragment fragment = new ChatsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        presenter = new ChatPresenter(this, getActivity());
        setHasOptionsMenu(true);
        unbinder = ButterKnife.bind(this, v);
        chatId = this.getArguments().getString("chatId");

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.notifyFragmentStarted();
    }

    @Override
    public void onPause() {
        App.INSTANCE.closeTelegramClient();
        super.onPause();
    }

    @Override
    public void onStop() {
        App.INSTANCE.closeTelegramClient();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        App.INSTANCE.closeTelegramClient();
        presenter.destroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    public void setupLayouts(boolean isNetworkAvailable, boolean isHasData) {
        if (isNetworkAvailable) {
            if (isHasData)
                state = FragmentState.STATE_CONTENT;
            else {
                state = FragmentState.STATE_NO_DATA;
                messageAdapter = null;
            }
        }
        else {
            state = FragmentState.STATE_NO_INTERNET_CONNECTION;
            messageAdapter = null;
        }
    }

    public void setupToolbar(String chatName) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(chatToolbar);
        activity.getSupportActionBar().setTitle(chatName);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
