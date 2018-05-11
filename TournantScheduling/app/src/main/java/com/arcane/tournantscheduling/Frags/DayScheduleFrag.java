package com.arcane.tournantscheduling.Frags;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.arcane.tournantscheduling.Adapter.RosterRecyclerAdapter;
import com.arcane.tournantscheduling.Models.Day;
import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.Utils.DataManager;
import com.arcane.tournantscheduling.ViewModels.RosterViewModel;
import com.arcane.tournantscheduling.ViewModels.ScheduleViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class DayScheduleFrag extends Fragment {
    public static final String TAG = "DAY_SCHEDULE_FRAG";
    public static DayScheduleFrag newInstance() {
        return new DayScheduleFrag();
    }

    RosterRecyclerAdapter mAdapter;
    RosterRecyclerAdapter.OnStaffSelectedListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getListenerFromContext(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_day_schedule, container, false);
        ScheduleViewModel scheduleViewModel = ViewModelProviders.of(getActivity()).get(ScheduleViewModel.class);
        RosterViewModel rosterViewModel = ViewModelProviders.of(getActivity()).get(RosterViewModel.class);
        Day day = scheduleViewModel.getScheduledDay();
        ArrayList<Staff> usersList = new ArrayList<>(rosterViewModel.getUsers().getValue());
      //  Log.d("ROSTER DaySchedule", usersList.size() + "");

        ArrayList<String> workingList = new ArrayList<>();
        if(day != null){
            String timeString = day.getInTime() + " - " + day.getOutTime();
            TextView timeTextview = root.findViewById(R.id.textView_scheduled_time);

            Iterator<Staff> iterator = usersList.iterator();

            while (iterator.hasNext()){
                Staff currentStaff = iterator.next();
                if(currentStaff.getDays() != null){
                    //Log.d("DATES ", currentStaff.getDays().toString());

                    if(currentStaff.getDays().get(DataManager.getDateString(day.getDate())) != null){
                        Map <String, Object> dates = currentStaff.getDays();
                       // Log.d("dates ",day.getDate());
                        workingList.add(currentStaff.getName());
                        iterator.remove();
                    }
                }
            }

            ListView listView = root.findViewById(R.id.working_listview);
            ListAdapter listAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,workingList);
            listView.setAdapter(listAdapter);
//            Log.d("WORKING LIST", workingList.toString());
//            Log.d("OFF LIST", usersList.toString());
//            Log.d("Day Is ", DataManager.getDateString(day.getDate()));
            timeTextview.setText(timeString);
        }else {
            Log.d("Day Is ", "NULL");
        }



        RecyclerView mRecyclerView = root.findViewById(R.id.rec_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter

        mAdapter = new RosterRecyclerAdapter(usersList, getContext(),TAG);
        mAdapter.setOnStaffSelectedListener(mListener);
        mRecyclerView.setAdapter(mAdapter);

        return root;
    }

    private void getListenerFromContext(Context context) {
        if (context instanceof RosterRecyclerAdapter.OnStaffSelectedListener) {
            mListener = (RosterRecyclerAdapter.OnStaffSelectedListener) context;
        } else {
            throw new ClassCastException("Containing activity must " +
                    "implement OnPersonInteractionListener");
        }

    }

}
