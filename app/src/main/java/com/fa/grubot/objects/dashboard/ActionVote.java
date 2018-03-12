package com.fa.grubot.objects.dashboard;

import com.fa.grubot.objects.misc.VoteOption;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ActionVote extends Action {
    private ArrayList<String> options = new ArrayList<>();

    public ActionVote(String id, String group, String groupName, String author, String authorName, String desc, Date date, ArrayList<VoteOption> options, Map<String, String> users) {
        super(id, group, groupName, author, authorName, desc, date, users);

        for (VoteOption option : options){
            this.options.add(option.getText());
        }
    }

    public ArrayList<String> getOptions() {
        return options;
    }
}
