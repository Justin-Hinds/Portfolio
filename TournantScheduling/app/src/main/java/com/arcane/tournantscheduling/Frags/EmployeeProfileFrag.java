package com.arcane.tournantscheduling.Frags;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arcane.tournantscheduling.Activities.HomeScreenActivity;
import com.arcane.tournantscheduling.Adapter.ScheduleRecyclerAdapter;
import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.ViewModels.RosterViewModel;
import com.arcane.tournantscheduling.ViewModels.ScheduleViewModel;

import java.util.ArrayList;


public class EmployeeProfileFrag extends Fragment {

    public static final String TAG = "EMPLOYEE_PROFILE_FRAG";
    ArrayList mDataset;
    ScheduleRecyclerAdapter mAdapter;
    Staff currentUser;
    TextView textView;
    TextView tAddress;
    TextView tPhone;
    TextView tName;
    TextView tEmail;
    ActionBar actionBar;
    ScheduleRecyclerAdapter.OnDaySelectedListener mListener;
    public static EmployeeProfileFrag newInstance(){return new EmployeeProfileFrag();}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getListenerFromContext(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_employee_profile, container, false);
        Log.d(" Employee Frag onCreate","CALLED");
        mDataset = new ArrayList();
        RosterViewModel rosterViewModel = ViewModelProviders.of(getActivity()).get(RosterViewModel.class);
        currentUser = rosterViewModel.getSelectedUser();
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(currentUser.getName());
        textView = root.findViewById(R.id.no_schedule);
        tEmail = root.findViewById(R.id.textview_email);
        tPhone = root.findViewById(R.id.textview_phone);
        tAddress = root.findViewById(R.id.textview_address);
        tName = root.findViewById(R.id.Textview_name);
        //tName.setText(currentUser.getName());
        tPhone.setText(String.valueOf(currentUser.getPhone()));
        String addressString = currentUser.getAddress() + "\n"  + currentUser.getCity()
                + ", " + currentUser.getState() + " " + currentUser.getZip();
        tAddress.setText(addressString);
        if(currentUser.getEmail() != null){
            tEmail.setText(currentUser.getEmail());
        }
        RecyclerView mRecyclerView =  root.findViewById(R.id.rec_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter
        mAdapter = new ScheduleRecyclerAdapter(mDataset,getActivity());
        mAdapter.setOnDaySelectedListener(mListener);

        mRecyclerView.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(" Employee Frag onStrart","CALLED");
        ScheduleViewModel scheduleViewModel = ViewModelProviders.of(getActivity()).get(ScheduleViewModel.class);
        scheduleViewModel.getUserSchedule(currentUser);
        scheduleViewModel.getSchedule(currentUser).observe(getActivity(), days -> {
            mDataset = days;
           // Log.d(days.get(0).getDayOfWeek(), currentUser.getName());
            mAdapter.update(days);
            if(mDataset.isEmpty()){
                textView.setVisibility(View.VISIBLE);
            }else {
                textView.setVisibility(View.GONE);
            }
        });
        ((HomeScreenActivity) getActivity()).inflateToolbar(R.menu.menu_employee_info);

    }

    private void getListenerFromContext(Context context){
        if (context instanceof ScheduleRecyclerAdapter.OnDaySelectedListener) {
            mListener = (ScheduleRecyclerAdapter.OnDaySelectedListener) context;
        } else {
            throw new ClassCastException("Containing activity must " +
                    "implement OnDaySelectedListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        actionBar.setTitle(getActivity().getApplicationInfo().loadLabel(getActivity().getPackageManager()));
        ((HomeScreenActivity) getActivity()).clearToolbar();

    }
}
