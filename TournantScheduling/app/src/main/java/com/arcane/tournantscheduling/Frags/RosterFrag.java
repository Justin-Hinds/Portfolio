package com.arcane.tournantscheduling.Frags;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcane.tournantscheduling.Activities.HomeScreenActivity;
import com.arcane.tournantscheduling.Adapter.RosterRecyclerAdapter;
import com.arcane.tournantscheduling.Adapter.RosterScheduleRecAdapter;
import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.ViewModels.RosterViewModel;

import java.util.ArrayList;


public class RosterFrag extends Fragment {
    public static final String TAG = "ROSTER_FRAG";
    public static Boolean isInActionMode = false;
    public static Boolean inSchedulingMode = false;
    private RosterRecyclerAdapter mAdapter;
    private RosterScheduleRecAdapter scheduleRecAdapter;
    private Staff mUser;
    private RosterViewModel viewModel;
    private ArrayList<Staff> myDataset;
    private RosterRecyclerAdapter.OnStaffSelectedListener mListener;
    private RosterScheduleRecAdapter.OnScheduleSelectedListener scheduleSelectedListener;


    public static RosterFrag newInstance() {
        return new RosterFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_roster, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(RosterViewModel.class);

        RecyclerView mRecyclerView = root.findViewById(R.id.roster_rec_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter

        mAdapter = new RosterRecyclerAdapter(myDataset, getContext(),TAG);
        viewModel.getUsers().observe(getActivity(), new Observer<ArrayList<Staff>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Staff> staff) {
                mAdapter.update(staff);
            }
        });
        scheduleRecAdapter = new RosterScheduleRecAdapter(myDataset,getContext());
        Bundle bundle = getArguments();
        if(bundle != null){
            //Log.d("BUNDLE CONTENTS", bundle.toString());
            if(bundle.getParcelable(HomeScreenActivity.EMPLOYEE_TAG)!=null){
                mUser = bundle.getParcelable(HomeScreenActivity.EMPLOYEE_TAG);

            }
            if(bundle.getSerializable(HomeScreenActivity.ARRAYLIST_SCHEDULE) != null){
                myDataset = (ArrayList<Staff>) bundle.getSerializable(HomeScreenActivity.ARRAYLIST_SCHEDULE);
                mAdapter.update(myDataset);
            }
            if(bundle.getString(HomeScreenActivity.SECTION)!=null){
               // Log.d("SECTION FROM BUNDLE", bundle.getString(HomeScreenActivity.SECTION));
            }

            if(bundle.getBoolean(HomeScreenActivity.ACTION_MODE)){
                isInActionMode = true;
                mAdapter.setOnStaffSelectedListener(mListener);
            }else {
                isInActionMode = false;
            }
            if(bundle.getBoolean(HomeScreenActivity.SCHEDULE_MODE)){
                inSchedulingMode = true;
                scheduleRecAdapter.setOnScheduledSelectedListener(scheduleSelectedListener);
                scheduleRecAdapter.update(myDataset);
                mRecyclerView.setAdapter(scheduleRecAdapter);

            }else {
                mRecyclerView.setAdapter(mAdapter);
            }
        }else {
            Log.d("BUNDLE:","FAILED");
            mRecyclerView.setAdapter(mAdapter);
        }




        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
//            if(isInActionMode){
//            Log.d("FAB ", "HIT IN ACTION MODE");
//            Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
//            }else {
            CreateStaffFrag frag = CreateStaffFrag.newInstance();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_view,frag).commit();
//            }
        });
        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("onAttach:","Hit");
        getListenerFromContext(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.d("onDetach:","Hit");
    }

    private void getListenerFromContext(Context context) {
        if (context instanceof RosterRecyclerAdapter.OnStaffSelectedListener) {
            mListener = (RosterRecyclerAdapter.OnStaffSelectedListener) context;
        } else {
            throw new ClassCastException("Containing activity must " +
                    "implement OnPersonInteractionListener");
        }
        if (context instanceof RosterScheduleRecAdapter.OnScheduleSelectedListener) {
            scheduleSelectedListener = (RosterScheduleRecAdapter.OnScheduleSelectedListener) context;
        } else {
            throw new ClassCastException("Containing activity must " +
                    "implement OnPersonInteractionListener");
        }
    }

}
