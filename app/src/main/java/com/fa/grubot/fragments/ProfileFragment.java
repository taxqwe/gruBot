package com.fa.grubot.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.fa.grubot.R;
import com.fa.grubot.abstractions.ProfileFragmentBase;
import com.fa.grubot.adapters.ProfileRecyclerAdapter;
import com.fa.grubot.objects.group.User;
import com.fa.grubot.objects.misc.ProfileItem;
import com.fa.grubot.presenters.ProfilePresenter;
import com.fa.grubot.util.Globals;
import com.fa.grubot.util.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.Nullable;

public class ProfileFragment extends Fragment implements ProfileFragmentBase, Serializable {

    @Nullable @BindView(R.id.toolbar) transient Toolbar toolbar;
    @Nullable @BindView(R.id.retryBtn) transient Button retryBtn;
    @Nullable @BindView(R.id.userImage) transient ImageView userImage;
    @Nullable @BindView(R.id.recycler) transient RecyclerView itemsView;

    @Nullable @BindView(R.id.progressBar) transient ProgressBar progressBar;
    @Nullable @BindView(R.id.content) transient View content;
    @Nullable @BindView(R.id.noInternet) transient View noInternet;

    private transient Unbinder unbinder;
    private transient ProfilePresenter presenter;

    private User user;

    private int state;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new ProfilePresenter(this);
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        user = (User) this.getArguments().getSerializable("user");
        setHasOptionsMenu(true);
        presenter.notifyFragmentStarted(getActivity(), user);

        unbinder = ButterKnife.bind(this, v);
        presenter.notifyViewCreated(state);

        return v;
    }

    public void setupViews() {
        progressBar.setVisibility(View.GONE);

        if (state == Globals.FragmentState.STATE_CONTENT)
            content.setVisibility(View.VISIBLE);
        else
            noInternet.setVisibility(View.VISIBLE);
    }

    public void setupLayouts(boolean isNetworkAvailable){
        if (isNetworkAvailable)
            state = Globals.FragmentState.STATE_CONTENT;
        else
            state = Globals.FragmentState.STATE_NO_INTERNET_CONNECTION;
    }

    public void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        String title = user.getFullname();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);

        ImageLoader imageLoader = new ImageLoader(this);
        if (user.getAvatar() != null) {
            imageLoader.loadToolbarImage(userImage, user.getAvatar());
        } else {
            imageLoader.loadToolbarImage(userImage, imageLoader.getUriOfDrawable(R.drawable.material_bg));
        }

        if (!user.getId().equals(Globals.getMe().getId())) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void setupRecyclerView(ArrayList<ProfileItem> items) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        itemsView.setLayoutManager(mLayoutManager);
        itemsView.setHasFixedSize(false);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration (
                this.getActivity(),
                mLayoutManager.getOrientation()
        );
        itemsView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_from_right));
        itemsView.addItemDecoration(dividerItemDecoration);

        ProfileRecyclerAdapter groupsAdapter = new ProfileRecyclerAdapter(getActivity(), items, user);
        itemsView.setAdapter(groupsAdapter);
        groupsAdapter.notifyDataSetChanged();
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
