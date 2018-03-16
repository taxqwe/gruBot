package com.fa.grubot.abstractions;

import com.fa.grubot.objects.Chat;
import com.google.firebase.firestore.DocumentChange;

import java.util.ArrayList;

import io.reactivex.Observable;

public interface ChatsListFragmentBase extends FragmentBase {
    void setupRecyclerView(ArrayList<Chat> chats);
    void subscribeOnChats(Observable<Chat> chatsObservable);
    void handleListUpdate(DocumentChange.Type type, int newIndex, int oldIndex, Chat chat);
    void setupLayouts(boolean isNetworkAvailable, boolean isHasData);
    boolean isAdapterExists();
    boolean isListEmpty();
}
