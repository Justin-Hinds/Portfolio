package com.arcane.tournantscheduling.Frags;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcane.tournantscheduling.Adapter.RosterRecyclerAdapter;
import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.ViewModels.RosterViewModel;

import java.util.ArrayList;


public class ScheduleRosterFrag extends Fragment {
    private RosterRecyclerAdapter rosterRecyclerAdapter;
    private ArrayList<Staff> myDataset;
    private RosterViewModel viewModel;
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
        RecyclerView mRecyclerView = root.findViewById(R.id.roster_rec_view);
        rosterRecyclerAdapter = new RosterRecyclerAdapter(myDataset,getContext(),TAG);
        viewModel.getUsers().observe(getActivity(), new Observer<ArrayList<Staff>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Staff> staff) {
                rosterRecyclerAdapter.update(staff);
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
}
