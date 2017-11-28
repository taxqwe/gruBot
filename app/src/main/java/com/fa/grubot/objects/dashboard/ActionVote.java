package com.fa.grubot.objects.dashboard;

import com.fa.grubot.objects.misc.VoteOption;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ActionVote extends Action {
    private ArrayList<String> options = new ArrayList<>();

    public ActionVote(String group, DocumentReference author, String desc, Date date, ArrayList<VoteOption> options, Map<String, Boolean> users) {
        super(group, author, desc, date, users);

        for (VoteOption option : options){
            this.options.add(option.getText());
        }
    }

    public ArrayList<String> getOptions() {
        return options;
    }
}
