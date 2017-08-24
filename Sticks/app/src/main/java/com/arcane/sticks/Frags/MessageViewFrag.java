package com.arcane.sticks.Frags;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.arcane.sticks.Adapters.CommentsRecyclerAdapter;
import com.arcane.sticks.Adapters.MessageViewRecAdapter;
import com.arcane.sticks.Models.Message;
import com.arcane.sticks.Models.Player;
import com.arcane.sticks.Models.Post;
import com.arcane.sticks.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MessageViewFrag extends Fragment {
    public static MessageViewFrag newInstance(){return new MessageViewFrag();}
    ImageButton sendButton, pickPhoto;
    EditText messageText;
    Player mPlayer;

    public static final String TAG = ".MessageViewFrag: ";
    public static final String PLAYER_ARG = "PLAYER_ARG";
    private RecyclerView mRecyclerView;
    private MessageViewRecAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList myDataset = new ArrayList();
    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Messages");

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PLAYER_ARG,mPlayer);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(mPlayer == null){
        mPlayer = (Player) savedInstanceState.getSerializable(PLAYER_ARG);

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.message_view_frag,container,false);


        sendButton = (ImageButton) root.findViewById(R.id.send_button);
        pickPhoto = (ImageButton) root.findViewById(R.id.camera_button);
        messageText = (EditText) root.findViewById(R.id.message_text);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.rec_view);
        myDataset = new ArrayList();

        // mDataManager = new DataManager(getContext());
//        mRecyclerView = (RecyclerView) root.findViewById(R.id.rec_card_view);
//        if(mDataManager.readSavedData() != null){
//            myDataset = mDataManager.readSavedData();
//        }else{
//            myDataset = new ArrayList();
//        }

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MessageViewRecAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) scrollToBottom();
            }
        });



        observeMessages();


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.setSender(userID);
                message.setReceiver(mPlayer.getId());
                message.setTime(System.currentTimeMillis());
                message.setMessage(messageText.getText().toString());
                Map<String,Object> messageValues = message.toMap();
                String messageID = myRef.push().getKey();
                Map<String,Object> childUpdates = new HashMap<>();
                childUpdates.put("/Messages/" + messageID, messageValues);
                childUpdates.put("/User Messages/" + userID + "/" + messageID, messageValues);
                childUpdates.put("/User Messages/" + mPlayer.getId() + "/" + messageID, messageValues);
                database.getReference().updateChildren(childUpdates);
                messageText.setText("");
                mAdapter.update(myDataset);
                scrollToBottom();
            }
        });

        return root;
    }


    private void scrollToBottom() {
        mLayoutManager.smoothScrollToPosition(mRecyclerView, null, mAdapter.getItemCount());
    }

    private void observeMessages(){
        DatabaseReference messageRef = database.getReference("User Messages").child(userID);
        myDataset.clear();
        messageRef.addValueEventListener(messageChildEvent);
//        messageRef.addChildEventListener(childEventListener);
    }

    ValueEventListener messageChildEvent = new ValueEventListener() {


        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            DatabaseReference messageRef = database.getReference("Messages").child(dataSnapshot.getKey());
            messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Message message = dataSnapshot.getValue(Message.class);
                    Log.i("PLAYER: ", mPlayer.getId());
                    Log.i("ChatPlayer: ",dataSnapshot.toString());
//                    if(message.chatPlayerID().equals(mPlayer.getId())  ){
//                Log.d("MESSAGE: ", message.getMessage());
////                        myDataset.add(message);
////                        mAdapter.update(myDataset);
//                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            for(DataSnapshot childSnap : dataSnapshot.getChildren()){
            Message message = childSnap.getValue(Message.class);
//            Log.i("PLAYER: ", mPlayer.getId());
//            Log.i("ChatPlayer: ",message.chatPlayerID());
            if(message.chatPlayerID().equals(mPlayer.getId())  ){
//                Log.d("MESSAGE: ", message.getMessage());
                myDataset.add(message);
                mAdapter.update(myDataset);
                //scrollToBottom();

            }

            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public void setmPlayer(Player player){
        mPlayer = player;
    }
}
