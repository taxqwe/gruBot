package com.fa.grubot.abstractions;

import com.fa.grubot.objects.chat.ChatMessage;

public interface ChatMessageSendRequestResponse {
    void onMessageSent(ChatMessage message);
}
