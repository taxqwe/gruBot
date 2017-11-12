package com.fa.grubot.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.fa.grubot.R;
import com.fa.grubot.abstractions.ActionsFragmentBase;
import com.fa.grubot.abstractions.ProfileFragmentBase;
import com.fa.grubot.adapters.ActionsRecyclerAdapter;
import com.fa.grubot.objects.dashboard.Action;
import com.fa.grubot.objects.dashboard.ActionAnnouncement;
import com.fa.grubot.objects.group.User;
import com.fa.grubot.presenters.ActionsPresenter;
import com.fa.grubot.presenters.ProfilePresenter;
import com.fa.grubot.util.Globals;
import com.fa.grubot.util.ImageLoader;
import com.fa.grubot.util.RecyclerItemTouchHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.Nullable;

public class ProfileFragment extends Fragment implements ProfileFragmentBase {

    @Nullable @BindView(R.id.toolbar) Toolbar toolbar;
    @Nullable @BindView(R.id.retryBtn) Button retryBtn;
    @Nullable @BindView(R.id.userImage) ImageView userImage;

    private Unbinder unbinder;
    private ProfilePresenter presenter;
    private int layout;

    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new ProfilePresenter(this);
        user = (User) this.getArguments().getSerializable("user");
        setHasOptionsMenu(true);
        presenter.notifyFragmentStarted(getActivity());
        View v = inflater.inflate(layout, container, false);

        unbinder = ButterKnife.bind(this, v);
        presenter.notifyViewCreated(layout, v);

        return v;
    }

    public void setupLayouts(boolean isNetworkAvailable){
        if (isNetworkAvailable)
            layout = R.layout.fragment_profile;
        else
            layout = R.layout.fragment_no_internet_connection;
    }

    public void setupViews() {

    }

    public void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        String title = "Профиль " + user.getFullname();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);

        ImageLoader imageLoader = new ImageLoader(this);
        imageLoader.loadToolbarImage(userImage, user.getAvatar());
        if (!user.getId().equals(Globals.getMe().getId())) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void setupRetryButton(){
        retryBtn.setOnClickListener(view -> presenter.onRetryBtnClick());
    }

    public void reloadFragment(){
        Fragment currentFragment = this;
        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
        fragTransaction.detach(currentFragment);
        fragTransaction.attach(currentFragment);
        fragTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            getActivity().onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.destroy();
    }
}
