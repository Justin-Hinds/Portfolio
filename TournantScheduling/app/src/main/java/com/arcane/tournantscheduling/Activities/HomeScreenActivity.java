package com.arcane.tournantscheduling.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.arcane.tournantscheduling.Frags.AvailabilityFrag;
import com.arcane.tournantscheduling.Frags.CreateScheduleFrag;
import com.arcane.tournantscheduling.Frags.DayScheduleFrag;
import com.arcane.tournantscheduling.Frags.HomeScreenFrag;
import com.arcane.tournantscheduling.Frags.MessageViewFrag;
import com.arcane.tournantscheduling.Frags.MessagesFrag;
import com.arcane.tournantscheduling.Models.TimeOff;
import com.arcane.tournantscheduling.Utils.DataManager;
import com.arcane.tournantscheduling.Models.Day;
import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.Frags.RosterFrag;
import com.arcane.tournantscheduling.Adapter.RosterRecyclerAdapter;
import com.arcane.tournantscheduling.Adapter.RosterScheduleRecAdapter;
import com.arcane.tournantscheduling.Utils.NetworkUtils;
import com.arcane.tournantscheduling.ViewModels.RosterViewModel;
import com.arcane.tournantscheduling.Adapter.ScheduleRecyclerAdapter;
import com.arcane.tournantscheduling.Frags.ScheduleRosterFrag;
import com.arcane.tournantscheduling.ViewModels.ScheduleViewModel;
import com.arcane.tournantscheduling.Frags.SectionFrag;
import com.arcane.tournantscheduling.Adapter.SectionRecyclerAdapter;
import com.arcane.tournantscheduling.Frags.SettingsFragment;
import com.arcane.tournantscheduling.Frags.TimeOffFrag;
import com.arcane.tournantscheduling.Frags.TimePickerFrag;
import com.arcane.tournantscheduling.ViewModels.TimeOffViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class HomeScreenActivity extends AppCompatActivity implements SectionRecyclerAdapter.OnSectionSelectedListener,
        RosterRecyclerAdapter.OnStaffSelectedListener, RosterScheduleRecAdapter.OnScheduleSelectedListener,
        TimePickerDialog.OnTimeSetListener, FragmentManager.OnBackStackChangedListener,
        ScheduleRecyclerAdapter.OnDaySelectedListener,DatePickerDialog.OnDateSetListener {

    public static final String  EMPLOYEE_TAG = "EMPLOYEE_TAG";
    public static final String SECTION = "SECTION";
    public static final String ACTION_MODE = "ACTION_MODE";
    public static final String SCHEDULE_MODE = "SCHEDULE_MODE";
    public static final String ARRAYLIST_SCHEDULE = "ARRAYLIST_SCHEDULE";
    public static final String DAYS_SCHEDULE = "DAYS_SCHEDULE";
    RosterViewModel model;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //TODO: Analytics
    private FirebaseAnalytics mFirebaseAnalytics;
    private  FirebaseUser mFireUser;
    private Staff currentUser;
    private Staff scheduledUser;
    private ArrayList<Day> dayArrayList;
    private DrawerLayout mDrawerLayout;
    private HomeScreenFrag homeFrag;
    boolean navDrawerIsSet = false;
    FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment fragment;
    Bundle bundle;
    Toolbar toolbar;
    DataManager dataManager;
    ScheduleViewModel scheduleViewModel;
    TimeOffViewModel timeOffViewModel;
    ProgressBar progressBar;
    int scheduledHour;
    int scheduledMinute;
    int outHour;
    int outMin;
    String newDate;
    String startDate;
    String endDate;
    String inScheduledTime;
    String outScheduledTime;
    long dateNumber;
    Boolean inTime = true;
    ArrayList<Staff> staffArrayList = new ArrayList<>();
    Boolean mTablet;
    ViewGroup tabletViewGroup;
    TextView inTextview;
    TextView outTextview;



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
         mFireUser = mAuth.getCurrentUser();
        updateUI(mFireUser);
    }

    private void updateUI(FirebaseUser firebaseUser) {
        if (firebaseUser == null){
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
        progressBar = findViewById(R.id.progressBar);
        dataManager = new DataManager(this);
        fragmentManager.addOnBackStackChangedListener(this);
        dayArrayList = new ArrayList<>();
        homeFrag = HomeScreenFrag.newInstance();

        if(!NetworkUtils.isConnected(this)){
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("No Network")
                    .setMessage("Please make sure you're connected to the internet.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
            return;
        }

        model = ViewModelProviders.of(this).get(RosterViewModel.class);
        timeOffViewModel = ViewModelProviders.of(this).get(TimeOffViewModel.class);
        scheduleViewModel = ViewModelProviders.of(this).get(ScheduleViewModel.class);
        mDrawerLayout = findViewById(R.id.drawer_layout);


        tabletViewGroup = findViewById(R.id.detail_view);
        mTablet = (tabletViewGroup != null);
        if(mTablet){
//            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        }
        model.getUsers().observe(this, users -> {
            for(Staff user : users){
                if(Objects.equals(user.getId(), mFireUser.getUid())){
                    currentUser = user;
                    if(!navDrawerIsSet){
                    setUpNavDrawer(user);
                    }
                    navDrawerIsSet = true;
                    timeOffViewModel.setCurrentUser(user);
                    setDeviceToken();
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.home_view,homeFrag, HomeScreenFrag.TAG)
                            .addToBackStack(HomeScreenFrag.TAG).commit();
                    progressBar.setVisibility(View.GONE);
                   // Log.d("VIEW MODEL current user", user.getName() );
                }
            }

        });
        mAuth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.schedule_done){
            fragmentManager.popBackStack(SectionFrag.TAG,0);
        }
        if(item.getItemId() == R.id.done){
            Bundle scheduleBundle = new Bundle();
            scheduleBundle.putBoolean(SCHEDULE_MODE,true);
            scheduleBundle.putSerializable(ARRAYLIST_SCHEDULE,staffArrayList);
            Log.d("STAFF MEMBERS", staffArrayList.size() + "");
            //scheduleBundle.putBoolean(ACTION_MODE, false);
            RosterFrag frag = RosterFrag.newInstance();
            frag.setArguments(scheduleBundle);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.home_view,frag)
                    .addToBackStack(RosterFrag.TAG).commit();
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_schedule);
        }
        return true;
    }

    @Override
    public void onSectionSelected(String section, String date, long newDateNumber) {
        toolbar.getMenu().clear();
        newDate = date;
        scheduleViewModel.setPostSectionDay(date);
        RosterFrag.isInActionMode=true;
        ScheduleRosterFrag frag = ScheduleRosterFrag.newInstance();
        fragmentManager
                .beginTransaction()
                .replace(R.id.home_view,frag)
                .addToBackStack(ScheduleRosterFrag.TAG).commit();

        toolbar.inflateMenu(R.menu.menu_roster_schedule_action_mode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onScheduledStaffSelected(Staff staff, View view) {
        scheduledUser = staff;
         inTextview = view.findViewById(R.id.in);
        android.support.v4.app.DialogFragment timePicker = new TimePickerFrag();
        timePicker.show(getSupportFragmentManager(),"Time Picker");
    }

    @Override
    public void onOutTimeSelected(Staff staff, View view) {
        scheduledUser = staff;
         outTextview = view.findViewById(R.id.out);
        android.support.v4.app.DialogFragment timePicker = new TimePickerFrag();
        timePicker.show(getSupportFragmentManager(),"Time Picker");
    }

    @Override
    public void OnStaffSelected(Staff staff) {

    }

    @Override
    public void OnNewChatSelected(Staff staff) {
        model.setChatBuddy(staff);
        Log.d(currentUser.getName() + " -> ", model.getChatBuddy().getName());
        MessageViewFrag frag = MessageViewFrag.newInstance();
        fragmentManager.beginTransaction()
                .add(frag,MessageViewFrag.TAG)
                .replace(R.id.home_view,frag)
                .addToBackStack(MessageViewFrag.TAG).commit();

    }

    @Override
    public void OnStaffChecked( int position, ArrayList<Staff> staffMembers) {
        staffArrayList = staffMembers;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        String newTimeString;
        Calendar calendar = Calendar.getInstance();
        java.text.SimpleDateFormat frmTime = new java.text.SimpleDateFormat("hh:mm",Locale.getDefault());
        Log.d("NAME TIME",timePicker.getId() + "");
        if(inTime) {
            scheduledHour = timePicker.getCurrentHour();
            scheduledMinute = timePicker.getCurrentMinute();
            calendar.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
            newTimeString = frmTime.format(calendar.getTime());
            Log.d("TIME", newTimeString);

            double hour =  scheduledHour;
            double minute = scheduledMinute;
            String timeString = String.valueOf(scheduledHour) + " : " + String.valueOf(scheduledMinute);
            inScheduledTime = newTimeString;
            if(inTextview != null){
            inTextview.setText(newTimeString);
            inTime = false;
            }else {

            }
        }else{
            outHour = timePicker.getCurrentHour();
            outMin = timePicker.getCurrentMinute();
            String timeString = String.valueOf(outHour) + " : " + String.valueOf(outMin);
            calendar.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
            newTimeString = frmTime.format(calendar.getTime());
            outScheduledTime = newTimeString;
            if(outTextview != null){
            outTextview.setText(newTimeString);
            setScheduledDay();
            }
            inTime = true;
        }
    }

    private void setScheduledDay(){
        Calendar calendar = Calendar.getInstance();
        java.text.SimpleDateFormat month_date = new java.text.SimpleDateFormat("MMMM", Locale.getDefault());
        java.text.SimpleDateFormat weekDay = new java.text.SimpleDateFormat("EE",Locale.getDefault());

        String month_name = month_date.format(calendar.getTime());
//        Day newDay = new Day(newDate,String.valueOf(scheduledHour),
//                String.valueOf(scheduledMinute),month_name,
//                String.valueOf(outHour),
//                String.valueOf(outMin),
//                scheduleViewModel.getWeekDay());

        Day newDay = new Day(inScheduledTime,outScheduledTime,newDate,month_name,scheduleViewModel.getWeekDay());
        Map<String,Day> dayHashMap = new HashMap<>();
        dayHashMap.put(newDate, newDay);
        dataManager.updateUserDay(scheduledUser, currentUser, newDay);
    }
    private void setUpNavDrawer(Staff staff){

        NavigationView navigationView;
        navigationView = findViewById(R.id.nav_view);
        if(staff.isManager()){
            navigationView.inflateMenu(R.menu.nav_drawer);
        }else{
            navigationView.inflateMenu(R.menu.employee_nav_drawer);
        }

        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    toolbar.getMenu().clear();
                    // set item as selected to persist highlight
                    menuItem.setChecked(true);
                    // close drawer when item is tapped
                    mDrawerLayout.closeDrawers();

                    switch (menuItem.getItemId()){
                        case R.id.availability:
                            Log.d("MENU ITEM: ", menuItem.toString());
                            fragment = AvailabilityFrag.newInstance();
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.home_view,fragment)
                                    .addToBackStack(AvailabilityFrag.TAG).commit();
                            break;
                        case R.id.request_time_off:
                            Log.d("MENU ITEM: ", menuItem.toString());
                            fragment = TimeOffFrag.newInstance();
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.home_view,fragment)
                                    .addToBackStack(TimeOffFrag.TAG).commit();
                            break;
                        case R.id.settings:
                            Log.d("MENU ITEM: ", menuItem.toString());
                            fragment = SettingsFragment.newInstance();
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.home_view,fragment)
                                    .addToBackStack(SettingsFragment.TAG).commit();
                            break;
                        case R.id.create_schedule:
                            Log.d("MENU ITEM: ", menuItem.toString());
                            fragment = CreateScheduleFrag.newInstance();
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.home_view,fragment)
                                    .addToBackStack(CreateScheduleFrag.TAG).commit();
                            break;
                        case R.id.messages:
                            Log.d("MENU ITEM: ", menuItem.toString());
                            fragment = MessagesFrag.newInstance();
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.home_view,fragment)
                                    .addToBackStack(MessagesFrag.TAG).commit();
                            break;
                        case R.id.home:
                            Log.d("MENU ITEM: ", menuItem.toString());
//                                Bundle scheduleBundle = new Bundle();
//                                scheduleBundle.putSerializable(DAYS_SCHEDULE,dayArrayList);
                            fragment = fragmentManager.findFragmentByTag(HomeScreenFrag.TAG);
//                                fragment.setArguments(scheduleBundle);
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.home_view,fragment)
                                    .addToBackStack(HomeScreenFrag.TAG).commit();
                            break;
                        case R.id.create_manage_staff:
                            Log.d("UserName ", currentUser.getName());
                            fragment = RosterFrag.newInstance();
                            fragment.setArguments(bundle);
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.home_view,fragment)
                                    .addToBackStack(RosterFrag.TAG).commit();
                            Log.d("BUNDLE:","SENT");

                            break;
                        case R.id.logout:
                            Log.d("MENU ITEM: ", menuItem.toString());
                            mAuth.getInstance().signOut();
                            updateUI(mAuth.getCurrentUser());
                            break;
                    }
                    return true;
                });
    }
    private void setDeviceToken(){
        Map<String, Object> token = new HashMap<>();
        token.put(currentUser.getId(), FirebaseInstanceId.getInstance().getToken());
        db.collection("fcmTokens").document("deviceTokens").set(token, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }
    @Override
    public void onBackStackChanged() {
        for (int i = fragmentManager.getBackStackEntryCount() - 1; i>=0; i--){
        Log.d("Backstack", fragmentManager.getBackStackEntryAt(i).getName());
        }

    }

    @Override
    public void onScheduleSelected(Day day) {
        scheduleViewModel.setScheduledDay(day);
        DayScheduleFrag frag = DayScheduleFrag.newInstance();
        fragmentManager.beginTransaction().replace(R.id.home_view,frag)
                .addToBackStack(DayScheduleFrag.TAG).commit();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(startDate == null){
            startDate = (month+1) + "-" + dayOfMonth + "-" + year;
            timeOffViewModel.setStartDate(startDate);
            Log.d("DATE PICKER START", (month+1) + "-" + dayOfMonth + "-" + year);
        }else{
            endDate = (month+1) + "-" + dayOfMonth + "-" + year;
            timeOffViewModel.setEndDate(endDate);
            startDate = null;
        Log.d("DATE PICKER PICKED END", (month+1) + "-" + dayOfMonth + "-" + year);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // fragmentManager.popBackStack(HomeScreenFrag.TAG,0);
        RosterFrag.isInActionMode = false;
        DataManager.hideKeyboard(this);
    }


}
