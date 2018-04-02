package com.fa.grubot.abstractions;

import com.fa.grubot.objects.chat.ChatMessage;

import java.util.ArrayList;

public interface MessagesListRequestResponse {
    void onMessagesListResult(ArrayList<ChatMessage> messages, int flag, boolean moveToTop);
}
