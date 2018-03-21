
package com.arcane.tournantscheduling.Models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import static android.content.ContentValues.TAG;


public class DataManager {
    private Context mContext = null;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public DataManager(Context context) {
        mContext = context;
    }

    public static String stringValidate(String s) {
        if (s.length() < 1) {
            return null;
        }
        if (s.trim().length() == 0) {
            return null;
        }

        return s;
    }
    public static Staff validateManager(Staff manager){

        if(manager.getName() == null){
            return null;
        }
        if(manager.getCreated() == null){
            return null;
        }
        if(manager.getAddress() == null){
            return null;
        }
        if(manager.getId() == null){
            return null;
        }
        if(manager.getCity() == null){
            return null;
        }
        if(manager.getZip() < 10000){
            return null;
        }


        return manager;
    }
    public void addRestaurant(Restaurant restaurant, final Staff manager, final FirebaseUser user){
        final DocumentReference restaurantRef = db.collection("Restaurants").document();
        final DocumentReference refManager =  restaurantRef.collection("Users").document(user.getUid());

//        WriteBatch batch = db.batch();
//        restaurantRef.set(restaurant);
//        refManager.set(manager);
//        batch.commit();
       restaurantRef.set(restaurant).addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void aVoid) {
               Log.d(TAG, "DocumentSnapshot successfully written!");

               refManager.set(manager).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                       Log.d(TAG, "DocumentSnapshot successfully written!");

                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Log.w(TAG, "Error writing document", e);
                   }
               });
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Log.w(TAG, "Error writing document", e);
           }
       });

    }
    //Function for adding new user to database
    private void addUser(FirebaseUser user ) {
        final String id = user.getUid();
        final Staff staff = new Staff();


    }
    //Checks user for login/creation submission
    public Staff userCheck(final ImageView imageView, final View view) {
        final Staff[] currentDishUser = new Staff[0];


//        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    Staff staff = child.getValue(Staff.class);
//                    Log.d("SNAP: ", child.getValue().toString());
//                    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                    if (user.equals(staff != null ? staff.getId() : null)) {
//                        currentDishUser[0] = staff;
//                        if (currentDishUser[0].getId().equals(user)) {
//                            mContext.startActivity(new Intent(mContext, MainActivity.class));
//                            return;
//                        }
//                    }
//                }
//                if(imageView != null || view != null){
//                    addUser(mAuth.getCurrentUser(),imageView, view);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        return currentDishUser[0];
    }





}
