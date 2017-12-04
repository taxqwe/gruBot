package com.fa.grubot.presenters;


import com.fa.grubot.abstractions.ProfileFragmentBase;
import com.fa.grubot.models.ProfileModel;
import com.fa.grubot.objects.group.User;
import com.fa.grubot.objects.misc.ProfileItem;
import com.fa.grubot.util.Globals;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

public class ProfilePresenter {
    private ProfileFragmentBase fragment;
    private ProfileModel model;

    private ArrayList<ProfileItem> items = new ArrayList<>();
    private User localUser;

    private DocumentReference userReference;
    private ListenerRegistration userRegistration;

    public ProfilePresenter(ProfileFragmentBase fragment) {
        this.fragment = fragment;
        this.model = new ProfileModel();
    }

    public void notifyFragmentStarted(String id) {
        userReference = FirebaseFirestore.getInstance().collection("users").document(id);
        setRegistration();
    }

    private void notifyViewCreated(int state) {
        fragment.showRequiredViews();

        switch (state) {
            case Globals.FragmentState.STATE_CONTENT:
                fragment.setupToolbar(localUser);
                fragment.setupRecyclerView(items, localUser);
                break;
            case Globals.FragmentState.STATE_NO_INTERNET_CONNECTION:
                fragment.setupRetryButton();
                break;
        }
    }

    @SuppressWarnings("unchecked")
    public void setRegistration() {
        userRegistration = userReference.addSnapshotListener((doc, e) -> {
            if (e == null) {
                User user = new User(doc.getId(),
                        doc.get("username").toString(),
                        doc.get("fullname").toString(),
                        doc.get("phoneNumber").toString(),
                        doc.get("desc").toString(),
                        doc.get("imgUrl").toString());

                ArrayList<String> changes = new ArrayList<>();
                if (localUser != null) {
                    if (!user.getUsername().equals(localUser.getUsername()))
                        changes.add("username");
                    if (!user.getFullname().equals(localUser.getFullname()))
                        changes.add("fullname");
                    if (!user.getPhoneNumber().equals(localUser.getPhoneNumber()))
                        changes.add("phoneNumber");
                    if (!user.getDesc().equals(localUser.getDesc()))
                        changes.add("desc");
                    if (!user.getAvatar().equals(localUser.getAvatar()))
                        changes.add("avatar");
                }

                localUser = user;

                if (fragment != null) {
                    if (!fragment.isAdapterExists()) {
                        fragment.setupLayouts(true);
                        notifyViewCreated(Globals.FragmentState.STATE_CONTENT);
                    }

                    fragment.handleProfileUpdate(user, changes);
                }
            } else {
                if (fragment != null) {
                    fragment.setupLayouts(false);
                    notifyViewCreated(Globals.FragmentState.STATE_NO_INTERNET_CONNECTION);
                }
            }
        });
    }

    public void removeRegistration() {
        userRegistration.remove();
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
