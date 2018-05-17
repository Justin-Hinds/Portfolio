
package com.arcane.tournantscheduling.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arcane.tournantscheduling.Models.Availability;
import com.arcane.tournantscheduling.Models.Day;
import com.arcane.tournantscheduling.Models.Restaurant;
import com.arcane.tournantscheduling.Models.Staff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;


public class DataManager {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public DataManager() {
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
            Toast.makeText(context, "Password must contain  6 characters, 1 upper case letter and at least 1 number ", Toast.LENGTH_LONG).show();

            return null;
        }
    }

    public void addRestaurant(Restaurant restaurant, final Staff manager, final FirebaseUser user){
        final DocumentReference restaurantRef = db.collection("Restaurants").document();
        final DocumentReference refManager =  restaurantRef.collection("Users").document(user.getUid());


        Log.d("REF ", restaurantRef.getId());
        addManager(user,manager,restaurant);

    }
    //Function for adding new manager to database
    public void addManager(FirebaseUser user, final Staff employee, Restaurant restaurant) {
        final DocumentReference restaurantRef = db.collection("Restaurants").document();
        final DocumentReference refManager =  restaurantRef.collection("Users").document(user.getUid());
        final String id = user.getUid();
        final Staff staff = new Staff();

        Availability availability = new Availability();
        availability.setMonday(0);
        availability.setTuesday(0);
        availability.setWednesday(0);
        availability.setThursday(0);
        availability.setFriday(0);
        availability.setSaturday(0);
        availability.setSunday(0);

        employee.setAvailability(availability);
        restaurant.setId(restaurantRef.getId());
        employee.setRestaurantID(restaurantRef.getId());
        restaurantRef.set(restaurant).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully written!");

                refManager.set(employee).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        Availability availability = new Availability();
        availability.setMonday(0);
        availability.setTuesday(0);
        availability.setWednesday(0);
        availability.setThursday(0);
        availability.setFriday(0);
        availability.setSaturday(0);
        availability.setSunday(0);

        employee.setRestaurantID(boss.getRestaurantID());
        refManager.set(employee).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateUserAvailability(employee,availability);
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void updateUserAvailability(Staff currentUser, Availability availability){
        Map<String,Object> availableMap = new HashMap<>();
        availableMap.put("monday",availability.getMonday());
        availableMap.put("tuesday",availability.getTuesday());
        availableMap.put("wednesday",availability.getWednesday());
        availableMap.put("thursday",availability.getThursday());
        availableMap.put("friday",availability.getFriday());
        availableMap.put("saturday",availability.getSaturday());
        availableMap.put("sunday",availability.getSunday());

        db.collection("Restaurants").document(currentUser.getRestaurantID())
                .collection("Users").document(currentUser.getId()).update("availability",availableMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void updateUserDay(Staff scheduledEmployee, Staff boss, Day day, Context context){
        final DocumentReference restaurantRef = db.collection("Restaurants").document(boss.getRestaurantID());
        final DocumentReference refManager =  restaurantRef.collection("Users").document(scheduledEmployee.getId());

        scheduledEmployee.setRestaurantID(boss.getRestaurantID());
        Map<String,Object> restVals = new HashMap<>();
        Map<String,Object> values = new HashMap<>();
        values.put("date",getDateString(day.getDate()));
        values.put("inTime",day.getInTime());
        values.put("outTime",day.getOutTime());
//        values.put("hour",day.getHour());
//        values.put("min", day.getMin());
//        values.put("outHour",day.getHourOut());
//        values.put("minOut",day.getMinOut());
        values.put("month",day.getMonth());
        refManager.collection("Days").document(day.date).set(day);
        refManager.update( "days."+getDateString(day.getDate()),values).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
                Toast.makeText(context,scheduledEmployee.getName() + " Schedule Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error writing document", e);
            }
        });
        restVals.put(scheduledEmployee.getId(),true);
        restaurantRef.collection("Days").document(getDateString(day.date)).collection(getDateString(day.getDate())).document(scheduledEmployee.getId());
        restaurantRef.update( "days."+ getDateString( day.getDate()),restVals).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public static boolean loginValidate(String email, String password, Context context){
        if(stringValidate(email) == null){
            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setTitle("Empty Field")
                    .setMessage("Please make sure email is filled in")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
            return false;
        }
        if(passwordValidate(password,context) == null){
            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setTitle("Invalid password")
                    .setMessage("Please make sure your password ha upper and lower case letters and at least one numerical value.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
            return false;
        }
        return true;
    }
    public static String getDateString(String date){
        DateFormat df1 = new java.text.SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        Date date1 = null;
        try {
            date1 = df1.parse(date);
        return df1.format(date1);


        }catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean hourlyAvailable(int hour, Staff user, String weekDay){
        int cutOff = 17;
        switch (weekDay){
            case "monday":
                if(user.getAvailability().getMonday() == 2 && hour > cutOff){
                    return false;
                }
                if(user.getAvailability().getMonday() == 3 && hour < cutOff){
                    return false;
                }
                break;
            case "Tuesday":
                if(user.getAvailability().getTuesday() == 2 && hour > cutOff){
                    return false;
                }
                if(user.getAvailability().getTuesday() == 3 && hour < cutOff){
                    return false;
                }
                break;
            case "Wednesday":
                if(user.getAvailability().getWednesday() == 2 && hour > cutOff){
                    return false;
                }
                if(user.getAvailability().getWednesday() == 3 && hour < cutOff){
                    return false;
                }
                break;
            case "Thursday":
                if(user.getAvailability().getThursday() == 2 && hour > cutOff){
                    return false;
                }
                if(user.getAvailability().getThursday() == 3 && hour < cutOff){
                    return false;
                }
                break;
            case "Friday":
                if(user.getAvailability().getFriday() == 2 && hour > cutOff){
                    return false;
                }
                if(user.getAvailability().getFriday() == 3 && hour < cutOff){
                    return false;
                }
                break;
            case "Saturday":
                if(user.getAvailability().getSaturday() == 2 && hour > cutOff){
                    return false;
                }
                if(user.getAvailability().getSaturday() == 3 && hour < cutOff){
                    return false;
                }
                break;
            case "Sunday":
                if(user.getAvailability().getSunday() == 2 && hour > cutOff){
                    return false;
                }
                if(user.getAvailability().getSunday() == 3 && hour < cutOff){
                    return false;
                }
                break;
        }
        return true;
    }



    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
