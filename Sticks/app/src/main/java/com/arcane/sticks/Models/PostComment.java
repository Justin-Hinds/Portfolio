package com.arcane.sticks.Models;

import java.util.HashMap;

/**
 * Created by ChefZatoichi on 8/17/17.
 */

public class PostComment {

    public PostComment(){

    }
    String postID;
    String text;
    Long time;
    String sender;

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
    public HashMap<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();

        result.put("text",text);
        result.put("time",time);
        result.put("postID",postID);
        result.put("sender",sender);
        return result;
    }
}
