package com.arcane.tournantscheduling.Models;


import android.os.Parcel;
import android.os.Parcelable;

public class Day implements Parcelable{
    public String date;
    String inTime;
    String outTime;
//    String hour;
//    String min;
//    String hourOut;
//    String minOut;
    String month;
    String dayOfWeek;
    String userId;

    public Day(){

    }
    public Day(String in, String out, String sDate, String sMonth, String weekday, String id){
        date = sDate;
        inTime = in;
        outTime = out;
        month = sMonth;
        dayOfWeek = weekday;
        userId = id;
    }
//    public Day(String sDate, String sHour, String sMin, String sMonth, String sHourOut, String sMinOut,String weekday){
//        date = sDate;
//        hour = sHour;
//        min = sMin;
//        hourOut = sHourOut;
//        minOut = sMinOut;
//        month = sMonth;
//        dayOfWeek = weekday;
//    }

    protected Day(Parcel in) {
        date = in.readString();
//        hour = in.readString();
//        min = in.readString();
//        hourOut = in.readString();
//        minOut = in.readString();
        month = in.readString();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

//    public String getHour() {
//        return hour;
//    }
//
//    public void setHour(String hour) {
//        this.hour = hour;
//    }
//
//    public String getMin() {
//        return min;
//    }
//
//    public String getHourOut() {
//        return hourOut;
//    }
//
//    public void setHourOut(String hourOut) {
//        this.hourOut = hourOut;
//    }
//
//    public String getMinOut() {
//        return minOut;
//    }
//
//    public void setMinOut(String minOut) {
//        this.minOut = minOut;
//    }
//
//    public void setMin(String min) {
//        this.min = min;
//    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
//        parcel.writeString(hour);
//        parcel.writeString(min);
//        parcel.writeString(hourOut);
//        parcel.writeString(minOut);
        parcel.writeString(month);
    }
}
