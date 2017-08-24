package com.arcane.sticks.Frags;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcane.sticks.Activities.CommentsActivity;
import com.arcane.sticks.Adapters.MainBoardRecyclerAdapter;
import com.arcane.sticks.Models.DataManager;
import com.arcane.sticks.Models.Post;
import com.arcane.sticks.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainBoardFrag extends Fragment {
    public static final String TAG = "com.arcane.MainBoard:";
    public static final String MaindBoard_TAG = "MaindBoard_TAG";
    private RecyclerView mRecyclerView;
    private MainBoardRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList myDataset = new ArrayList();
    MainBoardRecyclerAdapter.OnItemSelected mListener;
    public static final String POST_EXTRA = "com.arcane.sticks.POST_EXTRA";
    Context mContext;
    public static MainBoardFrag newInstance(){return new MainBoardFrag();}
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Posts");
    DataManager mDataManager;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        getListenerFromContext(context);
    }

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
        mAdapter = new MainBoardRecyclerAdapter(myDataset,getContext());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnInteraction(mListener);
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("USER: ", user);
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                myDataset.clear();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                //Log.d(TAG, "Value is: "  + childSnapshot.getValue(Post.class));
                Post post = childSnapshot.getValue(Post.class);
                   // Log.d(TAG, "Time is: "  + post.time);
                    myDataset.add(post);

                }
                  //  Log.d("DATASET", myDataset.toString());
                    mAdapter.update(myDataset);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return root;
    }
    public void openComments(Post post, Context context){
        Intent intent = new Intent(context, CommentsActivity.class);
        intent.putExtra(POST_EXTRA,post);
        startActivity(intent);
    }
    private void getListenerFromContext(Context context) {
        if (context instanceof MainBoardRecyclerAdapter.OnItemSelected) {
            mListener = (MainBoardRecyclerAdapter.OnItemSelected) context;
        } else {
            throw new ClassCastException("Containing activity must " +
                    "implement OnPersonInteractionListener");
        }
    }
}
