package com.arcane.tournantscheduling.Frags;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.arcane.tournantscheduling.Adapter.RosterRecyclerAdapter;
import com.arcane.tournantscheduling.Models.Message;
import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.ViewModels.RosterViewModel;

import java.util.ArrayList;


public class MessageGroupFrag extends Fragment {
    public static final String TAG = "MESSAGE_GROUP_FRAG";
    RosterRecyclerAdapter mAdapter;
    ArrayList<Staff> mDataset;
    ArrayList<Message> messages;
    RosterViewModel rosterViewModel;
    RosterRecyclerAdapter.OnStaffSelectedListener mListener;
//    FloatingActionButton fab,fabSingle, fabGlobal;
//    Animation open,close,rotateClockwise,rotateCounter;
//    Boolean isOpen = false;

    public static MessageGroupFrag newInstance() {
        return new MessageGroupFrag();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getListenerFromContext(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_message_group, container, false);
        RecyclerView mRecyclerView =  root.findViewById(R.id.roster_rec_view);
        rosterViewModel = ViewModelProviders.of(getActivity()).get(RosterViewModel.class);
        mDataset = new ArrayList<>(rosterViewModel.getUsers().getValue());
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter
        mAdapter = new RosterRecyclerAdapter(mDataset, getContext(),TAG);
        mAdapter.setOnStaffSelectedListener(mListener);
        mRecyclerView.setAdapter(mAdapter);

        return root;
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
