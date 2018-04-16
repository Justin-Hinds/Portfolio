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

import com.arcane.tournantscheduling.Adapter.RosterRecyclerAdapter;
import com.arcane.tournantscheduling.Models.Message;
import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.ViewModels.MessagesViewModel;
import com.arcane.tournantscheduling.ViewModels.RosterViewModel;

import java.util.ArrayList;
import java.util.Iterator;


public class MessagesFrag extends Fragment {


    public static MessagesFrag newInstance() {
        return new MessagesFrag();
    }
    public static final String TAG = "MESSAGE_FRAG";
    RosterRecyclerAdapter mAdapter;
    ArrayList<Staff> mDataset;
    ArrayList<Message> messages;
    RosterViewModel rosterViewModel;
    RosterRecyclerAdapter.OnStaffSelectedListener mListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_messages,container,false);
        RecyclerView mRecyclerView =  root.findViewById(R.id.roster_rec_view);
        mDataset = new ArrayList<>();
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


        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

                Log.d("FAB ", "");
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                ArrayList<Staff> staffArrayList = rosterViewModel.getUsers().getValue();
                Staff currentUser = rosterViewModel.getCurrentUser();
                assert staffArrayList != null;
                Log.d("BEFORE", staffArrayList.size() + "");
                for (int i = 0; i < staffArrayList.size(); i++){
                    if(staffArrayList.get(i).getId().equals(currentUser.getId())){
                        staffArrayList.remove(i);
                    }
                }
                Log.d("AFTER", staffArrayList.size() + "");

            mAdapter.update(staffArrayList);
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_view,frag).commit();

        });
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getListenerFromContext(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        MessagesViewModel messagesViewModel = ViewModelProviders.of(getActivity()).get(MessagesViewModel.class);
         rosterViewModel = ViewModelProviders.of(getActivity()).get(RosterViewModel.class);
        ArrayList<Staff> staffArrayList = new ArrayList<>(rosterViewModel.getUsers().getValue());
        messagesViewModel.setCurrentUser(rosterViewModel.getCurrentUser());
       messagesViewModel.getMessages(staffArrayList).observe(getActivity(), new Observer<ArrayList<Staff>>() {
           @Override
           public void onChanged(@Nullable ArrayList<Staff> staff1) {
               assert staff1 != null;
               ArrayList arrayList = new ArrayList(staff1);
               Iterator iterator = arrayList.iterator();

            mAdapter.update(staff1);
           }
       });
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
