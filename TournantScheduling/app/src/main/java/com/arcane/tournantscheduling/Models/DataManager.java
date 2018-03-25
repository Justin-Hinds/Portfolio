
package com.arcane.tournantscheduling.Models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static Restaurant validateRestaurant(Restaurant restaurant){

        String zipRegex = "^[0-9]{5}(?:-[0-9]{4})?$";
        Pattern pattern = Pattern.compile(zipRegex);
        Matcher matcher = pattern.matcher(String.valueOf(restaurant.zip));

        if(restaurant.getName() == null){

            return null;
        }
        if(restaurant.getCreated() == null){
            return null;
        }
        if(restaurant.getAddress() == null){
            return null;
        }
        if(!PhoneNumberUtils.isGlobalPhoneNumber(String.valueOf(restaurant.phone))){
            return null;
        }
        if(restaurant.getCity() == null){
            return null;
        }
        if(!matcher.matches()){
            return null;
        }


        return restaurant;
    }

    public static Staff validateManager(Staff manager){

        String zipRegex = "^[0-9]{5}(?:-[0-9]{4})?$";
        Pattern pattern = Pattern.compile(zipRegex);
        Matcher matcher = pattern.matcher(String.valueOf(manager.zip));

        if(manager.getName() == null){
            Log.d("BAD", "NAME");
            return null;
        }
        if(manager.getCreated() == null){
            Log.d("BAD", "DATE");

            return null;
        }
        if(manager.getAddress() == null){
            Log.d("BAD", "ADDRESS");

            return null;
        }
        if(manager.getCity() == null){
            Log.d("BAD", "CITY");

            return null;
        }
        if(!matcher.matches()){
            Log.d("BAD", "ZIP");

            return null;
        }

        return manager;
    }
    public static String passwordValidate(String s, Context context){
        String passwordRegex ="^(?=.*\\d)(?=.*[A-Z])[a-z  A-Z 0-9]{6,}$";
        Pattern passwordPattern = Pattern.compile(passwordRegex);
        Matcher matcher = passwordPattern.matcher(s);
        if (s.length()< 6){
            Toast.makeText(context, "Password must be at least 6 Characters", Toast.LENGTH_SHORT).show();
            return  null;
        }
        if(matcher.matches()){
            return s;
        }else {
            Log.d("PASSWORD ", s);
//            Log.d("MATCH ", matcher.matches())
            Toast.makeText(context, "Password must contain  6 characters, 1 upper case letter and at least 1 number ", Toast.LENGTH_LONG).show();

            return null;
        }
    }

    public void addRestaurant(Restaurant restaurant, final Staff manager, final FirebaseUser user){
        final DocumentReference restaurantRef = db.collection("Restaurants").document();
        final DocumentReference refManager =  restaurantRef.collection("Users").document(user.getUid());

//        WriteBatch batch = db.batch();
//        restaurantRef.set(restaurant);
//        refManager.set(manager);
//        batch.commit();
        Log.d("REF ", restaurantRef.getId());
        addManager(user,manager,restaurant);
//        restaurant.setId(restaurantRef.getId());
//        manager.setRestaurantID(restaurantRef.getId());
//       restaurantRef.set(restaurant).addOnSuccessListener(new OnSuccessListener<Void>() {
//           @Override
//           public void onSuccess(Void aVoid) {
//               Log.d(TAG, "DocumentSnapshot successfully written!");
//
//               refManager.set(manager).addOnSuccessListener(new OnSuccessListener<Void>() {
//                   @Override
//                   public void onSuccess(Void aVoid) {
//                       Log.d(TAG, "DocumentSnapshot successfully written!");
//
//                   }
//               }).addOnFailureListener(new OnFailureListener() {
//                   @Override
//                   public void onFailure(@NonNull Exception e) {
//                       Log.w(TAG, "Error writing document", e);
//                   }
//               });
//           }
//       }).addOnFailureListener(new OnFailureListener() {
//           @Override
//           public void onFailure(@NonNull Exception e) {
//               Log.w(TAG, "Error writing document", e);
//           }
//       });
    }
    //Function for adding new manager to database
    public void addManager(FirebaseUser user, final Staff employee, Restaurant restaurant) {
        final DocumentReference restaurantRef = db.collection("Restaurants").document();
        final DocumentReference refManager =  restaurantRef.collection("Users").document(user.getUid());
        final String id = user.getUid();
        final Staff staff = new Staff();

        restaurant.setId(restaurantRef.getId());
        employee.setRestaurantID(restaurantRef.getId());
        restaurantRef.set(restaurant).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully written!");

                refManager.set(employee).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void addEmployee(FirebaseUser user, final Staff employee, Staff boss) {
        Log.d("BOSS RESTAURANT", boss.getRestaurantID());
        final DocumentReference restaurantRef = db.collection("Restaurants").document(boss.getRestaurantID());
        final DocumentReference refManager =  restaurantRef.collection("Users").document(user.getUid());

        employee.setRestaurantID(boss.getRestaurantID());
        refManager.set(employee).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void updateUser(Staff scheduledEmployee, Staff boss, Day day){
        final DocumentReference restaurantRef = db.collection("Restaurants").document(boss.getRestaurantID());
        final DocumentReference refManager =  restaurantRef.collection("Users").document(scheduledEmployee.getId());

        scheduledEmployee.setRestaurantID(boss.getRestaurantID());
        Map<String,Object> values = new HashMap<>();
        values.put("date",day.getDate());
        values.put("hour",day.getHour());
        values.put("min", day.getMin());
        values.put("outHour",day.getHourOut());
        values.put("minOut",day.getMinOut());
        values.put("month",day.getMonth());
        refManager.collection("Days").document(day.date).set(day);
        refManager.update( "days."+ day.getDate(),values).addOnSuccessListener(new OnSuccessListener<Void>() {
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
//                    addManager(mAuth.getCurrentUser(),imageView, view);
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

    public static boolean loginValidate(String email, String password, Context context){
        if(stringValidate(email) == null){
            return false;
        }
        if(passwordValidate(password,context) == null){
            return false;
        }
        return true;
    }



}
