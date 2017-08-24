package com.arcane.sticks.Frags;

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

import com.arcane.sticks.Models.Player;
import com.arcane.sticks.Models.Post;
import com.arcane.sticks.Adapters.ProfileRecyclerAdapter;
import com.arcane.sticks.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ProfilePageFrag extends Fragment {
    public static ProfilePageFrag newInstance(){return new ProfilePageFrag();}
    public static final String PLAYER_EXTRA = "com.arcane.sticks.PLAYER_EXTRA";
    private RecyclerView mRecyclerView;
    private ProfileRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList myDataset = new ArrayList();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myUserRef = database.getReference("Users");
    DatabaseReference myRef = database.getReference("User Posts");
    Player mPlayer;
    ProfileRecyclerAdapter.AddFellowPlayerInterface mListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getListenerFromContext(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.players_frag_layout,container,false);

        myDataset = new ArrayList();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.rec_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ProfileRecyclerAdapter(myDataset,mPlayer);
        mAdapter.setAddFellowPlayerInterface(mListener);
        mRecyclerView.setAdapter(mAdapter);
        String user = mPlayer.getId();
        myRef.child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                myDataset.clear();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    Post post = childSnapshot.getValue(Post.class);
                    myDataset.add(post);

                }
                //  Log.d("DATASET", myDataset.toString());
                mAdapter.update(myDataset);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        return root;
    }

    public void setPlayer(Player player){
        mPlayer = player;
       // Log.i("SET PLAYER: " , player.toString());
    }
    private void getListenerFromContext(Context context) {
        if (context instanceof ProfileRecyclerAdapter.AddFellowPlayerInterface) {
            mListener = (ProfileRecyclerAdapter.AddFellowPlayerInterface) context;
        } else {
            throw new ClassCastException("Containing activity must " +
                    "implement OnPersonInteractionListener");
        }
    }
}
