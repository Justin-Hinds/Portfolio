package com.arcane.tournantscheduling.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.arcane.tournantscheduling.Models.Message;
import com.arcane.tournantscheduling.Models.Staff;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;

import javax.annotation.meta.When;


public class MessagesViewModel extends ViewModel {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<ArrayList<Staff>> liveMessages;
    private MutableLiveData<ArrayList<Message>> liveChat;
    private Staff currentUser;
    private Staff chatBuddy;
    private ArrayList<Message> transcript = new ArrayList<>();
    ArrayList<Staff> staffList;
    public MessagesViewModel(){
        //Log.d("Messages VIEW MODEL ", "CONSTRUCTOR");
    }
    public LiveData<ArrayList<Staff>> getMessages(ArrayList<Staff> users){
        staffList = users;
        if(liveMessages == null){
            liveMessages = new MutableLiveData<>();
            if(currentUser != null && staffList != null){
            getUserReceivedMessages(getUserSentMessages());
            }else {
                Log.d("LiveMessages Get User", "NULL");
            }
            return liveMessages;
        }
        return liveMessages;
    }
    public LiveData<ArrayList<Message>> getLiveChat(Staff buddy){
        if(liveChat == null){
            liveChat = new MutableLiveData<>();

            getBuddyMessages(buddy, getChatMessages(buddy));
            //Log.d("LIVECHAT", transcript.size() + "");
            return liveChat;
        }
        return liveChat;
    }

    private ArrayList getUserSentMessages() {
        ArrayList<String> messageArrayList = new ArrayList<>();
        db.collection("Restaurants").document(currentUser.getRestaurantID()).collection("Messages")
                .whereEqualTo("sender",currentUser.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
                if (e == null) {
                    Log.w("Messages VIEW MODEL ", "Listen failed.", e);
                    for(DocumentSnapshot documentSnapshot : querySnapshot){
                        if(documentSnapshot.get("sender") != null){
                            Message message = documentSnapshot.toObject(Message.class);
                            Log.d("SENT", documentSnapshot.get("sender").toString());
                            if (!messageArrayList.contains(message.getReceiver())){
                                messageArrayList.add(message.getReceiver());
                            }
                        }
                    }
                }else {

                }

            }
        });
        return messageArrayList;
    }
    private ArrayList<Staff> getUserReceivedMessages( ArrayList<String> sentMessages) {
        ArrayList<String> userIdList = sentMessages;
        ArrayList<Staff> userArrayList = new ArrayList<>();
        Log.d("USER LIST MESSAGES sent", sentMessages.size() + "");

        db.collection("Restaurants").document(currentUser.getRestaurantID()).collection("Messages")
                .whereEqualTo("receiver",currentUser.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
                if (e == null) {
                    for(DocumentSnapshot documentSnapshot : querySnapshot){
                        if(documentSnapshot.get("receiver") != null){
                            Message message = documentSnapshot.toObject(Message.class);
                            Log.d("Received", documentSnapshot.get("sender").toString());
                            if(!userIdList.contains(message.getSender())){
                                userIdList.add(message.getSender());
                            }
                        }
                    }
                    if(staffList != null){
                        for(String id : userIdList){
                            Iterator<Staff> itr = staffList.iterator();
                            while (itr.hasNext()) {
                                Staff  staff = itr.next();
                                if (staff.getId().equals(id)) {
                                    userArrayList.add(staff);
                                }
                                System.out.print(staff + " ");
                            }
                        }
//                        for(int i = 0; i<staffList.size(); i++){
//                            if(staffList.get(i).getId().equals(userIdList.get(i))){
//                                userArrayList.add(staffList.get(i));
//                            }
//                        }
                    }
                    Log.d("USER LIST MESSAGES", userArrayList.size() + "");
                    liveMessages.postValue(userArrayList);
                }else {
                    Log.d("ERROR", "Something went wrong");
                }

            }
        });
        return userArrayList;
    }
public ArrayList<Message> getChatMessages(Staff buddy){
        ArrayList<Message> messageArrayList = new ArrayList<>();
    db.collection("Restaurants").document(currentUser.getRestaurantID()).collection("Messages")
            .whereEqualTo("receiver",currentUser.getId()).whereEqualTo("sender",buddy.getId())
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
                    if(e == null){
                        for (DocumentSnapshot documentSnapshot: querySnapshot) {

                            Message message = documentSnapshot.toObject(Message.class);
                            //Log.d("CHAT", documentSnapshot.get("sender").toString());
                            if(!messageArrayList.contains(message)){
                                transcript.add(message);
                                messageArrayList.add(message);
                            }
                        }
                    //Log.d("CHAT ME 2", transcript.size() + "");
                        liveChat.postValue(messageArrayList);
                    }
                }
            });
    for (Message message : messageArrayList){
        Log.d("TRANSCRIPT", message.getMessage());
    }
    return messageArrayList;
}
private ArrayList<Message> getBuddyMessages(Staff buddy, ArrayList<Message> messages){
    ArrayList<Message> messageArrayList = messages;
    db.collection("Restaurants").document(currentUser.getRestaurantID()).collection("Messages")
            .whereEqualTo("receiver",buddy.getId()).whereEqualTo("sender",currentUser.getId())
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
                    if(e == null){
                        for (DocumentSnapshot documentSnapshot: querySnapshot) {
                            Message message = documentSnapshot.toObject(Message.class);
                            //Log.d("CHAT", documentSnapshot.get("sender").toString());
                            if(!messageArrayList.contains(message)){
                            transcript.add(message);
                            messageArrayList.add(message);
                            }
                        }
                   // Log.d("CHAT Buddy", transcript.size() + "");
                    liveChat.postValue(messageArrayList);
                    }
                    for (Message message : messageArrayList){
                        Log.d(" FINAL TRANSCRIPT", message.getMessage());
                    }
                }

            });

    return messageArrayList;
}
    public Staff getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Staff currentUser) {
        this.currentUser = currentUser;
    }

    public Staff getChatBuddy() {
        return chatBuddy;
    }

    public void setChatBuddy(Staff chatBuddy) {
        this.chatBuddy = chatBuddy;
    }
}
