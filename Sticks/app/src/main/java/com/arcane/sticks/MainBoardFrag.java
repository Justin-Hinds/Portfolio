package com.arcane.sticks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainBoardFrag extends Fragment {
    public static final String TAG = "com.arcane.MainBoard:";
    public static final String MaindBoard_TAG = "MaindBoard_TAG";
    private RecyclerView mRecyclerView;
    private MainBoardRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList myDataset = new ArrayList();
    public static MainBoardFrag newInstance(){return new MainBoardFrag();}
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("messages");
    DataManager mDataManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.mainboard_frag_layout,container,false);
        mDataManager = new DataManager(getContext());
        mRecyclerView = (RecyclerView) root.findViewById(R.id.rec_card_view);
        if(mDataManager.readSavedData() != null){
            myDataset = mDataManager.readSavedData();
        }else{
            myDataset = new ArrayList();
        }

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MainBoardRecyclerAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                HashMap<String, HashMap> hashMap = (HashMap<String,HashMap>) dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + dataSnapshot.getValue());
                Post post = new Post();
                if(hashMap != null){
                    for (HashMap p : hashMap.values()){
                        post.setMessage(p.get("message").toString());
                        post.setUser(p.get("user").toString());
                        Log.d("POST: ", p.get("message").toString());
                    myDataset.add(post);
                    }
                    Log.d("DATASET", myDataset.toString());
                    mAdapter.update(myDataset);

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return root;
    }
}
