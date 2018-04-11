package com.arcane.tournantscheduling.Frags;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcane.tournantscheduling.Adapter.RosterRecyclerAdapter;
import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.ViewModels.RosterViewModel;
import com.arcane.tournantscheduling.ViewModels.ScheduleViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;


public class ScheduleRosterFrag extends Fragment {
    private RosterRecyclerAdapter rosterRecyclerAdapter;
    private ArrayList<Staff> myDataset;
    private RosterViewModel viewModel;
    private ScheduleViewModel scheduleViewModel;
    private String weekDay;
    private RosterRecyclerAdapter.OnStaffSelectedListener mListener;
    public static final String TAG = "SCHEDULE_ROSTER_FRAG";
    public static ScheduleRosterFrag newInstance() {
        return new ScheduleRosterFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_schedule_roster, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(RosterViewModel.class);
        scheduleViewModel = ViewModelProviders.of(getActivity()).get(ScheduleViewModel.class);
        weekDay = scheduleViewModel.getWeekDay();
        RecyclerView mRecyclerView = root.findViewById(R.id.roster_rec_view);
        rosterRecyclerAdapter = new RosterRecyclerAdapter(myDataset,getContext(),TAG);
        viewModel.getUsers().observe(getActivity(), new Observer<ArrayList<Staff>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Staff> staff) {
                ArrayList<Staff> staffArrayList = staff;
                ArrayList<Staff> newList = new ArrayList<>();
                assert staff != null;
                Iterator<Staff> iterator = staffArrayList.iterator();

                while (iterator.hasNext()){

//                }
//                for(Staff user : staff){
                    Staff user = iterator.next();
                    Log.d("USER",user.getName());
                    if(user.getAvailability() != null){
                        Log.d("AVAILABILITY", user.getAvailability().getSaturday() + weekDay);
                    if(isAvailability(user,weekDay)){
                        String day = scheduleViewModel.getPostSectionDay();
                        DateFormat df1 = new java.text.SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
                        Calendar cal1 = Calendar.getInstance();

                        try {
                        Date date1;
                        date1 = df1 .parse(day);
                        cal1.setTime(date1);
                        String newDay = df1.format(cal1.getTime());
                       // Map<String, Object>  timeOff = (Map<String, Object>) user.getTimeOff().get(newDay);
                        Log.d("Date 1",newDay);
                        // Log.d("TIME OFF", timeOff.get("dates").toString());

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(!newList.contains(user)){
                        newList.add(user);
                            }
                        }else{
                        iterator.remove();
                     }
                    }
                }
                rosterRecyclerAdapter.update(newList);
            }
        });
        myDataset = viewModel.getUsers().getValue();
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        rosterRecyclerAdapter.setOnStaffSelectedListener(mListener);
        rosterRecyclerAdapter.update(myDataset);
        mRecyclerView.setAdapter(rosterRecyclerAdapter);
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getListenerFromContext(context);
    }

    private void getListenerFromContext(Context context) {
        if (context instanceof RosterRecyclerAdapter.OnStaffSelectedListener) {
            mListener = (RosterRecyclerAdapter.OnStaffSelectedListener) context;
        } else {
            throw new ClassCastException("Containing activity must " +
                    "implement OnPersonInteractionListener");
        }
    }
    private boolean isAvailability(Staff user, String weekDay){
        switch (weekDay){
            case "monday":
                if(user.getAvailability().getMonday() == 1){
                    return false;
                }
                break;
            case "Tuesday":
                if(user.getAvailability().getTuesday() == 1){
                    return false;
                }
                break;
            case "Wednesday":
                if(user.getAvailability().getWednesday() == 1){
                    return false;
                }
                break;
            case "Thursday":
                if(user.getAvailability().getThursday() == 1){
                    return false;
                }
                break;
            case "Friday":
                if(user.getAvailability().getFriday() == 1){
                    return false;
                }
                break;
            case "Saturday":
                if(user.getAvailability().getSaturday() == 1){
                    return false;
                }
                break;
            case "Sunday":
                if(user.getAvailability().getSunday() == 1){
                    return false;
                }
                break;
        }
        return true;
    }
}
