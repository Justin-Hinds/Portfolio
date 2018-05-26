package com.arcane.tournantscheduling.Frags;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.ViewModels.ScheduleViewModel;

import java.util.Calendar;



public class CreateScheduleFrag extends Fragment {
    public static final String TAG = "CREATE_SCHEDULE_FRAG";
    public static final String SCHEDULE_DATE = "SCHEDULE_DATE";
    CalendarView calendarView;
    public static CreateScheduleFrag newInstance() {
        return new CreateScheduleFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_create_schedule, container, false);
        calendarView = root.findViewById(R.id.calendar);
        ScheduleViewModel scheduleViewModel = ViewModelProviders.of(getActivity()).get(ScheduleViewModel.class);
        calendarView.setOnDateChangeListener((calendarView, i, i1, i2) -> {

            long currentTime = System.currentTimeMillis();
            Calendar calendarNow = Calendar.getInstance();
            calendarNow.setTimeInMillis(currentTime);

            String date = (i1 + 1) + "-" + i2 + "-" + i;
            String weekday = "Sunday";
            //Log.d(TAG, date);
            Calendar calendar = Calendar.getInstance();
            calendar.set(i, i1, i2);

            if(currentTime > calendar.getTimeInMillis()){
                Log.d("THIS IS"," THE PAST");
                Toast.makeText(getContext(),"Can not schedule past dates", Toast.LENGTH_SHORT).show();
                return;
            }
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            switch (dayOfWeek){
                case Calendar.SUNDAY:
                    weekday = "Sunday";
                    break;
                case Calendar.MONDAY:
                    weekday = "Monday";
                    break;
                case Calendar.TUESDAY:
                    weekday = "Tuesday";
                    break;
                case Calendar.WEDNESDAY:
                    weekday = "Wednesday";
                    break;
                case Calendar.THURSDAY:
                    weekday = "Thursday";
                    break;
                case Calendar.FRIDAY:
                    weekday = "Friday";
                    break;
                case Calendar.SATURDAY:
                    weekday = "Saturday";
                    break;
            }

            //Log.d("CALENDAR WEEKDAY", weekday + "");
            scheduleViewModel.setWeekDay(weekday);
            scheduleViewModel.setDateString(date);
            Bundle bundle = new Bundle();
            bundle.putString("DATE",weekday);
            bundle.putString(SCHEDULE_DATE, date);
            SectionFrag frag = SectionFrag.newInstance();
            frag.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_view,frag)
                    .addToBackStack(SectionFrag.TAG).commit();
        });
        return root;
    }

}
