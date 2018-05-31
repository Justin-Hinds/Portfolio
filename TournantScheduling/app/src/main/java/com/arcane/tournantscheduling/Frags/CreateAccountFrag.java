package com.arcane.tournantscheduling.Frags;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.Utils.DataManager;
import com.arcane.tournantscheduling.Models.Restaurant;

import java.util.Date;


public class  CreateAccountFrag extends Fragment {
    public static final String TAG = "CREATE_ACCOUNT_FRAG";

    Spinner state;
    EditText restaurantName;
    EditText address;
    EditText city;
    EditText zip;
    EditText phone;

    TextView textName;
    TextView textAddress;
    TextView textCity;
    TextView textZip;
    TextView textPhone;

    Button buttonNext;
    public static CreateAccountFrag newInstance() {
        return new CreateAccountFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_create_account, container, false);
        setupUi(root);
        setUpLabels();
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleNext();
            }
        });

        return root;
    }
    private void setupUi(View view){
        restaurantName = view.findViewById(R.id.editText_employee_name);
        address = view.findViewById(R.id.editText_address);
        city = view.findViewById(R.id.editText_city);
        zip = view.findViewById(R.id.editText_zip);
        phone = view.findViewById(R.id.editText_phone);
        buttonNext = view.findViewById(R.id.button_next);
        state = view.findViewById(R.id.spinner_state);

        textName = view.findViewById(R.id.Textview_name);
        textAddress = view.findViewById(R.id.Textview_address);
        textCity = view.findViewById(R.id.textview_city);
        textZip = view.findViewById(R.id.textview_zip);
        textPhone = view.findViewById(R.id.textview_phone);


        textName.setVisibility(View.GONE);
        textAddress.setVisibility(View.GONE);
        textCity.setVisibility(View.GONE);
        textZip.setVisibility(View.GONE);
        textPhone.setVisibility(View.GONE);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                getActivity().getResources().getStringArray(R.array.state_list));
        state.setAdapter(spinnerAdapter);
    }

    private void handleNext(){
        //TODO: Error Handling / Form Validation
        Restaurant restaurant = new Restaurant();
        String restaurantNameText = DataManager.stringValidate(restaurantName.getText().toString());
        String addressText = DataManager.stringValidate(address.getText().toString());
        String cityText = DataManager.stringValidate(city.getText().toString());
        if(DataManager.stringValidate(zip.getText().toString()) != null){
        int zipText = Integer.parseInt(DataManager.stringValidate(zip.getText().toString()));
        restaurant.setZip(zipText);

        }
        Log.d("Phone Number",PhoneNumberUtils.isGlobalPhoneNumber(phone.getText().toString()) +"");
        if (DataManager.stringValidate(phone.getText().toString()) != null){
            String phoneNumber = phone.getText().toString();
            phoneNumber = phoneNumber.replaceAll("[^\\d.]", "");
        long phoneText = Long.parseLong(DataManager.stringValidate(phoneNumber));
        restaurant.setPhone(phoneText);

        }
        String stateText = DataManager.stringValidate(state.getSelectedItem().toString());

        restaurant.setName(restaurantNameText);
        restaurant.setAddress(addressText);
        restaurant.setCity(cityText);
        restaurant.setState(stateText);
        restaurant.setCreated(new Date());

        if(DataManager.validateRestaurant(restaurant) != null){
            Bundle bundle = new Bundle();
            bundle.putParcelable("restaurant",restaurant);


            CreateAccountSecondFrag frag = CreateAccountSecondFrag.newInstance();
            frag.setArguments(bundle);
            getActivity().getSupportFragmentManager()
                    .beginTransaction().replace(R.id.login_view,frag)
                    .addToBackStack(CreateAccountSecondFrag.TAG).commit();
        }else {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                    .setTitle("Invalid Field")
                    .setMessage("Please make sure all fields are filled in and valid.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
            return;
        }

    }

    private void setUpLabels(){
        restaurantName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    textName.setVisibility(View.VISIBLE);
                }else {
                    textName.setVisibility(View.GONE);
                }
            }
        });
        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    textAddress.setVisibility(View.VISIBLE);
                }else {
                    textAddress.setVisibility(View.GONE);
                }
            }
        });
        city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    textCity.setVisibility(View.VISIBLE);
                }else {
                    textCity.setVisibility(View.GONE);
                }
            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    textPhone.setVisibility(View.VISIBLE);
                }else {
                    textPhone.setVisibility(View.GONE);
                }
            }
        });
        zip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    textZip.setVisibility(View.VISIBLE);
                }else {
                    textZip.setVisibility(View.GONE);
                }
            }
        });
    }
}
