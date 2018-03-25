package com.arcane.tournantscheduling.Models;


public class Day {
    String date;
    String hour;
    String min;
    String hourOut;
    String minOut;
    String month;

    public Day(String sDate, String sHour, String sMin, String sMonth, String sMinOut, String sHourOut){
        date = sDate;
        hour = sHour;
        min = sMin;
        hourOut = sHourOut;
        minOut = sMinOut;
        month = sMonth;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMin() {
        return min;
    }

    public String getHourOut() {
        return hourOut;
    }

    public void setHourOut(String hourOut) {
        this.hourOut = hourOut;
    }

    public String getMinOut() {
        return minOut;
    }

    public void setMinOut(String minOut) {
        this.minOut = minOut;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
