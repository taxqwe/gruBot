package com.fa.grubot.presenters;


import android.util.Log;

import com.fa.grubot.abstractions.GroupInfoFragmentBase;
import com.fa.grubot.models.GroupInfoModel;
import com.fa.grubot.objects.chat.Chat;
import com.fa.grubot.objects.dashboard.ActionAnnouncement;
import com.fa.grubot.objects.dashboard.ActionPoll;
import com.fa.grubot.objects.misc.VoteOption;
import com.fa.grubot.util.Consts;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class GroupInfoPresenter {
    private GroupInfoFragmentBase fragment;
    private GroupInfoModel model;

    private Query announcementsQuery;
    private Query pollsQuery;

    private ListenerRegistration announcementsRegistration;
    private ListenerRegistration pollsRegistration;

    public GroupInfoPresenter(GroupInfoFragmentBase fragment) {
        this.fragment = fragment;
        this.model = new GroupInfoModel();
    }

    public void notifyFragmentStarted(Chat chat) {
        Long chatId = Long.valueOf(chat.getId());
        String prefix = "";
        if (chat.getType().equals(Consts.Telegram))
            prefix = "-100";

        Log.d("debug", prefix + chatId);
        announcementsQuery = FirebaseFirestore.getInstance().collection("announcements").whereEqualTo("group", Long.valueOf(prefix + chatId));
        pollsQuery = FirebaseFirestore.getInstance().collection("votes").whereEqualTo("group", Long.valueOf(prefix + chatId));

        setRegistration();
    }

    private void notifyViewCreated(int state){
        fragment.showRequiredViews();

        switch (state) {
            case Consts.STATE_CONTENT:
                fragment.setupButtonClickListeners();
                fragment.setupToolbar();
                fragment.setupFab();
                fragment.setupRecyclerView(Consts.TYPE_ANNOUNCEMENT);
                fragment.setupRecyclerView(Consts.TYPE_POLL);
                fragment.setupRecyclerView(Consts.TYPE_ARTICLE);
                fragment.setupRecyclerView(Consts.TYPE_USER);
                break;
            case Consts.STATE_NO_INTERNET_CONNECTION:
                fragment.setupRetryButton();
                break;
        }
    }

    @SuppressWarnings("unchecked")
    public void setRegistration() {
        announcementsRegistration = announcementsQuery.addSnapshotListener((documentSnapshots, e) -> {
            if (e == null) {
                if (fragment != null && documentSnapshots.isEmpty() && !fragment.isOneOfTheAdaptersExists())  {
                    fragment.setupLayouts(true);
                    notifyViewCreated(Consts.STATE_CONTENT);
                }

                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot doc = dc.getDocument();
                    ActionAnnouncement announcement = new ActionAnnouncement(
                                doc.getId(),
                                doc.get("group").toString(),
                                doc.get("groupName").toString(),
                                doc.get("author").toString(),
                                doc.get("authorName").toString(),
                                doc.get("desc").toString(),
                                (Date) doc.get("date"),
                                doc.get("text").toString(),
                                (Map<String, String>) doc.get("users"),
                                (long) doc.get("messageId"));

                    if (fragment != null) {
                        if (!fragment.isOneOfTheAdaptersExists()) {
                            fragment.setupLayouts(true);
                            notifyViewCreated(Consts.STATE_CONTENT);
                        }

                        fragment.handleDataUpdate(Consts.TYPE_ANNOUNCEMENT, dc.getType(), dc.getNewIndex(), dc.getOldIndex(), announcement);
                    }
                }
            } else {
                if (fragment != null) {
                    fragment.setupLayouts(false);
                    notifyViewCreated(Consts.STATE_NO_INTERNET_CONNECTION);
                }
            }
        });

        pollsRegistration = pollsQuery.addSnapshotListener((documentSnapshots, e) -> {
            if (e == null) {
                if (fragment != null && documentSnapshots.isEmpty() && !fragment.isOneOfTheAdaptersExists())  {
                    fragment.setupLayouts(true);
                    notifyViewCreated(Consts.STATE_CONTENT);
                }

                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot doc = dc.getDocument();
                        ArrayList<VoteOption> voteOptions = new ArrayList<>();
                        for (Map.Entry<String, String> option : ((Map<String, String>) doc.get("voteOptions")).entrySet())
                            voteOptions.add(new VoteOption(option.getValue()));

                        ActionPoll poll = new ActionPoll(
                                doc.getId(),
                                doc.get("group").toString(),
                                doc.get("groupName").toString(),
                                doc.get("author").toString(),
                                doc.get("authorName").toString(),
                                doc.get("desc").toString(),
                                (Date) doc.get("date"),
                                voteOptions,
                                (Map<String, String>) doc.get("users"),
                                (long) doc.get("messageId"));

                    if (fragment != null) {
                        if (!fragment.isOneOfTheAdaptersExists()) {
                            fragment.setupLayouts(true);
                            notifyViewCreated(Consts.STATE_CONTENT);
                        }

                        fragment.handleDataUpdate(Consts.TYPE_POLL, dc.getType(), dc.getNewIndex(), dc.getOldIndex(), poll);
                    }
                }
            } else {
                if (fragment != null) {
                    fragment.setupLayouts(false);
                    notifyViewCreated(Consts.STATE_NO_INTERNET_CONNECTION);
                }
            }
        });
    }

    public void removeRegistration() {
        if (announcementsRegistration != null)
            announcementsRegistration.remove();
        if (pollsRegistration != null)
            pollsRegistration.remove();
    }

    public void onRetryBtnClick() {
        setRegistration();
    }

    public void destroy() {
        removeRegistration();
        fragment = null;
        model = null;
    }
}
