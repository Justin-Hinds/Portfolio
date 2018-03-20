package com.arcane.tournantscheduling;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.arcane.tournantscheduling.Models.DataManager;
import com.arcane.tournantscheduling.Models.Restaurant;
import com.google.firebase.auth.FirebaseAuth;


public class CreateAccountFrag extends Fragment {
    private DataManager dataMan;
    private FirebaseAuth mAuth;
    Spinner state;
    EditText restaurantName;
    EditText address;
    EditText city;
    EditText zip;
    EditText phone;
    Button buttonNext;
    Restaurant restaurant;
    public static CreateAccountFrag newInstance() {
        return new CreateAccountFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_create_account, container, false);
        setupUi(root);
        dataMan = new DataManager(getActivity());
        mAuth = FirebaseAuth.getInstance();
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleNext(root);
            }
        });

        return root;
    }
    private void setupUi(View view){
        restaurantName = view.findViewById(R.id.editText_restaurant_name);
        address = view.findViewById(R.id.editText_address);
        city = view.findViewById(R.id.editText_city);
        zip = view.findViewById(R.id.editText_zip);
        phone = view.findViewById(R.id.editText_phone);
        buttonNext = view.findViewById(R.id.button_next);
        state = view.findViewById(R.id.spinner_state);
    }

    private void handleNext(View view){
        //TODO: Error Handling/Int validation
        String restaurantNameText = DataManager.stringValidate(restaurantName.getText().toString());
        String addressText = DataManager.stringValidate(address.getText().toString());
        String cityText = DataManager.stringValidate(city.getText().toString());
        int zipText = Integer.getInteger(DataManager.stringValidate(zip.getText().toString()));
        int phoneText = Integer.getInteger(DataManager.stringValidate(phone.getText().toString()));
        String stateText = DataManager.stringValidate(restaurantName.getText().toString());

        Bundle bundle = new Bundle();
        bundle.putParcelable("restaurant",);


        CreateAccountSecondFrag frag = CreateAccountSecondFrag.newInstance();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.login_view,frag).commit();
    }
}
