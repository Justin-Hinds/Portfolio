package com.arcane.tournantscheduling.Frags;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import java.util.Collection;
import java.util.Collections;


public class MessageViewFrag extends Fragment {
    public static final String TAG = "MESSAGE_VIEW_FRAG";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RosterViewModel rosterViewModel;
    EditText messageText;
    Staff currentUser;
    Staff chatBuddy;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Message> mDataset;
    MessageViewRecyclerAdapter mAdapter;
    public static MessageViewFrag newInstance() {
        return new MessageViewFrag();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mAdapter.getItemCount() > 0){
        scrollToBottom();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_message_view, container, false);
        rosterViewModel = ViewModelProviders.of(getActivity()).get(RosterViewModel.class);
        currentUser = rosterViewModel.getCurrentUser();
        chatBuddy = rosterViewModel.getChatBuddy();

//        rosterViewModel.getStaffMemberLiveData(chatBuddy.getId()).getValue();
        MessagesViewModel messagesViewModel = ViewModelProviders.of(getActivity()).get(MessagesViewModel.class);
        messagesViewModel.setCurrentUser(currentUser);
        messagesViewModel.setChatBuddy(chatBuddy);
//        myTimeOffList = new ArrayList<>(messagesViewModel.getLiveChat(chatBuddy).getValue());
//        Collections.sort(myTimeOffList);
        mDataset = new ArrayList<>();
        mAdapter = new MessageViewRecyclerAdapter(mDataset);
        messagesViewModel.getLiveChat(rosterViewModel.getChatBuddy()).observe(getActivity(), new Observer<ArrayList<Message>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Message> messages) {
                if(messages != null){
                    Log.d("LIVE MESSAGES", messages.size()+"");
                    for (Message message : messages){
                        Log.d("Participants", message.getSender() + " ->" + message.getReceiver());
                    }
                    Collections.sort(messages);
                    mAdapter.update(messages);
                    scrollToBottom();
                }
            }
        });

        ImageButton sendButton = root.findViewById(R.id.send_button);
         messageText = root.findViewById(R.id.editText_message);

         mRecyclerView = root.findViewById(R.id.rec_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
         mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) scrollToBottom();
            }
        });


        sendButton.setOnClickListener(v -> {
            String messageString = DataManager.stringValidate(messageText.getText().toString());
            if(messageString != null){
                Message message = new Message();
                message.setSender(currentUser.getId());
                message.setReceiver(chatBuddy.getId());
                message.setTime(System.currentTimeMillis());
                message.setMessage(messageText.getText().toString());
                message.setDeviceToken(chatBuddy.getDeviceToken());
                message.setSenderName(currentUser.getName());
                sendText(message);
            }
        });
                //scrollToBottom();
        return root;
    }
    private void scrollToBottom() {
        mLayoutManager.smoothScrollToPosition(mRecyclerView, null, mAdapter.getItemCount());
        Log.i("SCROLL TO: ", mAdapter.getItemCount() + "");
    }
    private void sendText(Message message) {

        db.collection("Restaurants").document(currentUser.getRestaurantID()).collection("Messages").add(message);
        messageText.setText("");
    }

}
