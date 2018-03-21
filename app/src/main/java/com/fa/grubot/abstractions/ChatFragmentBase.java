package com.fa.grubot.abstractions;

public interface ChatFragmentBase {
    //void setupRecyclerView(ArrayList<Chat> chats);
    void setupLayouts(boolean isNetworkAvailable, boolean isHasData);
    void setupToolbar(String chatName);
    //void updateChatsList();
    //boolean isAdapterExists();
    //boolean isListEmpty();
}
