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

import com.arcane.sticks.adapters.MainBoardRecyclerAdapter;
import com.arcane.sticks.models.Player;
import com.arcane.sticks.models.Post;
import com.arcane.sticks.adapters.ProfileRecyclerAdapter;
import com.arcane.sticks.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.arcane.sticks.frags.MessageViewFrag.PLAYER_ARG;


public class ProfilePageFrag extends Fragment {
    public static ProfilePageFrag newInstance(){return new ProfilePageFrag();}
    public static final String PLAYER_EXTRA = "com.arcane.sticks.PLAYER_EXTRA";
    public static final String PLAYER_ARG = "PLAYER_ARG";
    private ProfileRecyclerAdapter mAdapter;
    private ArrayList myDataset = new ArrayList();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myUserRef = database.getReference("Users");
    private final DatabaseReference myRef = database.getReference("User Posts");
    private Player mPlayer;
    String user;
    private ProfileRecyclerAdapter.AddFellowPlayerInterface mListener;
    private MainBoardRecyclerAdapter.OnItemSelected mPostListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getListenerFromContext(context);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PLAYER_ARG,mPlayer);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(mPlayer == null){
            assert savedInstanceState != null;
            mPlayer = (Player) savedInstanceState.getSerializable(PLAYER_ARG);

        }
    }
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
        mAdapter = new ProfileRecyclerAdapter(myDataset,mPlayer,getContext());
        mAdapter.setAddFellowPlayerInterface(mListener,mPostListener);
        mRecyclerView.setAdapter(mAdapter);
        user = mPlayer.getId();
        myRef.child(user).addValueEventListener(valueEventListener);
        return root;
    }
    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            myDataset.clear();
            for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
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
    public void setPlayer(Player player){
        mPlayer = player;
       // Log.i("SET PLAYER: " , player.toString());
    }
    private void getListenerFromContext(Context context) {
        if (context instanceof ProfileRecyclerAdapter.AddFellowPlayerInterface) {
            mListener = (ProfileRecyclerAdapter.AddFellowPlayerInterface) context;
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
