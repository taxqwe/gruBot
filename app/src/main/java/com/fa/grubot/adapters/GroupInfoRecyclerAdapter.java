package com.fa.grubot.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fa.grubot.ChatActivity;
import com.fa.grubot.R;
import com.fa.grubot.objects.dashboard.Announcement;
import com.fa.grubot.objects.dashboard.DashboardEntry;
import com.fa.grubot.objects.group.GroupInfoButton;
import com.fa.grubot.objects.group.User;
import com.fa.grubot.util.Globals;
import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupInfoRecyclerAdapter extends ExpandableRecyclerAdapter<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem>{
    private static final int TYPE_ENTRY = 1001;
    private static final int TYPE_USER = 1002;

    private Context context;
    private ArrayList<GroupInfoRecyclerItem> buttons;

    public GroupInfoRecyclerAdapter(Context context, ArrayList<GroupInfoRecyclerItem> buttons) {
        super(context);
        this.context = context;
        this.buttons = buttons;

        setItems(buttons);
    }

    class HeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder {
        @BindView(R.id.buttonText) TextView buttonText;
        @BindView(R.id.childCountText) TextView childCountText;

        private HeaderViewHolder(View view) {
            super(view, view.findViewById(R.id.item_arrow));
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            super.bind(position);

            GroupInfoButton button = visibleItems.get(position).button;

            buttonText.setText(button.getText());
            if (button.getChildCount() > 0) {
                childCountText.setText(String.valueOf(button.getChildCount()));
                childCountText.setVisibility(View.VISIBLE);
            }

            if (button.getId() == 1) {
                buttonText.getRootView().setOnClickListener(v -> {
                    Intent intent = new Intent(context, ChatActivity.class);
                    context.startActivity(intent);
                });
            }
        }
    }

    class DashboardEntryViewHolder extends GroupInfoRecyclerAdapter.ViewHolder {
        @BindView(R.id.entryTypeText) TextView entryTypeText;
        @BindView(R.id.entryDate) TextView entryDate;
        @BindView(R.id.entryGroup) TextView entryGroup;
        @BindView(R.id.entryAuthor) TextView entryAuthor;
        @BindView(R.id.entryDesc) TextView entryDesc;
        @BindView(R.id.view_foreground) RelativeLayout viewForeground;


        private DashboardEntryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void bind(int position) {
            DashboardEntry entry = visibleItems.get(position).entry;

            entryDate.setText(entry.getDate());
            entryAuthor.setText(entry.getAuthor());
            entryDesc.setText(entry.getDesc());
            entryGroup.setText(entry.getGroup().getName());
            viewForeground.setBackgroundColor(getColorFromDashboardEntry(entry));

            if (entry instanceof Announcement) {
                entryTypeText.setText("Объявление");
                viewForeground.setOnClickListener(v -> {
                    new MaterialDialog.Builder(context)
                            .title(entry.getDesc())
                            .content(((Announcement) entry).getText())
                            .positiveText(android.R.string.ok)
                            .show();
                });
            } else {
                entryTypeText.setText("Голосование");
                viewForeground.setOnClickListener(v -> {
                    new MaterialDialog.Builder(context)
                            .title(entry.getGroup().getName() + ": " + entry.getDesc())
                            .items(new String[] {"1", "2", "3"})
                            .itemsCallbackSingleChoice(-1, (MaterialDialog.ListCallbackSingleChoice) (dialog, view, which, text) -> {
                                /**
                                 * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                                 * returning false here won't allow the newly selected radio button to actually be selected.
                                 **/
                                return true;
                            })
                            .positiveText(android.R.string.ok)
                            .show();
                });
            }
        }
    }

    class UserViewHolder extends GroupInfoRecyclerAdapter.ViewHolder {
        @BindView(R.id.userImage) ImageView userImage;
        @BindView(R.id.userName) TextView userName;
        @BindView(R.id.userPhone) TextView userPhone;

        private UserViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void bind(int position) {
            User user = visibleItems.get(position).user;

            userName.setText(user.getFullname());
            userPhone.setText(user.getPhoneNumber());
            userImage.setImageDrawable(Globals.ImageMethods.getRoundImage(context, user.getFullname()));
        }
    }

    public static class GroupInfoRecyclerItem extends ExpandableRecyclerAdapter.ListItem {
        private GroupInfoButton button;
        private DashboardEntry entry;
        private User user;

        public GroupInfoRecyclerItem(GroupInfoButton button) {
            super(TYPE_HEADER);

            this.button = button;
        }

        public GroupInfoRecyclerItem(DashboardEntry entry) {
            super(TYPE_ENTRY);

            this.entry = entry;
        }

        public GroupInfoRecyclerItem(User user) {
            super(TYPE_USER);

            this.user = user;
        }

        public boolean isHeader() {
            return (button != null);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderViewHolder(inflate(R.layout.item_group_info_button, parent));
            case TYPE_ENTRY:
                return new DashboardEntryViewHolder(inflate(R.layout.item_dashboard_entry, parent));
            case TYPE_USER:
                return new UserViewHolder(inflate(R.layout.item_user, parent));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                ((HeaderViewHolder) holder).bind(position);
                break;
            case TYPE_ENTRY:
                ((DashboardEntryViewHolder) holder).bind(position);
                break;
            case TYPE_USER:
                ((UserViewHolder) holder).bind(position);
                break;
        }
    }

    private int getColorFromDashboardEntry(DashboardEntry entry){
        if (entry instanceof Announcement)
            return context.getResources().getColor(R.color.colorAnnouncement);
        else
            return context.getResources().getColor(R.color.colorVote);
    }

    public void insertItem(DashboardEntry entry) {
        String type;
        if (entry instanceof Announcement)
            type = "Объявления";
        else
            type = "Голосования";

        for (GroupInfoRecyclerItem item : buttons) {
            if (item.isHeader() && item.button.getText().equals(type)) {
                buttons.add(buttons.indexOf(item) + 1, new GroupInfoRecyclerItem(entry));
                item.button.addChild(entry);
                setItems(buttons);
                break;
            }
        }
    }
}