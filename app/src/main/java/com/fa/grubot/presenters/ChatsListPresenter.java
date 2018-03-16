package com.fa.grubot.presenters;


import com.fa.grubot.App;
import com.fa.grubot.abstractions.ChatsListFragmentBase;
import com.fa.grubot.models.ChatsListModel;
import com.fa.grubot.objects.Chat;
import com.fa.grubot.util.FragmentState;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Map;

public class ChatsListPresenter {

    private ChatsListFragmentBase fragment;
    private ChatsListModel model;

    private ArrayList<Chat> chats = new ArrayList<>();

    private Query groupsQuery = FirebaseFirestore.getInstance().collection("chats").whereEqualTo("users." + App.INSTANCE.getCurrentUser().getTelegramUser().getId(), true);
    private ListenerRegistration groupsRegistration;

    public ChatsListPresenter(ChatsListFragmentBase fragment) {
        this.fragment = fragment;
        this.model = new ChatsListModel();
    }

    public void notifyFragmentStarted() {
        fragment.setupToolbar();
        setRegistration();
    }

    private void notifyViewCreated(int state) {
        fragment.showRequiredViews();

        switch (state) {
            case FragmentState.STATE_CONTENT:
                fragment.setupRecyclerView(chats);
                break;
            case FragmentState.STATE_NO_INTERNET_CONNECTION:
                fragment.setupRetryButton();
                break;
            case FragmentState.STATE_NO_DATA:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    public void setRegistration() {
        groupsRegistration = groupsQuery.addSnapshotListener((documentSnapshots, e) -> {
            if (e == null) {
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot doc = dc.getDocument();
                    Chat chat = new Chat(doc.get("chatId").toString(), doc.get("name").toString(), (Map<String, Boolean>) doc.get("users"), doc.get("imgUrl").toString());

                    if (fragment != null) {
                        if (!fragment.isAdapterExists() && fragment.isListEmpty()) {
                            fragment.setupLayouts(true, true);
                            notifyViewCreated(FragmentState.STATE_CONTENT);
                        }

                        fragment.handleListUpdate(dc.getType(), dc.getNewIndex(), dc.getOldIndex(), chat);
                    }
                }

                if (fragment != null && fragment.isListEmpty()) {
                    fragment.setupLayouts(true, false);
                    notifyViewCreated(FragmentState.STATE_NO_DATA);
                }
            } else {
                if (fragment != null) {
                    fragment.setupLayouts(false, false);
                    notifyViewCreated(FragmentState.STATE_NO_INTERNET_CONNECTION);
                }
            }
        });
    }

    public void onRetryBtnClick() {
        setRegistration();
    }

    public void removeRegistration() {
        if (groupsRegistration != null)
            groupsRegistration.remove();
    }

    public void destroy() {
        removeRegistration();
        fragment = null;
        model = null;
    }
}
