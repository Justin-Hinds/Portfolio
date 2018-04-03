package com.arcane.tournantscheduling.Models;


import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class TimeOff {

    ArrayList<String> dates;
    String reason;





    public TimeOff(){

    }

    public ArrayList<String> getDates() {
        return dates;
    }

    public void setDates(ArrayList<String> dates) {
        this.dates = dates;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
