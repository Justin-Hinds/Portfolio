package com.arcane.tournantscheduling.Frags;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arcane.tournantscheduling.Adapter.TimeOffRecyclerAdapter;
import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.Models.TimeOff;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.ViewModels.RosterViewModel;
import com.arcane.tournantscheduling.ViewModels.TimeOffViewModel;

import java.util.ArrayList;


public class TimeOffListFrag extends Fragment {
    public static final String TAG = "TIME_OFF_LIST_FRAG";
    TimeOffRecyclerAdapter mAdapter;
    RecyclerView mRecyclerView;
    ArrayList<TimeOff> myTimeOffList = new ArrayList<>();
    ArrayList<TimeOff> staffTimeOffList;
    RosterViewModel rosterViewModel;
    TimeOffViewModel timeOffViewModel;
    Staff currentuser;
    FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;
    public static TimeOffListFrag newInstance() {
        return new TimeOffListFrag();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_time_off_list, container, false);
        rosterViewModel = ViewModelProviders.of(getActivity()).get(RosterViewModel.class);
        currentuser = rosterViewModel.getCurrentUser();
        mAdapter = new TimeOffRecyclerAdapter(myTimeOffList,getContext(), currentuser);

        timeOffViewModel = ViewModelProviders.of(getActivity()).get(TimeOffViewModel.class);
        timeOffViewModel.getCompanyTimeOff(currentuser.getRestaurantID());
        timeOffViewModel.getLiveCompanyTimeOff().observe(getActivity(), new Observer<ArrayList<TimeOff>>() {
            @Override
            public void onChanged(@Nullable ArrayList<TimeOff> timeOffs) {
                staffTimeOffList = new ArrayList<>(timeOffs);
            }
        });
        timeOffViewModel.getUsersTimeOff(currentuser);
        timeOffViewModel.getTimeOff().observe(getActivity(), new Observer<ArrayList<TimeOff>>() {
            @Override
            public void onChanged(@Nullable ArrayList<TimeOff> timeOffs) {
                myTimeOffList = new ArrayList<>(timeOffs);
                mAdapter.update(timeOffs);
            }
        });
        bottomNavigationView = root.findViewById(R.id.bottom_nav_bar);
        fab = root.findViewById(R.id.fab);
            fab.bringToFront();
        if(!currentuser.isManager()){
        bottomNavigationView.setVisibility(View.GONE);
        }else {
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.your_timeoff){
                    mAdapter.update(myTimeOffList);
                }else if(item.getItemId() == R.id.staff_timeoff){
                    mAdapter.update(staffTimeOffList);
                }
                return true;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeOffFrag frag = TimeOffFrag.newInstance();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.home_view,frag)
                        .addToBackStack(TimeOffFrag.TAG).commit();
            }
        });
        mRecyclerView = root.findViewById(R.id.rec_view);
        myTimeOffList = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter
        mRecyclerView.setAdapter(mAdapter);


        return root;
    }

}
