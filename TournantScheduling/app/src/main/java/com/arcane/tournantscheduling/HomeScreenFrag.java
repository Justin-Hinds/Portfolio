package com.arcane.tournantscheduling;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.BatchUpdateException;
import java.util.ArrayList;


public class HomeScreenFrag  extends Fragment {
    public static final String TAG = "HOMESCREEN_FRAG";
    ArrayList mDataset;
    ScheduleRecyclerAdapter mAdapter;
    public static HomeScreenFrag newInstance(){return new HomeScreenFrag();}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home_screen,container,false);
        mDataset = new ArrayList();
        Bundle bundle = getArguments();
        if(bundle != null){
            mDataset = (ArrayList) bundle.getSerializable(HomeScreenActivity.DAYS_SCHEDULE);
        }
        RecyclerView mRecyclerView =  root.findViewById(R.id.schedule_rec_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter
        mAdapter = new ScheduleRecyclerAdapter(mDataset,getContext());
        //mAdapter.setOnListener(mListener);
        mRecyclerView.setAdapter(mAdapter);


        return root;
    }
}
