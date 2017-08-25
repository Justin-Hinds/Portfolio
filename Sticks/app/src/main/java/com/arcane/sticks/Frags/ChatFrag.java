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

import com.arcane.sticks.adapters.ChatLogRecAdapter;
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


public class ChatFrag extends Fragment {
    private ChatLogRecAdapter mAdapter;
    private ArrayList myDataset = new ArrayList();
    private ChatLogRecAdapter.OnPlayerSelectedListener mListener;
    private Player mPlayer;
    private final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference("Users").child(userID);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getListenerFromContext(context);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MessageViewFrag.PLAYER_ARG,mPlayer);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(mPlayer == null){
          //  mPlayer = (Player) savedInstanceState.getSerializable(MessageViewFrag.PLAYER_ARG);

        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.chat_frag_layout,container,false);


        myDataset = new ArrayList();
        RecyclerView mRecyclerView = (RecyclerView) root.findViewById(R.id.rec_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ChatLogRecAdapter(myDataset,getContext());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnPlayerInteraction(mListener);
        //Map<String, Message> messageMap = new HashMap<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPlayer = dataSnapshot.getValue(Player.class);
               // Log.i("FRIENDS: ", mPlayer.getFellowPlayers().toString());
                assert mPlayer != null;
                for(String value : mPlayer.getFellowPlayers().keySet()){
                   // Log.d("VALUE: ", value);
                    DatabaseReference friendRef = database.getReference("Users").child(value);
                    friendRef.addValueEventListener(friendValueEvent);
                }
                //DatabaseReference friendsRef = database.getReference().child("Users").child(mPlayer.getFellowPlayers().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return root;
    }
    ValueEventListener userValueEvent = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private final ValueEventListener friendValueEvent = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
           // Log.d("PLAYER", dataSnapshot.getValue().toString());
            Player player = dataSnapshot.getValue(Player.class);
            //noinspection unchecked
            myDataset.add(player);
            //noinspection unchecked
            mAdapter.update(myDataset);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void getListenerFromContext(Context context) {
        if (context instanceof PlayersRecyclerAdapter.OnPlayerSelectedListener) {
            mListener = (ChatLogRecAdapter.OnPlayerSelectedListener) context;
        } else {
            throw new ClassCastException("Containing activity must " +
                    "implement OnPersonInteractionListener");
        }
    }
}
