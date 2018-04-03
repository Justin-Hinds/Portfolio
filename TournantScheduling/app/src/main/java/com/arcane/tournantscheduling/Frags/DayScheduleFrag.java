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
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.ViewModels.ScheduleViewModel;


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

        Day day = scheduleViewModel.getScheduledDay();
        if(day != null){
            String timeString = day.getHour() + " : " + day.getMin() + " - " + day.getHourOut() + " : " + day.getMinOut();
            TextView timeTextview = root.findViewById(R.id.textView_scheduled_time);
            timeTextview.setText(timeString);
        }else {
            Log.d("Day Is ", "NULL");
        }

        return root;
    }

}
