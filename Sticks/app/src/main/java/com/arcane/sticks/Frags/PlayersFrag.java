package com.arcane.sticks.frags;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcane.sticks.models.Player;
import com.arcane.sticks.adapters.PlayersRecyclerAdapter;
import com.arcane.sticks.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class PlayersFrag extends Fragment {
    private PlayersRecyclerAdapter mAdapter;
    private ArrayList myDataset = new ArrayList();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference("Users");
    public static PlayersFrag newInstance(){return new PlayersFrag();}
    private PlayersRecyclerAdapter.OnPlayerSelectedListener mListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.players_frag_layout,container,false);

        myDataset = new ArrayList();
        RecyclerView mRecyclerView = (RecyclerView) root.findViewById(R.id.rec_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new PlayersRecyclerAdapter(myDataset,getContext());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnPlayerInteraction(mListener);


        myRef.addValueEventListener(valueEventListener);
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getListenerFromContext(context);
    }

    private void getListenerFromContext(Context context) {
        if (context instanceof PlayersRecyclerAdapter.OnPlayerSelectedListener) {
            mListener = (PlayersRecyclerAdapter.OnPlayerSelectedListener) context;
        } else {
            throw new ClassCastException("Containing activity must " +
                    "implement OnPersonInteractionListener");
        }
    }
    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //Log.i("USERS: ", dataSnapshot.getValue().toString());
            for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                //Log.d("USERS ", "Name is: "  + childSnapshot.getValue(Player.class).getName());
                Player player = childSnapshot.getValue(Player.class);
                // Log.d(TAG, "Time is: "  + post.time);
                //noinspection unchecked
                if(!player.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                myDataset.add(player);

                }



            }
            //noinspection unchecked
            mAdapter.update(myDataset);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    @Override
    public void onPause() {
        myRef.removeEventListener(valueEventListener);
        super.onPause();
    }
}
