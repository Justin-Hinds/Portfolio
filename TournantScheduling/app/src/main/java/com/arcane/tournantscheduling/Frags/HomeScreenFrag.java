package com.arcane.tournantscheduling.Frags;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arcane.tournantscheduling.Adapter.ScheduleRecyclerAdapter;
import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.ViewModels.RosterViewModel;
import com.arcane.tournantscheduling.ViewModels.ScheduleViewModel;

import java.util.ArrayList;


public class HomeScreenFrag  extends Fragment {
    public static final String TAG = "HOMESCREEN_FRAG";
    ArrayList mDataset;
    ScheduleRecyclerAdapter mAdapter;
    Staff currentUser;
    TextView textView;
    ScheduleRecyclerAdapter.OnDaySelectedListener mListener;
    public static HomeScreenFrag newInstance(){return new HomeScreenFrag();}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getListenerFromContext(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        ScheduleViewModel scheduleViewModel = ViewModelProviders.of(getActivity()).get(ScheduleViewModel.class);
        scheduleViewModel.getUserSchedule(currentUser);
        scheduleViewModel.getSchedule(currentUser).observe(getActivity(), days -> {
            mDataset = days;
            mAdapter.update(days);
            if(mDataset.isEmpty()){
                textView.setVisibility(View.VISIBLE);
            }else {
                textView.setVisibility(View.GONE);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("onCreate","CALLED");

        View root = inflater.inflate(R.layout.fragment_home_screen,container,false);
        mDataset = new ArrayList();
        RosterViewModel rosterViewModel = ViewModelProviders.of(getActivity()).get(RosterViewModel.class);
        currentUser = rosterViewModel.getCurrentUser();
        textView = root.findViewById(R.id.no_schedule);

        RecyclerView mRecyclerView =  root.findViewById(R.id.schedule_rec_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter
        mAdapter = new ScheduleRecyclerAdapter(mDataset,getContext());
        mAdapter.setOnDaySelectedListener(mListener);

        mRecyclerView.setAdapter(mAdapter);


        return root;
    }

    private void getListenerFromContext(Context context){
        if (context instanceof ScheduleRecyclerAdapter.OnDaySelectedListener) {
            mListener = (ScheduleRecyclerAdapter.OnDaySelectedListener) context;
        } else {
            throw new ClassCastException("Containing activity must " +
                    "implement OnPersonInteractionListener");
        }
    }
}
