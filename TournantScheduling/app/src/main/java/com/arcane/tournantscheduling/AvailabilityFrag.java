package com.arcane.tournantscheduling;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.arcane.tournantscheduling.Models.Availability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AvailabilityFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private  FirebaseAuth mAuth;

    private Spinner mondaySpinner;
    private Spinner tuesdaySpinner;
    private Spinner wednesdaySpinner;
    private Spinner thursdaySpinner;
    private Spinner fridaySpinner;
    private Spinner saturdaySpinner;
    private Spinner sundaySpinner;
    Availability availability;

    public static AvailabilityFrag newInstance(){
        return new AvailabilityFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_availability,container,false);

        mAuth = FirebaseAuth.getInstance();
        setUpSpinners(root);

        Button changeButton = root.findViewById(R.id.button_change_availability);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setAvailability(root);
            }
        });

        return root;
    }
    private void setUpSpinners(View view){
         mondaySpinner = view.findViewById(R.id.spinner_monday);
         tuesdaySpinner = view.findViewById(R.id.spinner_tuesday);
         wednesdaySpinner = view.findViewById(R.id.spinner_wednesday);
         thursdaySpinner = view.findViewById(R.id.spinner_thursday);
         fridaySpinner = view.findViewById(R.id.spinner_friday);
         saturdaySpinner = view.findViewById(R.id.spinner_saturday);
         sundaySpinner = view.findViewById(R.id.spinner_sunday);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                getActivity().getResources().getStringArray(R.array.availability_options));

        mondaySpinner.setAdapter(spinnerAdapter);
        mondaySpinner.setOnItemSelectedListener(this);
        tuesdaySpinner.setAdapter(spinnerAdapter);
        tuesdaySpinner.setOnItemSelectedListener(this);
        wednesdaySpinner.setAdapter(spinnerAdapter);
        wednesdaySpinner.setOnItemSelectedListener(this);
        thursdaySpinner.setAdapter(spinnerAdapter);
        tuesdaySpinner.setOnItemSelectedListener(this);
        fridaySpinner.setAdapter(spinnerAdapter);
        fridaySpinner.setOnItemSelectedListener(this);
        saturdaySpinner.setAdapter(spinnerAdapter);
        saturdaySpinner.setOnItemSelectedListener(this);
        sundaySpinner.setAdapter(spinnerAdapter);
        sundaySpinner.setOnItemSelectedListener(this);
    }

    private void setAvailability(View view){

         availability = new Availability();

        availability.setMonday(mondaySpinner.getSelectedItemPosition());
        availability.setTuesday(tuesdaySpinner.getSelectedItemPosition());
        availability.setWednesday(wednesdaySpinner.getSelectedItemPosition());
        availability.setThursday(thursdaySpinner.getSelectedItemPosition());
        availability.setFriday(fridaySpinner.getSelectedItemPosition());
        availability.setSaturday(saturdaySpinner.getSelectedItemPosition());
        availability.setSunday(sundaySpinner.getSelectedItemPosition());

        mDatabase.child("Users").child(mAuth.getUid()).child("Availability").setValue(availability);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 0:
                Toast.makeText(getContext(),"None",Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(getContext(),"Mornings",Toast.LENGTH_SHORT).show();

                break;
            case 2:
                Toast.makeText(getContext(),"Nights",Toast.LENGTH_SHORT).show();

                break;
            case 3:
                Toast.makeText(getContext(),"Available",Toast.LENGTH_SHORT).show();

                break;

            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    ValueEventListener availabilityListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Availability avail = dataSnapshot.getValue(Availability.class);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
