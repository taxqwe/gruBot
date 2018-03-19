package com.fa.grubot.abstractions;

import com.fa.grubot.objects.chat.Chat;

import java.util.ArrayList;

public interface ChatsListFragmentBase extends FragmentBase {
    void setupRecyclerView(ArrayList<Chat> chats);
    void setupLayouts(boolean isNetworkAvailable, boolean isHasData);
    void updateChatsList(ArrayList<Chat> chats, boolean moveToTop);
    boolean isAdapterExists();
    boolean isListEmpty();
}
