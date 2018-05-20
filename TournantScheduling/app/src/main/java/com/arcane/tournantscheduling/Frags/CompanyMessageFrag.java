package com.arcane.tournantscheduling.Frags;


import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arcane.tournantscheduling.Models.CompanyMessage;
import com.arcane.tournantscheduling.Models.Message;
import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.Utils.DataManager;
import com.arcane.tournantscheduling.ViewModels.RosterViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class CompanyMessageFrag extends Fragment {
    public static final String TAG = "COMPANY_MESSAGE_FRAG";
    EditText editText;
    Button sendButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RosterViewModel rosterViewModel;
    Staff currentUser;
    ArrayList<String> tokens = new ArrayList<>();
    public static CompanyMessageFrag newInstance() {
        return new CompanyMessageFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_company_message, container, false);
        editText = root.findViewById(R.id.editText_message);
        sendButton = root.findViewById(R.id.button_send);
        rosterViewModel = ViewModelProviders.of(getActivity()).get(RosterViewModel.class);
        currentUser = rosterViewModel.getCurrentUser();
        ArrayList<Staff> staffArrayList = new ArrayList<>(rosterViewModel.getUsers().getValue());
        for (Staff staff : staffArrayList){
            if(staff.getDeviceToken() != null){
                tokens.add(staff.getDeviceToken());
            }
        }
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        String message = DataManager.stringValidate(editText.getText().toString());
                Log.d("MESSAGE", editText.getText().toString());
                if(message != null){
                    CompanyMessage companyMessage = new CompanyMessage();
                    companyMessage.setSender(currentUser.getId());
                    companyMessage.setTime(System.currentTimeMillis());
                    companyMessage.setMessage(message);
                    companyMessage.setCompanyList(tokens);
                    companyMessage.setSenderName(currentUser.getName());

                    AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle("Company Message")
                            .setMessage("Are you sure you want to send this message?" )
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sendText(companyMessage);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
        });


        return root;
    }
    private void sendText(CompanyMessage message) {
        db.collection("Restaurants").document(currentUser.getRestaurantID()).collection("CompanyMessages").add(message)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getContext(),"Company message sent", Toast.LENGTH_SHORT).show();
            }
        });
        editText.setText("");
    }
}
