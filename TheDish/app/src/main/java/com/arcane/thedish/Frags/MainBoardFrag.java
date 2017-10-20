package com.arcane.thedish.Frags;

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

import com.arcane.thedish.Adapters.MainBoardRecyclerAdapter;
import com.arcane.thedish.Models.DataManager;
import com.arcane.thedish.Models.Post;
import com.arcane.thedish.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


@SuppressWarnings("unchecked")
public class MainBoardFrag extends Fragment {
    public static final String MaindBoard_TAG = "MaindBoard_TAG";
    public static final String POST_EXTRA = "com.arcane.sticks.POST_EXTRA";
    private static final String TAG = "com.arcane.MainBoard:";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference("Posts");
    private MainBoardRecyclerAdapter mAdapter;
    private final ArrayList<Post> myDataset = new ArrayList();
    private MainBoardRecyclerAdapter.OnItemSelected mListener;
    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            myDataset.clear();
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                Post post = childSnapshot.getValue(Post.class);
                // Log.d(TAG, "Time is: "  + post.time);
                //noinspection unchecked
                myDataset.add(post);

            }
            //Log.d("DATA SET", myDataset.toString());
            //noinspection unchecked
            DataManager.sortingPosts(myDataset);

            mAdapter.update(myDataset);


        }

        @Override
        public void onCancelled(DatabaseError error) {
            // Failed to read value
            Log.w(TAG, "Failed to read value.", error.toException());
        }
    };

    public static MainBoardFrag newInstance() {
        return new MainBoardFrag();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getListenerFromContext(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.mainboard_frag_layout, container, false);
//        DataManager mDataManager = new DataManager(getContext());
        RecyclerView mRecyclerView =  root.findViewById(R.id.rec_card_view);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        mAdapter = new MainBoardRecyclerAdapter(myDataset, getContext());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnInteraction(mListener);
//        Log.d("USER: ", user);
        // Read from the database
        observePosts();
//        Log.i("onCreateView", "HIT");

        return root;
    }

    private void observePosts() {
        myRef.addValueEventListener(valueEventListener);
    }

    private void getListenerFromContext(Context context) {
        if (context instanceof MainBoardRecyclerAdapter.OnItemSelected) {
            mListener = (MainBoardRecyclerAdapter.OnItemSelected) context;
        } else {
            throw new ClassCastException("Containing activity must " +
                    "implement OnPersonInteractionListener");
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myRef.removeEventListener(valueEventListener);

    }
}
