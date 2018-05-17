package com.arcane.tournantscheduling.Models;

import java.util.ArrayList;


public class CompanyMessage {


    private String message;
    private String sender;
    private String senderName;
    private long time;
    private ArrayList<String> companyList;

   public  CompanyMessage(){

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

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ArrayList<String> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(ArrayList<String> companyList) {
        this.companyList = companyList;
    }
}
