package com.arcane.sticks.models;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;



public class Message {
    private String message;
    private String sender;
    private String receiver;
    private long time;
    private String imgURL;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("time", time);
        result.put("imgURL", imgURL);
        result.put("sender", sender);
        result.put("receiver", receiver);
        result.put("message", message);

        return result;
    }


    public String chatPlayerID(){

        return sender.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) ? receiver : sender ;
    }
}
