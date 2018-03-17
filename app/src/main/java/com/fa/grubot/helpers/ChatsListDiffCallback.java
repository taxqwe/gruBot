package com.fa.grubot.helpers;

import android.support.v7.util.DiffUtil;
import android.text.TextUtils;

import com.fa.grubot.objects.chat.Chat;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ChatsListDiffCallback extends DiffUtil.Callback {
    private final ArrayList<Chat> oldChatsList;
    private final ArrayList<Chat> newChatsList;

    public ChatsListDiffCallback(ArrayList<Chat> oldChatsList, ArrayList<Chat> newChatsList) {
        this.oldChatsList = oldChatsList;
        this.newChatsList = newChatsList;
    }

    @Override
    public int getOldListSize() {
        return oldChatsList.size();
    }

    @Override
    public int getNewListSize() {
        return newChatsList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldChatsList.get(oldItemPosition).getId().equals(newChatsList.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Chat oldChat = oldChatsList.get(oldItemPosition);
        final Chat newChat = newChatsList.get(newItemPosition);


        return TextUtils.equals(oldChat.getId(), newChat.getId()) &&
                TextUtils.equals(oldChat.getName(), newChat.getName()) &&
                TextUtils.equals(oldChat.getName(), newChat.getName()) &&
                TextUtils.equals(oldChat.getLastMessage(), newChat.getLastMessage()) &&
                TextUtils.equals(oldChat.getImgURI(), newChat.getImgURI());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }


}
