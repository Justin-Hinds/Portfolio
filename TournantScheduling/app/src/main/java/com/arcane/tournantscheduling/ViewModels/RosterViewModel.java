package com.arcane.tournantscheduling.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.arcane.tournantscheduling.Models.Staff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;



public class RosterViewModel extends ViewModel {
    private FirebaseAuth mAuth;
    private Staff currentUser;
    private Staff chatBuddy;
    private Staff selectedUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<ArrayList<Staff>> users;


    public RosterViewModel(){
        getUser();
    }
    public LiveData<ArrayList<Staff>> getUsers() {
        mAuth = FirebaseAuth.getInstance();
        if (users == null) {
            users = new MutableLiveData<>();
        }
        return users;
    }

    private void loadUsers() {
        final ArrayList<Staff> staffArrayList = new ArrayList<>();
        db.collection("Restaurants").document(currentUser.getRestaurantID()).collection("Users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
                        if(querySnapshot != null){
                            for(DocumentSnapshot document : querySnapshot){
                                Log.d("NAME", document.get("name").toString());
                                Staff staff = document.toObject(Staff.class);
                                if(staff.getDays() != null){
                                    //Log.d("DAYS",staff.getDays().toString());
                                }
                                if(!staffArrayList.contains(staff)){
                                staffArrayList.add(staff);
                                users.postValue(staffArrayList);
                                Log.d("LOAD USERS", " => " + staffArrayList.size());

                                }
                            }
                        }
                    }
                });
//                .get().addOnCompleteListener(task -> {
//                    if(task.isSuccessful()){
//                        for(DocumentSnapshot document : task.getResult()){
//                            Log.d("NAME", document.get("name").toString());
//                            Staff staff = document.toObject(Staff.class);
//                            if(staff.getDays() != null){
//                                //Log.d("DAYS",staff.getDays().toString());
//                            }
//                            staffArrayList.add(staff);
//                            users.postValue(staffArrayList);
//                        }
//                    }
                   // bundle.putSerializable(ARRAYLIST_SCHEDULE,staffArrayList);
//                });
    }

    public Staff getUser(){
        final Staff[] staff = new Staff[1];
        db.collection("Restaurants").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        db.collection("Restaurants").document(document.getId()).collection("Users")
                                .whereEqualTo("id",mAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
                                if (querySnapshot != null) {
                                    for( DocumentSnapshot document2 : querySnapshot){
//                                        Log.d("CURRENT USER", document2.getId() + " => " + document2.getData());
                                        currentUser = document2.toObject(Staff.class);
                                        staff[0] = document2.toObject(Staff.class);

                                        loadUsers();

                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ");
                                }
                            }

                        });
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }

        });
        return staff[0];
    }

    public void updateUserProfile(Staff user){
        DocumentReference reference = db.collection("Restaurants").document(user.getRestaurantID())
                .collection("Users").document(user.getId());

        reference.set(user, SetOptions.merge());

    }
    public void deleteUser(Staff user){
        DocumentReference reference = db.collection("Restaurants").document(user.getRestaurantID())
                .collection("Users").document(user.getId());
        reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("User"," DELETED");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("User"," DELETION FAILED");

            }
        });
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public void setCurrentUser(Staff currentUser) {
        this.currentUser = currentUser;
    }
    public Staff getCurrentUser(){return currentUser;}

    public FirebaseFirestore getDb() {
        return db;
    }

    public void setDb(FirebaseFirestore db) {
        this.db = db;
    }

    public Staff getChatBuddy() {
        return chatBuddy;
    }

    public void setChatBuddy(Staff chatBuddy) {
        this.chatBuddy = chatBuddy;
    }

    public Staff getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(Staff selectedUser) {
        this.selectedUser = selectedUser;
    }
}
