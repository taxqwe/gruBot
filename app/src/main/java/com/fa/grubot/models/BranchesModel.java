package com.fa.grubot.models;

import com.fa.grubot.App;
import com.fa.grubot.objects.chat.BranchOfDiscussions;

import java.util.ArrayList;

/**
 * Created by ni.petrov on 18/11/2017.
 */

public class BranchesModel {


    public BranchesModel() {
    }

    public ArrayList<BranchOfDiscussions> loadBranches(){
        return App.INSTANCE.getDataHelper().getBranches();
    }
}
