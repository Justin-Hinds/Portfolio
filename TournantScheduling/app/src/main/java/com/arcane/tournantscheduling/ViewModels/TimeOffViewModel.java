package com.arcane.tournantscheduling.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.icu.text.SimpleDateFormat;
import android.support.annotation.NonNull;
import android.util.Log;

import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.Models.TimeOff;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class TimeOffViewModel extends ViewModel {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String startDate;
    String endDate;
    Staff currentUser;

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

    void sendTimeOffRequest(TimeOff timeOff){
        Map<String,Object> daysOff = new HashMap<>();

//        for(String date : timeOff.getDates()){
//            daysOff.put(date,true);
//        }
        daysOff.put("dates", timeOff.getDates());
        daysOff.put("reason", timeOff.getReason());
        Log.d("CURRENT USER", currentUser.getName());

        Map<String,Object> offDaysList = new HashMap<>();
        offDaysList.put(timeOff.getDates().get(0),daysOff);
        db.collection("Restaurants").document(currentUser.getRestaurantID())
                .collection("Users").document(currentUser.getId()).collection("TimeOff")
                .document(timeOff.getDates().get(0)).set(timeOff)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Days Off", timeOff.getDates().get(0));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FAIL", e.getMessage());
            }
        });
        db.collection("Restaurants").document(currentUser.getRestaurantID())
                .collection("Users").document(currentUser.getId()).update("timeOff." +timeOff.getDates().get(0),daysOff);

    }
    public  ArrayList<String> getTimeOffRequest(String dateString1, String dateString2, String reason) {

        ArrayList<Date> dates = new ArrayList<>();
        ArrayList<String> dateStrings = new ArrayList<>();
        DateFormat df1 = new java.text.SimpleDateFormat("MM-dd-yyyy",Locale.getDefault());

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1 .parse(dateString1);
            Log.d("Date 1",date1.toString());
            date2 = df1 .parse(dateString2);
            Log.d("Date 2",date2.toString());

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
            dateStrings.add(df1.format(cal1.getTime()));
            cal1.add(Calendar.DATE, 1);
        }

        for(Date date:dates){
            Log.d("DATE", df1.format(date));
        }
        if(currentUser != null){
            TimeOff timeOff = new TimeOff();
            timeOff.setDates(dateStrings);
            timeOff.setReason(reason);
            sendTimeOffRequest(timeOff);
        }else {
            Log.d("CURRENT USER", "NULL");
        }
        return dateStrings;
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

    public void setCurrentUser(Staff currentUser) {
        this.currentUser = currentUser;
    }
}
