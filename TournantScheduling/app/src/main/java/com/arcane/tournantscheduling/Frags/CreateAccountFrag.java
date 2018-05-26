package com.arcane.tournantscheduling.Frags;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
    Button buttonNext;
    public static CreateAccountFrag newInstance() {
        return new CreateAccountFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_create_account, container, false);
        setupUi(root);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleNext();
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
        if (DataManager.stringValidate(phone.getText().toString()) != null){
            String phoneNumber = phone.getText().toString();
            if(phoneNumber.contains("-") || phoneNumber.contains("(") || phoneNumber.contains(")")){
                phoneNumber.replace("-","");
                phoneNumber.replace("(","");
                phoneNumber.replace(")","");

            }
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
}
