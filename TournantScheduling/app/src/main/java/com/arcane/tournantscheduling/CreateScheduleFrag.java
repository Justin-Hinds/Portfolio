package com.arcane.tournantscheduling;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.EventLogTags;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import java.util.Calendar;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateScheduleFrag extends Fragment {
    public static final String TAG = "CREATE_SCHEDULE_FRAG";

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
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date = (i1 + 1) + "/" + i2 + "/" + i;
                Log.d(TAG, date);
                Intent intent = new Intent();
                intent.putExtra("date", date);
                SectionFrag frag = SectionFrag.newInstance();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_view,frag).commit();
            }
        });
        return root;
    }

}
