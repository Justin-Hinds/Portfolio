package com.arcane.tournantscheduling.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.arcane.tournantscheduling.Models.Day;
import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.Models.TimeOff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;



public class ScheduleViewModel extends ViewModel {
    private  Staff currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<ArrayList<Day>> liveSchedule;
    private ArrayList<Day> dayArrayList;
    private Day scheduledDay;
    private String weekDay;
    String postSectionDay;

    public ScheduleViewModel(){
        Log.d(" SCHEDULE VIEW MODEL", "CONSTRUCTOR");

    }

    public LiveData<ArrayList<Day>> getSchedule(Staff user){
        if (liveSchedule == null) {
            liveSchedule = new MutableLiveData<>();
            currentUser = user;
            if(user != null){
                getUserSchedule(user);
            }else {
                Log.d("LIVEDATA Get User", "NULL");
            }
            return liveSchedule;
        }

        return liveSchedule;
    }

    public ArrayList<Day> getUserSchedule(Staff staff){
        currentUser = staff;
        ArrayList<Day> days = new ArrayList<>();
        db.collection("Restaurants").document(currentUser.getRestaurantID())
                .collection("Users").document(currentUser.getId()).collection("Days")
                .addSnapshotListener((values, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    Log.d("SCHEDULED DAY", "HIT");

                    for (DocumentSnapshot doc : values ) {
                        if (doc.get("hour") != null) {
                            Day newDay = doc.toObject(Day.class);
                            days.add(doc.toObject(Day.class));
                            Log.d("SCHEDULED DAY", doc.getData().toString());
                            dayArrayList = days;
                           // liveSchedule.setValue(days);
                        }
                        liveSchedule.setValue(days);
                    }
                });

        return days;
    }

    public Day getselecteddDay(String dateString) {
        final Day[] pickedDay = new Day[1];
        db.collection("Restaurants").document(currentUser.getRestaurantID())
                .collection("Users").document(currentUser.getId()).collection("Days")
                .document(dateString).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                pickedDay[0] = documentSnapshot.toObject(Day.class);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("GET SCHEDULED DAY","FAILED");
            }
        });

        return pickedDay[0];
    }
    public ArrayList<Staff> timeOffCheck(Staff user){
        ArrayList<Staff> availableStaff = new ArrayList<>();

            Log.d("TimeOff", user.getTimeOff().toString());

//        db.collection("Restaurants")
//                .document(currentUser.getRestaurantID()).collection("Users")
//                .document(currentUser.getId()).collection("TimeOff").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        for(DocumentSnapshot doc : task.getResult()){
//
//                        Log.d("TIME OFF", doc.toObject(TimeOff.class).getDates().get(0));
//                        }
//                    }
//                });
        return availableStaff;
    }


    public void setScheduledDay(Day scheduledDay) {
        this.scheduledDay = scheduledDay;
    }

    public Day getScheduledDay() {
        return scheduledDay;
    }

    public void setCurrentUser(Staff currentUser) {
        this.currentUser = currentUser;
    }

    public ArrayList<Day> getDayArrayList() {
        return dayArrayList;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
       // Log.d("WEEKDAY","SET");
        this.weekDay = weekDay;
    }

    public String getPostSectionDay() {
        return postSectionDay;
    }

    public void setPostSectionDay(String postSectionDay) {
        this.postSectionDay = postSectionDay;
    }
}
