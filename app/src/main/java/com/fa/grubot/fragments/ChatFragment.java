package com.fa.grubot.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fa.grubot.R;
import com.fa.grubot.abstractions.ChatFragmentBase;
import com.fa.grubot.objects.chat.ChatMessage;
import com.fa.grubot.presenters.ChatPresenter;
import com.fa.grubot.util.Globals;
import com.fa.grubot.util.PreferencesStorage;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by ni.petrov on 22/10/2017.
 */

public class ChatFragment extends Fragment implements ChatFragmentBase {

    private ChatPresenter presenter;

    private MessagesListAdapter<ChatMessage> messageAdapter;

    private String senderId;

    private int myId;

    private Unbinder unbinder;

    private boolean isSmartEnabled;

    private PreferencesStorage preferences;

    private Menu menu;

    @BindView(R.id.messagesList)
    MessagesList messagesListView;

    @BindView(R.id.input)
    MessageInput inputView;

    @BindView(R.id.toolbar)
    Toolbar chatToolbar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        setHasOptionsMenu(true);

        unbinder = ButterKnife.bind(this, v);

        init(v);

        setRetainInstance(true);

        return v;
    }

    private void init(View view) {
        preferences = new PreferencesStorage(view.getContext());

        presenter = new ChatPresenter(this);

        chatToolbar.bringToFront();

        ((AppCompatActivity) getActivity()).setSupportActionBar(chatToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Чат");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        ImageLoader imageLoader = new com.fa.grubot.util.ImageLoader(this);

        messageAdapter = new MessagesListAdapter<>(Globals.getMe().getId(), imageLoader); // sender id must be equals id of loggined user
        messagesListView.setAdapter(messageAdapter);

        inputView.setInputListener(input -> {
            // validate and send message here
            ChatMessage message = new ChatMessage("2",
                    inputView.getInputEditText().getText().toString(),
                    Globals.getMe(), new Date());
            messageAdapter.addToStart(message, true);
            presenter.sendMessage(message);
            return true;
        });

        presenter.onNotifyViewCreated();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_chat, menu);
        this.menu = menu;
        refreshMenuItems();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void subscribeOnNewMessages(Observable<ChatMessage> messagesObservable) {
        messagesObservable
                .subscribeOn(AndroidSchedulers.mainThread())

                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(message -> {
                    //todo hardcode
                    if (messagesListView != null) {
                        messageAdapter.addToStart(message,
                                !messagesListView.canScrollVertically(1));
                    }
                });
    }

    @Override
    public void setUserId(int id) {
        myId = id;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.smart_chat_menu_item:
                refreshMenuItems();
                isSmartEnabled = !isSmartEnabled;
                preferences.putBoolean("isSmartEnabled", isSmartEnabled);
                refreshMenuItems();
                Toast.makeText(getActivity(),
                        isSmartEnabled ? "Умный фильтр включен" : "Умный фильтр выключен", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.destroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void refreshMenuItems() {
        isSmartEnabled = preferences.getBoolean("isSmartEnabled", false);

        menu.getItem(0).setIcon(isSmartEnabled ? R.drawable.brain_enabled : R.drawable.brain_disabled);
    }
}
