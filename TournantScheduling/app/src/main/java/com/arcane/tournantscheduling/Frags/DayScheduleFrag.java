package com.arcane.tournantscheduling.Frags;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcane.tournantscheduling.R;


public class DayScheduleFrag extends Fragment {
    public static final String TAG = "DAY_SCHEDULE_FRAG";

    public static DayScheduleFrag newInstance() {
        return new DayScheduleFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_day_schedule, container, false);

        return root;
    }

}
