package com.arcane.tournantscheduling.Frags;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_day_schedule, container, false);
        ScheduleViewModel scheduleViewModel = ViewModelProviders.of(getActivity()).get(ScheduleViewModel.class);
        RosterViewModel rosterViewModel = ViewModelProviders.of(getActivity()).get(RosterViewModel.class);
        Day day = scheduleViewModel.getScheduledDay();
        if(day != null){
            String timeString = day.getInTime() + " - " + day.getOutTime();
            TextView timeTextview = root.findViewById(R.id.textView_scheduled_time);
            ArrayList<Staff> usersList = rosterViewModel.getUsers().getValue();
            ArrayList<Staff> workingList = new ArrayList<>();

            Iterator<Staff> iterator = usersList.iterator();
            while (iterator.hasNext()){
                Staff currentStaff = iterator.next();
                if(currentStaff.getDays() != null){
                    if(currentStaff.getDays().get(day.getDate()) != null){
                        Map <String, Object> dates = new HashMap<>();
                        workingList.add(currentStaff);
                        iterator.remove();
                        Log.d("dates ", dates.values().toString());
                    }
                }
            }
            Log.d("WORKING LIST", workingList.toString());
            Log.d("OFF LIST", usersList.toString());


//            for (Staff user : usersList){
//                if(user.getDays() != null){
//                    if(user.getDays().get(day.getDate()) != null){
//                        Map <String, Object> dates = new HashMap<>();
//                       dates = (Map<String, Object>) user.getDays().get(day.getDate());
//                       workingList.add(user);
//                       //usersList.remove(user);
//                        Log.d("dates ", dates.values().toString());
//                    }
//                Log.d("User Days ", user.getDays().toString());
//                }
//            }
            Log.d("Day Is ", DataManager.getDateString(day.getDate()));

            timeTextview.setText(timeString);
        }else {
            Log.d("Day Is ", "NULL");
        }

        return root;
    }

}
