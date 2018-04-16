package com.arcane.tournantscheduling.Frags;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcane.tournantscheduling.Adapter.SectionRecyclerAdapter;
import com.arcane.tournantscheduling.R;

import java.util.ArrayList;


public class SectionFrag extends Fragment {
    public static final String TAG = "SECTION_FRAG";

    private SectionRecyclerAdapter mAdapter;
    private RecyclerView recyclerView;
    ArrayList<String> mDataset;
    String newDate;
    long dateNumber;
    private SectionRecyclerAdapter.OnSectionSelectedListener mListener;

    public static SectionFrag newInstance() {
        return new SectionFrag();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getListenerFromContext(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_section, container, false);
        Bundle bundle = getArguments();
        if(bundle != null){
           newDate = bundle.getString(CreateScheduleFrag.SCHEDULE_DATE);
        }
        mDataset = new ArrayList<>();
        mDataset.add("Bar");
        mDataset.add("Floor");
        mDataset.add("Kitchen");
        mDataset.add("Managers");

        RecyclerView mRecyclerView =  root.findViewById(R.id.section_rec_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter
        mAdapter = new SectionRecyclerAdapter(mDataset,dateNumber, newDate,getContext());
        mAdapter.setOnSectionSelectedListener(mListener);
        mRecyclerView.setAdapter(mAdapter);

        return root;
    }
    private void getListenerFromContext(Context context) {
        if (context instanceof SectionRecyclerAdapter.OnSectionSelectedListener) {
            mListener = (SectionRecyclerAdapter.OnSectionSelectedListener) context;
        } else {
            throw new ClassCastException("Containing activity must " +
                    "implement OnPersonInteractionListener");
        }
    }
}
