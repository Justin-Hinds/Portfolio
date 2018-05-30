package com.arcane.tournantscheduling.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
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
import android.widget.Toast;

import com.arcane.tournantscheduling.Frags.AvailabilityFrag;
import com.arcane.tournantscheduling.Frags.CreateScheduleFrag;
import com.arcane.tournantscheduling.Frags.DayScheduleFrag;
import com.arcane.tournantscheduling.Frags.EmployeeProfileEditFrag;
import com.arcane.tournantscheduling.Frags.EmployeeProfileFrag;
import com.arcane.tournantscheduling.Frags.HomeScreenFrag;
import com.arcane.tournantscheduling.Frags.MessageViewFrag;
import com.arcane.tournantscheduling.Frags.MessagesFrag;
import com.arcane.tournantscheduling.Frags.TimeOffListFrag;
import com.arcane.tournantscheduling.Models.GroupChat;
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

import java.text.ParseException;
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
    Intent messageIntent;
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
    private static Boolean mTablet;
    Boolean mlaunched = false;
    ViewGroup tabletViewGroup;
    TextView inTextview;
    TextView outTextview;
    long inMillis;
    long outMillis;
    long minimumTime = 3600000;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(NetworkUtils.isConnected(this)) {
            mFireUser = mAuth.getCurrentUser();
            updateUI(mFireUser);
        }
    }

    private void updateUI(FirebaseUser firebaseUser) {
        if(NetworkUtils.isConnected(this)){
        if (firebaseUser == null){
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
       mFireUser = mAuth.getCurrentUser();
        }else {

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        progressBar = findViewById(R.id.progressBar);
        dataManager = new DataManager();
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
                            startActivity(new Intent(HomeScreenActivity.this,LoginActivity.class));
                            finish();
                            dialog.dismiss();
                        }
                    }).show();
            return;
        }

        messageIntent = getIntent();

        model = ViewModelProviders.of(this).get(RosterViewModel.class);
        timeOffViewModel = ViewModelProviders.of(this).get(TimeOffViewModel.class);
        scheduleViewModel = ViewModelProviders.of(this).get(ScheduleViewModel.class);
        mDrawerLayout = findViewById(R.id.drawer_layout);


        tabletViewGroup = findViewById(R.id.detail_view);
        mTablet = (tabletViewGroup != null);
        if(mTablet){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
                    if(!mlaunched){
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.home_view,homeFrag, HomeScreenFrag.TAG)
                            .addToBackStack(HomeScreenFrag.TAG).commit();
                    }
                    mlaunched = true;
                    progressBar.setVisibility(View.GONE);
                   // Log.d("VIEW MODEL current user", user.getName() );
                }
                if(messageIntent.hasExtra("message")){
                   // Log.d("INTENT", messageIntent.getStringExtra("sender"));
                    String sender = messageIntent.getStringExtra("message");
                    if (user.getId().equals(sender)) {
                        model.setChatBuddy(user);
                    }
                    if(currentUser != null){
                        model.setCurrentUser(currentUser);
                        MessageViewFrag frag = MessageViewFrag.newInstance();
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.home_view,frag, MessageViewFrag.TAG)
                                .addToBackStack(MessageViewFrag.TAG).commit();
                    }
                }
            }
            if(messageIntent.hasExtra("timeOffStart")){
                TimeOffListFrag frag = TimeOffListFrag.newInstance();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.home_view,frag, TimeOffListFrag.TAG)
                        .addToBackStack(TimeOffListFrag.TAG).commit();
            }

            if(messageIntent.hasExtra("companyMessage")){
                String name = messageIntent.getStringExtra("senderName");
                String message = messageIntent.getStringExtra("companyMessage");
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("From: " + name)
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
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
        switch (item.getItemId()){
            case R.id.schedule_done:
                fragmentManager.popBackStack(SectionFrag.TAG,0);
                toolbar.getMenu().clear();
                break;
            case R.id.done:
                Bundle scheduleBundle = new Bundle();
                scheduleBundle.putBoolean(SCHEDULE_MODE,true);
                scheduleBundle.putSerializable(ARRAYLIST_SCHEDULE,staffArrayList);
                RosterFrag frag = RosterFrag.newInstance();
                frag.setArguments(scheduleBundle);
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.home_view,frag)
                        .addToBackStack(RosterFrag.TAG).commit();
                model.setFab(false);
                toolbar.getMenu().clear();
                toolbar.inflateMenu(R.menu.menu_schedule);
                break;
            case R.id.message_group_done:
                ArrayList<String> arrayList = new ArrayList();
                for(Staff staff : staffArrayList){
                    arrayList.add(staff.getId());
                }
                GroupChat groupChat = new GroupChat();
                groupChat.setUserIds(arrayList);
                toolbar.getMenu().clear();
                break;
            case android.R.id.home:
                if(!mTablet){
                mDrawerLayout.openDrawer(GravityCompat.START);
                DataManager.hideKeyboard(this );
                }
                break;
            case R.id.update:
                EmployeeProfileEditFrag editFrag = EmployeeProfileEditFrag.newInstance();
                fragmentManager.beginTransaction()
                        .replace(R.id.home_view,editFrag)
                        .addToBackStack(EmployeeProfileEditFrag.TAG).commit();
                toolbar.getMenu().clear();
                toolbar.inflateMenu(R.menu.menu_employee_edit);
                break;
            case R.id.delete:
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Delete Employee")
                        .setMessage("Are you sure you want to delete this employee?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                 model.deleteUser(model.getSelectedUser());
                                 model.getUsers();
                                 fragmentManager.popBackStack(HomeScreenFrag.TAG,0);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

                break;

        }

        return true;
    }

    @Override
    public void onSectionSelected(String section, String date, long newDateNumber) {
        model.setFab(false);
        toolbar.getMenu().clear();
        newDate = date;
        scheduleViewModel.setPostSectionDay(date);
        scheduleViewModel.setSection(section);
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
        model.setSelectedUser(staff);
        EmployeeProfileFrag frag = EmployeeProfileFrag.newInstance();
        fragmentManager.beginTransaction()
                .replace(R.id.home_view,frag)
                .addToBackStack(EmployeeProfileFrag.TAG).commit();
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_employee_info);
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

    @Override
    public void OnGroupSelected(int position, ArrayList<Staff> groupMembers) {
        staffArrayList = groupMembers;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        String newTimeString;


        Calendar calendar = Calendar.getInstance();
        java.text.SimpleDateFormat frmTime = new java.text.SimpleDateFormat("hh:mm",Locale.getDefault());
        if(inTime) {
            scheduledHour = timePicker.getCurrentHour();
            scheduledMinute = timePicker.getCurrentMinute();
            String postSTR = " AM";
            if(scheduledHour > 11){
                postSTR = " PM";
            }
            calendar.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
             inMillis = calendar.getTimeInMillis();
            newTimeString = frmTime.format(calendar.getTime()) + postSTR;
            Log.d("SCHEDULED HOUR", newTimeString);

            if(DataManager.hourlyAvailable(scheduledHour,scheduledUser,scheduleViewModel.getWeekDay())){
            //String timeString = String.valueOf(scheduledHour) + " : " + String.valueOf(scheduledMinute);
            inScheduledTime = newTimeString;
            if(inTextview != null){
            inTextview.setText(newTimeString);
            inTime = false;
            }
            }else {
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Scheduling Error")
                        .setMessage("This staff member is not available for this time. ")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }

        }else{
            outHour = timePicker.getCurrentHour();
            outMin = timePicker.getCurrentMinute();
//            String timeString = String.valueOf(outHour) + " : " + String.valueOf(outMin);
            calendar.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
            outMillis = calendar.getTimeInMillis();
            long timeDiff = outMillis - inMillis;
//            Log.d("TIMES", timeDiff + ""  );
//                Toast.makeText(this,"Shift is only " + Math.floor(timeDiff / minimumTime) + " hours",Toast.LENGTH_SHORT).show();
            if( (outMillis - inMillis) < minimumTime){
                Toast.makeText(this,"Shift is less than an hour long.",Toast.LENGTH_SHORT).show();
                return;
            }
            String postSTR = " AM";
            if(outHour > 11){
                postSTR = " PM";
            }
            if(DataManager.hourlyAvailable(outHour,scheduledUser,scheduleViewModel.getWeekDay())){
                newTimeString = frmTime.format(calendar.getTime()) + postSTR;
                outScheduledTime = newTimeString;
                if(outTextview != null){
                    outTextview.setText(newTimeString);
                    setScheduledDay();
                }
                inTime = true;
            }else {
                new AlertDialog.Builder(this)
                        .setTitle("Scheduling Error")
                        .setMessage("This staff member is not available for this time. ")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }

        }
    }

    private void setScheduledDay(){
        String dateString = scheduleViewModel.getDateString().replace("-","/");
        Calendar calendar = Calendar.getInstance();
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy",Locale.getDefault());
        java.text.SimpleDateFormat month_date = new java.text.SimpleDateFormat("MMMM", Locale.getDefault());
        java.text.SimpleDateFormat weekDay = new java.text.SimpleDateFormat("EE",Locale.getDefault());
        try {
            calendar.setTime(simpleDateFormat.parse(dateString));
            Log.d("Calendar time", calendar.getTime().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String month_name = month_date.format(calendar.getTime());
        Log.d("CALENDAR POST", calendar.getTime().toString());
        Day newDay = new Day(inScheduledTime,outScheduledTime,newDate,month_name,scheduleViewModel.getWeekDay(),scheduledUser.getId());
        Map<String,Day> dayHashMap = new HashMap<>();
        dayHashMap.put(newDate, newDay);
        dataManager.updateUserDay(scheduledUser, currentUser, newDay,this);
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
                            fragment = AvailabilityFrag.newInstance();
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.home_view,fragment)
                                    .addToBackStack(AvailabilityFrag.TAG).commit();
                            DataManager.hideKeyboard(this);
                            break;
                        case R.id.request_time_off:
                            fragment = TimeOffListFrag.newInstance();
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.home_view,fragment)
                                    .addToBackStack(TimeOffListFrag.TAG).commit();
                            DataManager.hideKeyboard(this);
                            break;
                        case R.id.settings:
                            fragment = SettingsFragment.newInstance();
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.home_view,fragment)
                                    .addToBackStack(SettingsFragment.TAG).commit();
                            DataManager.hideKeyboard(this);
                            break;
                        case R.id.create_schedule:
                            fragment = CreateScheduleFrag.newInstance();
                            model.setFab(false);
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.home_view,fragment)
                                    .addToBackStack(CreateScheduleFrag.TAG).commit();
                            DataManager.hideKeyboard(this);

                            break;
                        case R.id.messages:
                            fragment = MessagesFrag.newInstance();
                            model.setFab(false);
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.home_view,fragment)
                                    .addToBackStack(MessagesFrag.TAG).commit();
                            DataManager.hideKeyboard(this);

                            break;
                        case R.id.home:
                            fragment = fragmentManager.findFragmentByTag(HomeScreenFrag.TAG);
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.home_view,fragment)
                                    .addToBackStack(HomeScreenFrag.TAG).commit();
                            DataManager.hideKeyboard(this);

                            break;
                        case R.id.create_manage_staff:
                            fragment = RosterFrag.newInstance();
                            fragment.setArguments(bundle);
                            model.setFab(true);
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.home_view,fragment)
                                    .addToBackStack(RosterFrag.TAG).commit();
                            DataManager.hideKeyboard(this);
                            break;
                        case R.id.logout:
                            DataManager.hideKeyboard(this);
                            mAuth.getInstance().signOut();
                            updateUI(mAuth.getCurrentUser());
                            break;
                    }
                    return true;
                });

    }

    private void setDeviceToken(){
        currentUser.setDeviceToken(FirebaseInstanceId.getInstance().getToken());
        model.updateUserProfile(currentUser);
        Map<String, Object> token = new HashMap<>();
        token.put(currentUser.getId(), FirebaseInstanceId.getInstance().getToken());
        db.collection("fcmTokens").document("deviceTokens").set(token, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }

    @Override
    public void onBackStackChanged() {
        for (int i = fragmentManager.getBackStackEntryCount() - 1; i >= 0; i--){
//        Log.d("Backstack", fragmentManager.getBackStackEntryAt(i).getName());
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
//            Log.d("DATE PICKER START", (month+1) + "-" + dayOfMonth + "-" + year);
        }else{
            endDate = (month+1) + "-" + dayOfMonth + "-" + year;
            timeOffViewModel.setEndDate(endDate);
            startDate = null;
//        Log.d("DATE PICKER PICKED END", (month+1) + "-" + dayOfMonth + "-" + year);
        }
    }

    @Override
    public void onBackPressed() {
        int count = fragmentManager.getBackStackEntryCount() - 1;
//        Log.d("Back pressed", fragmentManager.getBackStackEntryAt(count).getName());
        if(fragmentManager.getBackStackEntryAt(count).getName().equals(RosterFrag.TAG)){
            fragmentManager.popBackStack(SectionFrag.TAG,0);
            return;
        }
        if(fragmentManager.getBackStackEntryCount() > 1) {
            super.onBackPressed();
            RosterFrag.isInActionMode = false;
            DataManager.hideKeyboard(this);
        }
    }

    public static Boolean getTablet(){
        return mTablet;
    }

}
