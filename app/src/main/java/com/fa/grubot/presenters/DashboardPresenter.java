package com.fa.grubot.presenters;


import com.fa.grubot.App;
import com.fa.grubot.abstractions.DashboardFragmentBase;
import com.fa.grubot.fragments.ActionsFragment;
import com.fa.grubot.models.DashboardModel;
import com.fa.grubot.objects.dashboard.DashboardAnnouncement;
import com.fa.grubot.objects.dashboard.DashboardItem;
import com.fa.grubot.objects.dashboard.DashboardVote;
import com.fa.grubot.util.Globals;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;

public class DashboardPresenter {
    private DashboardFragmentBase fragment;
    private DashboardModel model;

    private ArrayList<DashboardItem> items = new ArrayList<DashboardItem>(Arrays.asList(new DashboardAnnouncement(0, 0), new DashboardVote(0, 0)));

    private Query announcementsQuery = FirebaseFirestore.getInstance().collection("announcements").whereEqualTo("users." + App.INSTANCE.getCurrentUser().getId(), "new");
    private Query archiveAnnouncementsQuery = FirebaseFirestore.getInstance().collection("announcements").whereEqualTo("users." + App.INSTANCE.getCurrentUser().getId(), "archive");

    private Query votesQuery = FirebaseFirestore.getInstance().collection("votes").whereEqualTo("users." + App.INSTANCE.getCurrentUser().getId(), "new");
    private Query archiveVotesQuery = FirebaseFirestore.getInstance().collection("votes").whereEqualTo("users." + App.INSTANCE.getCurrentUser().getId(), "archive");

    private ListenerRegistration announcementsRegistration;
    private ListenerRegistration archiveAnnouncementsRegistration;
    private ListenerRegistration votesRegistration;
    private ListenerRegistration archiveVotesRegistration;

    public DashboardPresenter(DashboardFragmentBase fragment){
        this.fragment = fragment;
        this.model = new DashboardModel();
    }

    public void notifyFragmentStarted() {
        fragment.setupToolbar();
        setRegistration();
    }

    private void notifyViewCreated(int state) {
        fragment.showRequiredViews();

        switch (state) {
            case Globals.FragmentState.STATE_CONTENT:
                fragment.setupToolbar();
                fragment.setupRecyclerView(items);
                break;
            case Globals.FragmentState.STATE_NO_INTERNET_CONNECTION:
                fragment.setupRetryButton();
                break;
        }
    }

    @SuppressWarnings("unchecked")
    public void setRegistration() {
        announcementsRegistration = announcementsQuery.addSnapshotListener((documentSnapshots, e) -> {
            if (e == null) {
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    int count = 0;

                    switch (dc.getType()) {
                        case ADDED:
                            count++;
                            break;
                        case REMOVED:
                            count--;
                            break;
                    }

                    if (fragment != null) {
                        if (!fragment.isAdapterExists()) {
                            fragment.setupLayouts(true);
                            notifyViewCreated(Globals.FragmentState.STATE_CONTENT);
                        }

                        fragment.handleListUpdate(count, ActionsFragment.TYPE_ANNOUNCEMENTS);
                    }
                }
            } else {
                if (fragment != null) {
                    fragment.setupLayouts(false);
                    notifyViewCreated(Globals.FragmentState.STATE_NO_INTERNET_CONNECTION);
                }
            }
        });

        archiveAnnouncementsRegistration = archiveAnnouncementsQuery.addSnapshotListener((documentSnapshots, e) -> {
            if (e == null) {
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    int count = 0;

                    switch (dc.getType()) {
                        case ADDED:
                            count++;
                            break;
                        case REMOVED:
                            count--;
                            break;
                    }

                    if (fragment != null) {
                        if (!fragment.isAdapterExists()) {
                            fragment.setupLayouts(true);
                            notifyViewCreated(Globals.FragmentState.STATE_CONTENT);
                        }

                        fragment.handleListUpdate(count, ActionsFragment.TYPE_ANNOUNCEMENTS_ARCHIVE);
                    }
                }
            } else {
                if (fragment != null) {
                    fragment.setupLayouts(false);
                    notifyViewCreated(Globals.FragmentState.STATE_NO_INTERNET_CONNECTION);
                }
            }
        });

        votesRegistration = votesQuery.addSnapshotListener((documentSnapshots, e) -> {
            if (e == null) {
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    int count = 0;

                    switch (dc.getType()) {
                        case ADDED:
                            count++;
                            break;
                        case REMOVED:
                            count--;
                            break;
                    }

                    if (fragment != null) {
                        if (!fragment.isAdapterExists()) {
                            fragment.setupLayouts(true);
                            notifyViewCreated(Globals.FragmentState.STATE_CONTENT);
                        }

                        fragment.handleListUpdate(count, ActionsFragment.TYPE_VOTES);
                    }
                }
            } else {
                if (fragment != null) {
                    fragment.setupLayouts(false);
                    notifyViewCreated(Globals.FragmentState.STATE_NO_INTERNET_CONNECTION);
                }
            }
        });

        archiveVotesRegistration = archiveVotesQuery.addSnapshotListener((documentSnapshots, e) -> {
            if (e == null) {
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    int count = 0;

                    switch (dc.getType()) {
                        case ADDED:
                            count++;
                            break;
                        case REMOVED:
                            count--;
                            break;
                    }

                    if (fragment != null) {
                        if (!fragment.isAdapterExists()) {
                            fragment.setupLayouts(true);
                            notifyViewCreated(Globals.FragmentState.STATE_CONTENT);
                        }

                        fragment.handleListUpdate(count, ActionsFragment.TYPE_VOTES_ARCHIVE);
                    }
                }
            } else {
                if (fragment != null) {
                    fragment.setupLayouts(false);
                    notifyViewCreated(Globals.FragmentState.STATE_NO_INTERNET_CONNECTION);
                }
            }
        });
    }


    public void onRetryBtnClick() {
        setRegistration();
    }

    public void removeRegistration() {
        announcementsRegistration.remove();
        archiveAnnouncementsRegistration.remove();
        votesRegistration.remove();
        archiveVotesRegistration.remove();
    }

    public void destroy() {
        removeRegistration();
        fragment = null;
        model = null;
    }
}
