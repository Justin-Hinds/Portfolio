package com.arcane.thedish.Frags;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcane.thedish.Adapters.MainBoardRecyclerAdapter;
import com.arcane.thedish.Adapters.ProfileRecyclerAdapter;
import com.arcane.thedish.Models.DishUser;
import com.arcane.thedish.Models.Post;
import com.arcane.thedish.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ProfilePageFrag extends Fragment {
    public static final String PLAYER_EXTRA = "com.arcane.sticks.PLAYER_EXTRA";
    private static final String PLAYER_ARG = "PLAYER_ARG";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference("User Posts");
    DatabaseReference myUserRef = database.getReference("Users");
    private String user;
    private ProfileRecyclerAdapter mAdapter;
    private ArrayList myDataset = new ArrayList();
    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            myDataset.clear();
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                Post post = childSnapshot.getValue(Post.class);
                //noinspection unchecked
                myDataset.add(post);

            }
            //  Log.d("DATASET", myDataset.toString());
            //noinspection unchecked
            mAdapter.update(myDataset);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private DishUser mDishUser;
    private ProfileRecyclerAdapter.AddFellowStaffInterface mListener;
    private MainBoardRecyclerAdapter.OnItemSelected mPostListener;

    public static ProfilePageFrag newInstance() {
        return new ProfilePageFrag();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getListenerFromContext(context);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PLAYER_ARG, mDishUser);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (mDishUser == null) {
            assert savedInstanceState != null;
            mDishUser = (DishUser) savedInstanceState.getSerializable(PLAYER_ARG);

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.user_frag_layout, container, false);


        SearchView searchView = root.findViewById(R.id.searchview);
        searchView.removeAllViews();


        myDataset = new ArrayList();
        RecyclerView mRecyclerView = root.findViewById(R.id.rec_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ProfileRecyclerAdapter(myDataset, mDishUser, getContext());
        mAdapter.setAddFellowStaffInterface(mListener, mPostListener);
        mRecyclerView.setAdapter(mAdapter);
        user = mDishUser.getId();
        myRef.child(user).addValueEventListener(valueEventListener);
        return root;
    }

    public void setPlayer(DishUser dishUser) {
        mDishUser = dishUser;
        // Log.i("SET PLAYER: " , dishUser.toString());
    }

    private void getListenerFromContext(Context context) {
        if (context instanceof ProfileRecyclerAdapter.AddFellowStaffInterface) {
            mListener = (ProfileRecyclerAdapter.AddFellowStaffInterface) context;
            mPostListener = (MainBoardRecyclerAdapter.OnItemSelected) context;
        } else {
            throw new ClassCastException("Containing activity must " +
                    "implement OnPersonInteractionListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myRef.child(user).removeEventListener(valueEventListener);
    }
}
