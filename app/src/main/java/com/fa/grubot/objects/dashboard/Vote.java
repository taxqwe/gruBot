package com.fa.grubot.objects.dashboard;

import com.fa.grubot.objects.group.Group;
import com.fa.grubot.objects.misc.VoteOption;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Vote extends DashboardEntry {
    private ArrayList<String> options = new ArrayList<>();

    public Vote(int id, Group group, String author, String desc, Date date, ArrayList<VoteOption> options) {
        super(id, group, author, desc,date);

        for (VoteOption option : options){
            this.options.add(option.getText());
        }
    }

    public ArrayList<String> getOptions() {
        return options;
    }
}
