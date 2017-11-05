package com.fa.grubot.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Vote extends DashboardEntry {
    private Map<Integer, String> votePairList = new HashMap<>();

    public Vote(int id, Group group, String author, String desc, Date date, ArrayList<String> options) {
        super(id, group, author, desc,date);

        for (int i = 0; i < options.size(); i++) {
            votePairList.put(i, options.get(i));
        }
    }
}
