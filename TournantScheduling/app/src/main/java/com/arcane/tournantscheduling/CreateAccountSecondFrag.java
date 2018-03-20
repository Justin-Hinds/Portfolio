package com.arcane.tournantscheduling;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccountSecondFrag extends Fragment {
    Spinner state;
    EditText managerName;
    EditText address;
    EditText city;
    EditText zip;
    EditText phone;
    EditText email;
    EditText password;
    EditText confirmPassword;
    Button createAccount;

    public static CreateAccountSecondFrag newInstance() {
        return new CreateAccountSecondFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_account_second, container, false);
        setupUi(root);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return root;
    }


    private void setupUi(View view){
        managerName = view.findViewById(R.id.editText_restaurant_name);
        address = view.findViewById(R.id.editText_address);
        city = view.findViewById(R.id.editText_city);
        zip = view.findViewById(R.id.editText_zip);
        phone = view.findViewById(R.id.editText_phone);
        createAccount = view.findViewById(R.id.button_create_account);
        email = view.findViewById(R.id.editText_email);
        password = view.findViewById(R.id.editText_password);
        confirmPassword = view.findViewById(R.id.editText_confirm_password);
        state = view.findViewById(R.id.spinner_state);
    }

   private void createAccount(){


   }

}
