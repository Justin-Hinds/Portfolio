package com.arcane.sticks.frags;

import android.os.Bundle;
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

import com.arcane.sticks.adapters.MessageViewRecAdapter;
import com.arcane.sticks.models.DataManager;
import com.arcane.sticks.models.Message;
import com.arcane.sticks.models.Player;
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


public class MessageViewFrag extends Fragment {
    public static MessageViewFrag newInstance(){return new MessageViewFrag();}

    private EditText messageText;
    private Player mPlayer;

    public static final String TAG = ".MessageViewFrag: ";
    public static final String PLAYER_ARG = "PLAYER_ARG";
    private RecyclerView mRecyclerView;
    private MessageViewRecAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList myDataset = new ArrayList();
    private final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference("Messages");
    DatabaseReference userMessageRef = database.getReference("User Messages").child(userID);

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
        View root = inflater.inflate(R.layout.message_view_frag,container,false);


        ImageButton sendButton = (ImageButton) root.findViewById(R.id.send_button);
        ImageButton pickPhoto = (ImageButton) root.findViewById(R.id.camera_button);
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
        mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
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
                if(DataManager.stringValidate(messageText.toString()) != null){
                    Map<String,Object> messageValues = message.toMap();
                    String messageID = myRef.push().getKey();
                    Map<String,Object> childUpdates = new HashMap<>();
                    childUpdates.put("/Messages/" + messageID, messageValues);
                    childUpdates.put("/User Messages/" + userID + "/" + messageID, messageValues);
                    childUpdates.put("/User Messages/" + mPlayer.getId() + "/" + messageID, messageValues);
                    //myDataset.clear();
                    database.getReference().updateChildren(childUpdates);
                    messageText.setText("");
                    //noinspection unchecked
                    mAdapter.update(myDataset);
                    //scrollToBottom();
                }

            }
        });

        return root;
    }


    private void scrollToBottom() {
        mLayoutManager.smoothScrollToPosition(mRecyclerView, null, mAdapter.getItemCount());
        Log.i("SCROLL TO: ", mAdapter.getItemCount() + "");
    }

    private void observeMessages(){
        //myDataset.clear();
        userMessageRef.addChildEventListener(messageChildEventNested);
        //userMessageRef.addValueEventListener(messageChildEvent);
    }
    private final ChildEventListener messageChildEventNested = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
//            Log.i("PLAYER: ", mPlayer.getId());
//            Log.i("ChatPlayer: ",message.chatPlayerID());
                assert message != null;
                if (message.chatPlayerID().equals(mPlayer.getId())) {
//                Log.d("MESSAGE: ", message.getMessage());
                    //noinspection unchecked
                    myDataset.add(message);
                    //noinspection unchecked
                    mAdapter.update(myDataset);
                    scrollToBottom();


            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private final ValueEventListener messageChildEvent = new ValueEventListener() {


        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            //myRef.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(messageChildEventNested);
            for(DataSnapshot childSnap : dataSnapshot.getChildren()){
            Message message = childSnap.getValue(Message.class);
//            Log.i("PLAYER: ", mPlayer.getId());
//            Log.i("ChatPlayer: ",message.chatPlayerID());
                assert message != null;
                if(message.chatPlayerID().equals(mPlayer.getId())  ){
//                Log.d("MESSAGE: ", message.getMessage());
                //noinspection unchecked
                myDataset.add(message);
                //noinspection unchecked
                mAdapter.update(myDataset);
                scrollToBottom();

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



    @Override
    public void onDestroy() {
        super.onDestroy();
        userMessageRef.removeEventListener(messageChildEvent);
        myRef.removeEventListener(messageChildEventNested);

    }
}
