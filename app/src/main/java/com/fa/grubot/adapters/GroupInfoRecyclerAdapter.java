package com.fa.grubot.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fa.grubot.ChatActivity;
import com.fa.grubot.R;
import com.fa.grubot.objects.DashboardEntry;
import com.fa.grubot.objects.GroupInfoButton;
import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fa.grubot.objects.DashboardEntry.TYPE_ANNOUNCEMENT;
import static com.fa.grubot.objects.DashboardEntry.TYPE_VOTE;

public class GroupInfoRecyclerAdapter extends ExpandableRecyclerAdapter<GroupInfoRecyclerAdapter.GroupInfoRecyclerItem>{
    private static final int TYPE_ENTRY = 1001;

    private Context context;

    public GroupInfoRecyclerAdapter(Context context, ArrayList<GroupInfoRecyclerItem> buttons) {
        super(context);
        this.context = context;

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
        @BindView(R.id.card_view) View cardView;


        private DashboardEntryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void bind(int position) {
            DashboardEntry entry = visibleItems.get(position).entry;

            entryTypeText.setText(entry.getTypeText());
            entryDate.setText(entry.getDate());
            entryAuthor.setText(entry.getAuthor());
            entryDesc.setText(entry.getDesc());
            entryGroup.setText(entry.getGroup().getName());
            cardView.setBackgroundColor(getColorFromDashboardEntry(entry));

            cardView.setOnClickListener(v -> {
                if (entry.getType() == DashboardEntry.TYPE_ANNOUNCEMENT)
                    new MaterialDialog.Builder(context)
                            .title(entry.getGroup().getName() + ": " + entry.getDesc())
                            .content(entry.getText())
                            .positiveText(android.R.string.ok)
                            .show();
            });
        }
    }

    public static class GroupInfoRecyclerItem extends ExpandableRecyclerAdapter.ListItem {
        private GroupInfoButton button;
        private DashboardEntry entry;

        public GroupInfoRecyclerItem(GroupInfoButton button) {
            super(TYPE_HEADER);

            this.button = button;
        }

        public GroupInfoRecyclerItem(DashboardEntry entry) {
            super(TYPE_ENTRY);

            this.entry = entry;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderViewHolder(inflate(R.layout.item_group_info_button, parent));
            default:
                return new DashboardEntryViewHolder(inflate(R.layout.item_dashboard_entry, parent));
        }
    }

    @Override
    public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                ((HeaderViewHolder) holder).bind(position);
                break;
            case TYPE_ENTRY:
            default:
                ((DashboardEntryViewHolder) holder).bind(position);
                break;
        }
    }

    private int getColorFromDashboardEntry(DashboardEntry entry){
        Map<Integer, Integer> colorsPairList = new HashMap<>();

        colorsPairList.put(TYPE_ANNOUNCEMENT, ResourcesCompat.getColor(context.getResources(), R.color.colorAnnouncement, null));
        colorsPairList.put(TYPE_VOTE, ResourcesCompat.getColor(context.getResources(), R.color.colorVote, null));

        return colorsPairList.get(entry.getType());
    }
}