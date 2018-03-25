package com.arcane.tournantscheduling.Models;


import android.os.Parcel;
import android.os.Parcelable;

public class Day implements Parcelable{
    String date;
    String hour;
    String min;
    String hourOut;
    String minOut;
    String month;
public Day(){

}
    public Day(String sDate, String sHour, String sMin, String sMonth, String sMinOut, String sHourOut){
        date = sDate;
        hour = sHour;
        min = sMin;
        hourOut = sHourOut;
        minOut = sMinOut;
        month = sMonth;
    }

    protected Day(Parcel in) {
        date = in.readString();
        hour = in.readString();
        min = in.readString();
        hourOut = in.readString();
        minOut = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(hour);
        parcel.writeString(min);
        parcel.writeString(hourOut);
        parcel.writeString(minOut);
        parcel.writeString(month);
    }
}
