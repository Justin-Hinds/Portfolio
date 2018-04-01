package com.arcane.tournantscheduling.Frags;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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

import com.arcane.tournantscheduling.Adapter.MessageViewRecyclerAdapter;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.Utils.DataManager;
import com.arcane.tournantscheduling.Models.Message;
import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.ViewModels.MessagesViewModel;
import com.arcane.tournantscheduling.ViewModels.RosterViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class MessageViewFrag extends Fragment {
    public static final String TAG = "MESSAGE_VIEW_FRAG";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RosterViewModel rosterViewModel;
    EditText messageText;
    Staff currentUser;
    Staff chatBuddy;
    ArrayList<Message> mDataset;
    MessageViewRecyclerAdapter mAdapter;
    public static MessageViewFrag newInstance() {
        return new MessageViewFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_message_view, container, false);
        mDataset = new ArrayList<>();

        mAdapter = new MessageViewRecyclerAdapter(mDataset);
        rosterViewModel = ViewModelProviders.of(getActivity()).get(RosterViewModel.class);
        currentUser = rosterViewModel.getCurrentUser();
        chatBuddy = rosterViewModel.getChatBuddy();
        MessagesViewModel messagesViewModel = ViewModelProviders.of(getActivity()).get(MessagesViewModel.class);
        messagesViewModel.setCurrentUser(currentUser);
        messagesViewModel.setChatBuddy(chatBuddy);
        messagesViewModel.getLiveChat(rosterViewModel.getChatBuddy()).observe(getActivity(), new Observer<ArrayList<Message>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Message> messages) {
                if(messages != null){
                    Log.d("LIVE MESSAGES", messages.size()+"");
                    mAdapter.update(messages);
                }
            }
        });

        ImageButton sendButton = root.findViewById(R.id.send_button);
         messageText = root.findViewById(R.id.editText_message);

        RecyclerView mRecyclerView =  root.findViewById(R.id.rec_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter
        mRecyclerView.setAdapter(mAdapter);



        sendButton.setOnClickListener(v -> {
            String messageString = DataManager.stringValidate(messageText.getText().toString());
            if(messageString != null){
                Message message = new Message();
                message.setSender(currentUser.getId());
                message.setReceiver(chatBuddy.getId());
                message.setTime(System.currentTimeMillis());
                message.setMessage(messageText.getText().toString());
                sendText(message);
            }
        });
        return root;
    }

    private void sendText(Message message) {

        db.collection("Restaurants").document(currentUser.getRestaurantID()).collection("Messages").add(message);
        messageText.setText("");
    }

}
