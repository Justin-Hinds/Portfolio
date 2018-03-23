package com.arcane.tournantscheduling;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcane.tournantscheduling.Models.Staff;

import java.util.ArrayList;


public class RosterFrag extends Fragment {
    private RosterRecyclerAdapter mAdapter;
    private Staff mUser;
    private ArrayList<Staff> myDataset = new ArrayList();
    private RosterRecyclerAdapter.OnStaffSelectedListener mListener;


    public static RosterFrag newInstance() {
        return new RosterFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_roster, container, false);

        RecyclerView mRecyclerView =  root.findViewById(R.id.roster_rec_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter
        mAdapter = new RosterRecyclerAdapter(myDataset, getContext());
        mRecyclerView.setAdapter(mAdapter);



        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("FAB ", "HIT");
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                CreateStaffFrag frag = CreateStaffFrag.newInstance();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_view,frag).commit();
            }
        });
        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof RosterRecyclerAdapter.OnStaffSelectedListener) {
//            mListener = (RosterRecyclerAdapter.OnStaffSelectedListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
