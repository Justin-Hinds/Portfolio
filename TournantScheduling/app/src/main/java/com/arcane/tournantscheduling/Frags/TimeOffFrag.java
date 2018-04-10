package com.arcane.tournantscheduling.Frags;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.arcane.tournantscheduling.Models.TimeOff;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.Utils.DataManager;
import com.arcane.tournantscheduling.ViewModels.RosterViewModel;
import com.arcane.tournantscheduling.ViewModels.TimeOffViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimeOffFrag extends Fragment {

    public static final String TAG = "TIME_OFF_FRAG";
    EditText editTextStartDate;
    EditText editTextEndDate;
    EditText editTextReason;
    Button requestButton;
      public static TimeOffFrag newInstance() {
        return new TimeOffFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_time_off,container,false);
        RosterViewModel rosterViewModel = ViewModelProviders.of(getActivity()).get(RosterViewModel.class);
        TimeOffViewModel timeOffViewModel = ViewModelProviders.of(getActivity()).get(TimeOffViewModel.class);
        timeOffViewModel.setCurrentUser(rosterViewModel.getCurrentUser());
        editTextStartDate = root.findViewById(R.id.editText_start_date);
        editTextEndDate = root.findViewById(R.id.editText_end_date);
        editTextReason = root.findViewById(R.id.editText_reason);
        requestButton = root.findViewById(R.id.button_request);
        timeOffViewModel.getStartTimeOff().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                editTextStartDate.setText(s);
            }
        });
        timeOffViewModel.getEndTimeOff().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                editTextEndDate.setText(s);
            }
        });
        editTextStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                DatePickerFrag datePickerFrag = new DatePickerFrag();
                datePickerFrag.show(getActivity().getSupportFragmentManager(),"DATE Begin FRAG");
                }
            }
        });
        editTextEndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    DatePickerFrag datePickerFrag = new DatePickerFrag();
                    datePickerFrag.show(getActivity().getSupportFragmentManager(),"DATE End FRAG");
                }
            }
        });
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start = timeOffViewModel.getStartDate();
                String end = timeOffViewModel.getEndDate();
                String reason = DataManager.stringValidate(editTextReason.getText().toString());
                if(DataManager.stringValidate(reason) != null) {
                    TimeOff timeOff = new TimeOff();
                    timeOffViewModel.getTimeOffRequest(start, end, reason);
                    HomeScreenFrag frag = HomeScreenFrag.newInstance();
                    getActivity().getSupportFragmentManager().popBackStack(HomeScreenFrag.TAG,0);
                    DataManager.hideKeyboard(getActivity());
                }
            }
        });


        return root;
    }



}
