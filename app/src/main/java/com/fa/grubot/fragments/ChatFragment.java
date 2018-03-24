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
import android.widget.Button;
import android.widget.ProgressBar;

import com.fa.grubot.App;
import com.fa.grubot.R;
import com.fa.grubot.abstractions.ChatFragmentBase;
import com.fa.grubot.objects.chat.Chat;
import com.fa.grubot.objects.chat.ChatMessage;
import com.fa.grubot.objects.chat.MessagesListParcelable;
import com.fa.grubot.presenters.ChatPresenter;
import com.fa.grubot.util.FragmentState;
import com.fa.grubot.util.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;
import io.reactivex.annotations.Nullable;

public class ChatFragment extends Fragment implements ChatFragmentBase, Serializable {

    @Nullable @BindView(R.id.messagesList) MessagesListParcelable messagesList;
    @Nullable @BindView(R.id.input) MessageInput inputView;
    @Nullable @BindView(R.id.toolbar) Toolbar toolbar;

    @Nullable @BindView(R.id.retryBtn) Button retryBtn;

    @Nullable @BindView(R.id.progressBar) ProgressBar progressBar;
    @Nullable @BindView(R.id.content) View content;
    @Nullable @BindView(R.id.noInternet) View noInternet;
    @Nullable @BindView(R.id.noData) View noData;

    private ChatPresenter presenter;
    private MessagesListAdapter<ChatMessage> messagesListAdapter;
    private Unbinder unbinder;

    private int state;
    private String chatId;
    private String chatTitle;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    public static ChatFragment newInstance(String chatId, String chatTitle) {
        Bundle args = new Bundle();
        args.putString("chatId", chatId);
        args.putString("chatTitle", chatTitle);
        ChatFragment fragment = new ChatFragment();
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
        chatTitle = this.getArguments().getString("chatTitle");

        presenter.notifyFragmentStarted(chatId);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
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
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    public void showRequiredViews() {
        progressBar.setVisibility(View.GONE);
        noInternet.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
        content.setVisibility(View.GONE);

        switch (state) {
            case FragmentState.STATE_CONTENT:
                content.setVisibility(View.VISIBLE);
                break;
            case FragmentState.STATE_NO_INTERNET_CONNECTION:
                noInternet.setVisibility(View.VISIBLE);
                break;
            case FragmentState.STATE_NO_DATA:
                noData.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setupLayouts(boolean isNetworkAvailable, boolean isHasData) {
        if (isNetworkAvailable) {
            if (isHasData)
                state = FragmentState.STATE_CONTENT;
            else {
                state = FragmentState.STATE_NO_DATA;
                messagesListAdapter = null;
            }
        }
        else {
            state = FragmentState.STATE_NO_INTERNET_CONNECTION;
            messagesListAdapter = null;
        }
    }

    public void updateMessagesList(ArrayList<ChatMessage> messages, boolean moveToTop) {
        /*if (isAdapterExists()) {
            messagesListAdapter.updateMessagesList(messages);

            if (moveToTop)
                messagesList.scrollToPosition(0);
        }*/
    }

    public void setupRecyclerView(ArrayList<ChatMessage> messages) {
        MessageHolders holdersConfig = new MessageHolders()
                .setIncomingTextLayout(R.layout.item_custom_incoming_text_message)
                .setOutcomingTextLayout(R.layout.item_custom_outcoming_text_message)
                .setIncomingImageLayout(R.layout.item_custom_incoming_image_message)
                .setOutcomingImageLayout(R.layout.item_custom_outcoming_image_message);

        ImageLoader imageLoader = new ImageLoader(this);

        messagesListAdapter = new MessagesListAdapter<>(String.valueOf(App.INSTANCE.getCurrentUser().getTelegramUser().getId()), holdersConfig, imageLoader);
        messagesListAdapter.addToEnd(messages, false);
        messagesList.setAdapter(messagesListAdapter);
    }


    public void setupToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(chatTitle);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.bringToFront();
    }

    public void setupRetryButton() {
        retryBtn.setOnClickListener(view -> presenter.onRetryBtnClick());
    }

    public boolean isListEmpty() {
        return messagesListAdapter == null || messagesListAdapter.getItemCount() == 0;
    }

    public boolean isAdapterExists() {
        return messagesListAdapter != null;
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
