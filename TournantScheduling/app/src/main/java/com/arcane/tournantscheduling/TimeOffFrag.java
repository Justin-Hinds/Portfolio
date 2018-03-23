package com.arcane.tournantscheduling;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimeOffFrag extends Fragment {

    public static final String TAG = "TIME_OFF_FRAG";

    public static TimeOffFrag newInstance() {
        return new TimeOffFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_time_off,container,false);




        return root;
    }

}
