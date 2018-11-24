package com.nomercy.meetly.Model;

import com.google.gson.annotations.SerializedName;
import com.nomercy.meetly.Model.Groups;

import java.util.ArrayList;

public class GroupList {
    @SerializedName("groups")
    private ArrayList<Groups> groupList;

    public ArrayList<Groups> getGroupArrayList() {
        return groupList;
    }

    public void setGroupArrayList(ArrayList<Groups> groupArrayList) {
        this.groupList = groupArrayList;
    }
}
