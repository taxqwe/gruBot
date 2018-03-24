package com.fa.grubot.abstractions;

import com.fa.grubot.objects.chat.ChatMessage;

import java.util.ArrayList;

public interface ChatFragmentBase extends FragmentBase {
    void setupRecyclerView(ArrayList<ChatMessage> messages);
    void setupLayouts(boolean isNetworkAvailable, boolean isHasData);
    void updateMessagesList(ArrayList<ChatMessage> messages, boolean moveToTop);
    boolean isAdapterExists();
    boolean isListEmpty();
}
