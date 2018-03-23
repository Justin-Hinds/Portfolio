package com.arcane.tournantscheduling;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class SectionFrag extends Fragment {
    public static final String TAG = "SECTION_FRAG";

    private SectionRecyclerAdapter mAdapter;
    private RecyclerView recyclerView;
    ArrayList<String> mDataset;
    public static SectionFrag newInstance() {
        return new SectionFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_section, container, false);

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
        mAdapter = new SectionRecyclerAdapter(mDataset, getContext());

        mRecyclerView.setAdapter(mAdapter);


        return root;
    }

}
