package com.fa.grubot.helpers;

import android.util.SparseArray;

import com.github.badoualy.telegram.tl.api.TLAbsChat;
import com.github.badoualy.telegram.tl.api.TLAbsPeer;
import com.github.badoualy.telegram.tl.api.TLAbsUser;
import com.github.badoualy.telegram.tl.api.TLChannel;
import com.github.badoualy.telegram.tl.api.TLChannelForbidden;
import com.github.badoualy.telegram.tl.api.TLChat;
import com.github.badoualy.telegram.tl.api.TLChatEmpty;
import com.github.badoualy.telegram.tl.api.TLChatForbidden;
import com.github.badoualy.telegram.tl.api.TLPeerChannel;
import com.github.badoualy.telegram.tl.api.TLPeerChat;
import com.github.badoualy.telegram.tl.api.TLPeerUser;
import com.github.badoualy.telegram.tl.api.TLUser;
import com.github.badoualy.telegram.tl.api.messages.TLAbsDialogs;
import com.github.badoualy.telegram.tl.core.TLVector;

public class Telegram {

    public Telegram() {

    }

    public SparseArray<String> getChatNamesMap(TLAbsDialogs tlAbsDialogs) {
        SparseArray<String> nameMap = new SparseArray<>();

        TLVector<TLAbsUser> users = tlAbsDialogs.getUsers();

        users.forEach(absUser -> {
            TLUser user = absUser.getAsUser();
            nameMap.put(user.getId(), user.getFirstName() + " " + user.getLastName());
        });


        TLVector<TLAbsChat> chats = tlAbsDialogs.getChats();

        chats.forEach(chat -> {
            if (chat instanceof TLChannel) {
                nameMap.put(chat.getId(), ((TLChannel) chat).getTitle());
            } else if (chat instanceof TLChannelForbidden) {
                nameMap.put(chat.getId(), ((TLChannelForbidden) chat).getTitle());
            } else if (chat instanceof TLChat) {
                nameMap.put(chat.getId(), ((TLChat) chat).getTitle());
            } else if (chat instanceof TLChatEmpty) {
                nameMap.put(chat.getId(), "Empty chat");
            } else if (chat instanceof TLChatForbidden) {
                nameMap.put(chat.getId(), ((TLChatForbidden) chat).getTitle());
            }
        });

        return nameMap;
    }

    public int getId(TLAbsPeer peer) {
        if (peer instanceof TLPeerUser)
            return ((TLPeerUser) peer).getUserId();
        if (peer instanceof TLPeerChat)
            return ((TLPeerChat) peer).getChatId();

        return ((TLPeerChannel) peer).getChannelId();
    }
}
