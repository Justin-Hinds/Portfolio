package com.arcane.tournantscheduling.Models;


import android.support.annotation.NonNull;

public class Message implements Comparable<Message>{

    private String message;
    private String sender;
    private String receiver;
    private long time;

    public Message(){

    }

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


    @Override
    public boolean equals(Object newMessage) {
        Message comparingMessage = (Message) newMessage;
        if( comparingMessage.getTime() == time){
            return true;
        }
        return false;
    }


    @Override
    public int compareTo(@NonNull Message nextMessage) {
        long current = this.getTime();
        long next = nextMessage.getTime();

        if(current < next){
            return -1;
        }
        if(current > next){
            return 1;
        }
        return 0;
    }
}
