package com.arcane.tournantscheduling.Frags;

import android.arch.lifecycle.ViewModelProviders;
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
import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.Utils.DataManager;
import com.arcane.tournantscheduling.ViewModels.RosterViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;


public class AvailabilityFrag extends Fragment implements AdapterView.OnItemSelectedListener {
    public static final String TAG = "AVAILABILITY_FRAG";

//    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private  FirebaseAuth mAuth;
    private DataManager dataManager;
    RosterViewModel rosterViewModel;
    private Spinner mondaySpinner;
    private Spinner tuesdaySpinner;
    private Spinner wednesdaySpinner;
    private Spinner thursdaySpinner;
    private Spinner fridaySpinner;
    private Spinner saturdaySpinner;
    private Spinner sundaySpinner;
    Availability availability;
    Staff currentUser;

    public static AvailabilityFrag newInstance(){
        return new AvailabilityFrag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_availability,container,false);
        dataManager = new DataManager(getActivity());
        mAuth = FirebaseAuth.getInstance();
        rosterViewModel = ViewModelProviders.of(getActivity()).get(RosterViewModel.class);
        currentUser = rosterViewModel.getCurrentUser();
        setUpSpinners(root);

        Button changeButton = root.findViewById(R.id.button_change_availability);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAvailability();
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

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
        thursdaySpinner.setOnItemSelectedListener(this);

        fridaySpinner.setAdapter(spinnerAdapter);
        fridaySpinner.setOnItemSelectedListener(this);

        saturdaySpinner.setAdapter(spinnerAdapter);
        saturdaySpinner.setOnItemSelectedListener(this);

        sundaySpinner.setAdapter(spinnerAdapter);
        sundaySpinner.setOnItemSelectedListener(this);

        if(currentUser.getAvailability() != null){
            Log.d("AVAILABILITY", currentUser.getAvailability().getMonday() + "");
            mondaySpinner.setSelection(currentUser.getAvailability().getMonday());
            tuesdaySpinner.setSelection(currentUser.getAvailability().getTuesday());
            wednesdaySpinner.setSelection(currentUser.getAvailability().getWednesday());
            thursdaySpinner.setSelection(currentUser.getAvailability().getThursday());
            fridaySpinner.setSelection(currentUser.getAvailability().getFriday());
            saturdaySpinner.setSelection(currentUser.getAvailability().getSaturday());
            sundaySpinner.setSelection(currentUser.getAvailability().getSunday());

        }else {
            Log.d("USER", "Null");

        }
    }

    private void setAvailability(){

         availability = new Availability();

        availability.setMonday(mondaySpinner.getSelectedItemPosition());
        availability.setTuesday(tuesdaySpinner.getSelectedItemPosition());
        availability.setWednesday(wednesdaySpinner.getSelectedItemPosition());
        availability.setThursday(thursdaySpinner.getSelectedItemPosition());
        availability.setFriday(fridaySpinner.getSelectedItemPosition());
        availability.setSaturday(saturdaySpinner.getSelectedItemPosition());
        availability.setSunday(sundaySpinner.getSelectedItemPosition());
        if(currentUser != null){
        dataManager.updateUserAvailability(currentUser,availability);
        }
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
}
