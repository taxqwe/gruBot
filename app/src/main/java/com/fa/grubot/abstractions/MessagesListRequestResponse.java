package com.fa.grubot.abstractions;

import com.fa.grubot.objects.misc.CombinedMessagesListObject;

public interface MessagesListRequestResponse {
    void onMessagesListResult(CombinedMessagesListObject combinedMessagesListObject, int flag, boolean moveToTop);
}
