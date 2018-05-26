package com.arcane.tournantscheduling.Models;

import java.util.ArrayList;



public class GroupChat {
    String id;
    ArrayList<String> userIds;

    public GroupChat(){

    }

    public ArrayList<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(ArrayList<String> userIds) {
        this.userIds = userIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
