package com.arcane.tournantscheduling;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;

import com.arcane.tournantscheduling.Models.Staff;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeScreenActivity extends AppCompatActivity {
    public static final String  EMPLOYEE_TAG = "EMPLOYEE_TAG";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //TODO: Analytics
    private FirebaseAnalytics mFirebaseAnalytics;
    private  FirebaseUser mFireUser;
    private Staff manager;
    private DrawerLayout mDrawerLayout;
    private HomeScreenFrag homeFrag;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser == null){
//            LoginFrag loginFrag = LoginFrag.newInstance();
//            getSupportFragmentManager().beginTransaction().replace(R.id.home_view,loginFrag).commit();
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
       mFireUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        //db.collection("Restaurants")
        homeFrag = HomeScreenFrag.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.home_view,homeFrag).commit();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        Fragment fragment;

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
                                Log.d("MENU ITEM: ", menuItem.toString());
                                fragment = RosterFrag.newInstance();
                                getSupportFragmentManager().beginTransaction().replace(R.id.home_view,fragment).commit();
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





}
