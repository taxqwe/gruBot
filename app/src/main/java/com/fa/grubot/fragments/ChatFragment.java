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
import com.fa.grubot.objects.ChatMessage;
import com.fa.grubot.presenters.ChatPresenter;
import com.fa.grubot.util.Globals;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by ni.petrov on 22/10/2017.
 */

public class ChatFragment extends Fragment implements ChatFragmentBase {

    private ChatPresenter presenter;

    private MessagesListAdapter<ChatMessage> messageAdapter;

    private String senderId;

    private MessagesList messagesListView;

    private MessageInput inputView;

    private Toolbar chatToolbar;

    private int myId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        setHasOptionsMenu(true);
        init(v);

        return v;
    }

    private void init(View view) {
        presenter = new ChatPresenter(this);

        messagesListView = view.findViewById(R.id.messagesList);
        inputView = view.findViewById(R.id.input);

        chatToolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(chatToolbar);

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
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_chat, menu);
    }

    @Override
    public void subscribeOnNewMessages(Observable<ChatMessage> messagesObservable) {
        messagesObservable
                .subscribeOn(AndroidSchedulers.mainThread())

                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(message -> {
                    messageAdapter.addToStart(message,
                            !messagesListView.canScrollVertically(1));
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
            case R.id.enable_smart:
                Toast.makeText(getActivity(), "Smart Chat enabled", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
