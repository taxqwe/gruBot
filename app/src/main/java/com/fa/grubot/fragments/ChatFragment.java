package com.fa.grubot.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fa.grubot.App;
import com.fa.grubot.R;
import com.fa.grubot.abstractions.ChatFragmentBase;
import com.fa.grubot.objects.chat.ChatMessage;
import com.fa.grubot.objects.chat.MessagesListParcelable;
import com.fa.grubot.presenters.ChatPresenter;
import com.fa.grubot.util.Globals;
import com.fa.grubot.util.PreferencesStorage;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by ni.petrov on 22/10/2017.
 */

public class ChatFragment extends Fragment implements ChatFragmentBase, Serializable {

    private ChatPresenter presenter;

    private MessagesListAdapter<ChatMessage> messageAdapter;

    private String senderId;

    private int myId;

    private Unbinder unbinder;

    private boolean isSmartEnabled;

    private PreferencesStorage preferences;

    private Menu menu;

    private ArrayList<ChatMessage> messages;

    @BindView(R.id.messagesList)
    MessagesListParcelable messagesListView;

    @BindView(R.id.input)
    MessageInput inputView;

    @BindView(R.id.toolbar)
    Toolbar chatToolbar;

    private Disposable messagesDisposable;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        setHasOptionsMenu(true);

        Icepick.restoreInstanceState(this, savedInstanceState);

        unbinder = ButterKnife.bind(this, v);

        init(v);


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

        messageAdapter = new MessagesListAdapter<>(App.INSTANCE.getCurrentUser().getId(), imageLoader); // sender id must be equals id of loggined user
        messagesListView.setAdapter(messageAdapter);

        inputView.setInputListener(input -> {
            // validate and send message here
            ChatMessage message = new ChatMessage("2",
                    inputView.getInputEditText().getText().toString(),
                    App.INSTANCE.getCurrentUser(), new Date());
            messageAdapter.addToStart(message, true);
            presenter.sendMessage(message);
            return true;
        });

        messages = new ArrayList<>();

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
        setRetainInstance(true);
    }

    @Override
    public void subscribeOnNewMessages(Observable<ChatMessage> messagesObservable) {
        messagesDisposable = messagesObservable
                .subscribeOn(AndroidSchedulers.mainThread())

                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(message -> {
                    //messages.add(message);
                    //todo hardcode
                    if (messagesListView != null) {
                        messageAdapter.addToStart(message,
                                !messagesListView.canScrollVertically(1));
                    }

                    Log.d("BUG1", "count of items: " + messageAdapter.getItemCount());
                });
    }

    @Override
    public void setUserId(int id) {
        myId = id;
    }

    @Override
    public void drawMessage(ChatMessage msg, boolean needToScroll) {
        messageAdapter.addToStart(msg, needToScroll);
    }

    @Override
    public void drawCachedMessages(List<ChatMessage> messages) {
        messageAdapter.addToEnd(messages, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            //todo REMOVE IS SMART ENABLE LOGIC TO PRESENTER
            case R.id.smart_chat_menu_item:
                refreshMenuItems();
                isSmartEnabled = !isSmartEnabled;
                preferences.putBoolean("isSmartEnabled", isSmartEnabled);
                refreshMenuItems();
                Toast.makeText(getActivity(),
                        isSmartEnabled ? "Умный фильтр включен" : "Умный фильтр выключен", Toast.LENGTH_SHORT).show();
                presenter.notifySmartFilterStatusChanged(isSmartEnabled);
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
        messagesDisposable.dispose();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Icepick.saveInstanceState(this, outState);
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
