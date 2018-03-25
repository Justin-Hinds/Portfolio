package com.arcane.tournantscheduling;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;

import com.arcane.tournantscheduling.Models.DataManager;
import com.arcane.tournantscheduling.Models.Day;
import com.arcane.tournantscheduling.Models.Staff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class HomeScreenActivity extends AppCompatActivity implements SectionRecyclerAdapter.OnSectionSelectedListener,
        RosterRecyclerAdapter.OnStaffSelectedListener, RosterScheduleRecAdapter.OnScheduleSelectedListener, TimePickerDialog.OnTimeSetListener{
    public static final String  EMPLOYEE_TAG = "EMPLOYEE_TAG";
    public static final String SECTION = "SECTION";
    public static final String ACTION_MODE = "ACTION_MODE";
    public static final String SCHEDULE_MODE = "SCHEDULE_MODE";
    public static final String ARRAYLIST_SCHEDULE = "ARRAYLIST_SCHEDULE";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //TODO: Analytics
    private FirebaseAnalytics mFirebaseAnalytics;
    private  FirebaseUser mFireUser;
    private Staff manager;
    private Staff scheduledUser;
    private DrawerLayout mDrawerLayout;
    private HomeScreenFrag homeFrag;
    Fragment fragment;
    Bundle bundle;
    Toolbar toolbar;
    DataManager dataManager;
    int scheduledHour;
    int scheduledMinute;
    int outHour;
    int outMin;
    String newDate;
    Boolean inTime = true;
    ArrayList<Staff> staffArrayList = new ArrayList<>();
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        getUser();
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser == null){
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
       mFireUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataManager = new DataManager(this);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        homeFrag = HomeScreenFrag.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.home_view,homeFrag, HomeScreenFrag.TAG).commit();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        toolbar.getMenu().clear();
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()){
                            case R.id.availability:
                                Log.d("MENU ITEM: ", menuItem.toString());
                                fragment = AvailabilityFrag.newInstance();
                                getSupportFragmentManager().beginTransaction().replace(R.id.home_view,fragment).commit();
                                break;
                            case R.id.request_time_off:
                                Log.d("MENU ITEM: ", menuItem.toString());
                                fragment = TimeOffFrag.newInstance();
                                getSupportFragmentManager().beginTransaction().replace(R.id.home_view,fragment).commit();
                                break;
                            case R.id.settings:
                                Log.d("MENU ITEM: ", menuItem.toString());
                                fragment = SettingsFragment.newInstance();
                                getSupportFragmentManager().beginTransaction().replace(R.id.home_view,fragment).commit();
                                break;
                            case R.id.create_schedule:
                                Log.d("MENU ITEM: ", menuItem.toString());
                                fragment = CreateScheduleFrag.newInstance();
                                getSupportFragmentManager().beginTransaction().replace(R.id.home_view,fragment).commit();
                                break;
                            case R.id.messages:
                                Log.d("MENU ITEM: ", menuItem.toString());
                                fragment = MessagesFrag.newInstance();
                                getSupportFragmentManager().beginTransaction().replace(R.id.home_view,fragment).commit();
                                break;
                            case R.id.home:
                                Log.d("MENU ITEM: ", menuItem.toString());
                                fragment = HomeScreenFrag.newInstance();
                                getSupportFragmentManager().beginTransaction().replace(R.id.home_view,fragment).commit();
                                break;
                            case R.id.create_manage_staff:
                                Log.d("UserName ", manager.getName());
                                fragment = RosterFrag.newInstance();
                                fragment.setArguments(bundle);
                                getSupportFragmentManager().beginTransaction().replace(R.id.home_view,fragment).commit();
                                Log.d("BUNDLE:","SENT");

                                break;
                            case R.id.logout:
                                Log.d("MENU ITEM: ", menuItem.toString());
                                mAuth.getInstance().signOut();
                                updateUI(mAuth.getCurrentUser());
                                break;
                        }
                        return true;
                    }
                });


        //Log.d("Name:", mAuth.getCurrentUser().getUid());
    }


    private void getUser(){

        db.collection("Restaurants").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        db.collection("Restaurants").document(document.getId()).collection("Users")
                                .whereEqualTo("id",mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                if (task2.isSuccessful()) {
                                    for( DocumentSnapshot document2 : task2.getResult()){
                                        Log.d(TAG, document2.getId() + " => " + document2.getData());
                                        manager = document2.toObject(Staff.class);
                                        bundle = new Bundle();
                                        //bundle.putParcelable(EMPLOYEE_TAG,manager);
                                        getStaffList();
                                        Log.d("BUNDLE MADE", "");
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task2.getException());
                                }
                            }
                        });
                        //Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.schedule_done){
//            fragment = RosterFrag.newInstance();
//            bundle.putSerializable(ARRAYLIST_SCHEDULE,staffArrayList);
//            fragment.setArguments(bundle);
            if(RosterFrag.inSchedulingMode){

            }else {
                //getSupportFragmentManager().beginTransaction().replace(R.id.home_view,fragment).commit();
            }
            //RosterFrag.inSchedulingMode = !RosterFrag.inSchedulingMode;

            Log.d("BUNDLE:","NewToolBar");
        }
        if(item.getItemId() == R.id.done){
            Bundle scheduleBundle = new Bundle();
            scheduleBundle.putBoolean(SCHEDULE_MODE,true);
            scheduleBundle.putSerializable(ARRAYLIST_SCHEDULE,staffArrayList);
            Log.d("STAFF MEMBERS", staffArrayList.size() + "");
            //scheduleBundle.putBoolean(ACTION_MODE, false);
            RosterFrag frag = RosterFrag.newInstance();
            frag.setArguments(scheduleBundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.home_view,frag).commit();
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_schedule);
        }
        return true;
    }

    @Override
    public void onSectionSelected(String section, String date) {
        toolbar.getMenu().clear();
        newDate = date;
        Bundle sectionBundle = new Bundle();
        sectionBundle.putString(SECTION,section);
        sectionBundle.putParcelable(EMPLOYEE_TAG,manager);
        sectionBundle.putBoolean(ACTION_MODE, true);
        RosterFrag frag = RosterFrag.newInstance();
        frag.setArguments(sectionBundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.home_view,frag).commit();
        //toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_roster_schedule_action_mode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onScheduledStaffSelected(Staff staff) {
        scheduledUser = staff;
        android.support.v4.app.DialogFragment timePicker = new TimePickerFrag();
        timePicker.show(getSupportFragmentManager(),"Time Picker");
    }

    @Override
    public void onOutTimeSelected(Staff staff) {
        scheduledUser = staff;
        android.support.v4.app.DialogFragment timePicker = new TimePickerFrag();
        timePicker.show(getSupportFragmentManager(),"Time Picker");
    }

    @Override
    public void OnStaffSelected(Staff staff) {

    }

    @Override
    public void OnStaffChecked( int position, ArrayList<Staff> staffMembers) {
        staffArrayList = staffMembers;
    }

    private void getStaffList(){
        db.collection("Restaurants").document(manager.getRestaurantID()).collection("Users")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document : task.getResult()){
                        Log.d("NAME", document.get("name").toString());
                        Staff staff = document.toObject(Staff.class);
                        if(staff.getDays() != null){
                        Log.d("DAYS",staff.getDays().toString());

                        }

                        staffArrayList.add(staff);

                    }
                }
                bundle.putSerializable(ARRAYLIST_SCHEDULE,staffArrayList);
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        if(inTime) {
            scheduledHour = i1;
            scheduledMinute = i;
            inTime = false;
        }else{
            outHour = i1;
            outMin = i;
            setScheduledDay();
            inTime = true;
        }

    }
    private void setScheduledDay(){
        Calendar calendar = Calendar.getInstance();
        java.text.SimpleDateFormat month_date = new java.text.SimpleDateFormat("MMMM", Locale.getDefault());
        java.text.SimpleDateFormat new_date = new java.text.SimpleDateFormat("MM-DD-YYYY",Locale.getDefault());
        //String date = new_date.format(calendar.getTime());
        Log.d("DATE",newDate);
        String month_name = month_date.format(calendar.getTime());
        int month = calendar.get(Calendar.MONTH);
        Day newDay = new Day(newDate,String.valueOf(scheduledHour),
                String.valueOf(scheduledMinute),month_name, String.valueOf(outHour), String.valueOf(outMin));

        Map<String,Day> dayHashMap = new HashMap<>();
        dayHashMap.put(newDate, newDay);
        //scheduledUser.setDays(dayHashMap);
        dataManager.updateUser(scheduledUser,manager, newDay);
    }
}
