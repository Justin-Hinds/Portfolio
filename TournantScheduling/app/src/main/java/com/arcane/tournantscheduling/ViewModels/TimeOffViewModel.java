package com.arcane.tournantscheduling.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.icu.text.SimpleDateFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



public class TimeOffViewModel extends ViewModel {
    String startDate;
    String endDate;
    MutableLiveData<String> liveStartDate;
    MutableLiveData<String> liveEndDate;

   public LiveData<String> getStartTimeOff(){
        if(liveStartDate == null){
            liveStartDate = new MutableLiveData<>();
        }
        return liveStartDate;
    }

    public LiveData<String> getEndTimeOff(){
        if(liveEndDate == null){
            liveEndDate = new MutableLiveData<>();
        }
        return liveEndDate;
    }
    private static ArrayList<Date> getTimeOffRequest(String dateString1, String dateString2)
    {
        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 =DateFormat.getDateInstance();//new java.text.SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1 .parse(dateString1);
            date2 = df1 .parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while(!cal1.after(cal2))
        {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }


    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
        liveStartDate.setValue(startDate);
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
        liveEndDate.setValue(endDate);
    }

    public MutableLiveData<String> getLiveStartDate() {
        return liveStartDate;
    }

    public void setLiveStartDate(MutableLiveData<String> liveStartDate) {
        this.liveStartDate = liveStartDate;
    }

    public MutableLiveData<String> getLiveEndDate() {
        return liveEndDate;
    }

    public void setLiveEndDate(MutableLiveData<String> liveEndDate) {
        this.liveEndDate = liveEndDate;
    }
}
