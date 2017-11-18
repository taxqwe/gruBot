package com.fa.grubot.abstractions;

import com.fa.grubot.objects.chat.BranchOfDiscussions;

import java.util.ArrayList;

/**
 * Created by ni.petrov on 18/11/2017.
 */

public interface BranchesFragmentBase {

    void setupToolbar();
    void setupRecyclerView(ArrayList<BranchOfDiscussions> data);
}
