package com.arcane.tournantscheduling;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.arcane.tournantscheduling.Models.DataManager;
import com.arcane.tournantscheduling.Models.Restaurant;
import com.arcane.tournantscheduling.Models.Staff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccountSecondFrag extends Fragment {
    public static final String TAG = "CREATE_ACCOUNT_SECOND_FRAG";

    private FirebaseAuth mAuth;
    private DataManager dataMan;

    private FirebaseAnalytics mFirebaseAnalytics;

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
    Restaurant restaurant;
    Staff manager;
    public static CreateAccountSecondFrag newInstance() {
        return new CreateAccountSecondFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        dataMan = new DataManager(getActivity());

        final View root = inflater.inflate(R.layout.fragment_create_account_second, container, false);
        setupUi(root);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        Bundle bundle = getArguments();
        if(bundle.getParcelable("restaurant") != null){
        restaurant = bundle.getParcelable("restaurant");
        }


        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpManager();
                if(password.getText().toString().equals(confirmPassword.getText().toString())){
                    final String emailString = email.getText().toString();
                    final String passwordString = password.getText().toString();
                   if(DataManager.loginValidate(emailString,passwordString,getContext())){
                    handleEmailCreateAccount(emailString, passwordString,root);
                   } else {
                       Toast.makeText(getContext(), "Please enter valid Email and Passwored", Toast.LENGTH_LONG).show();
                   }
                }else {
                    Log.d("NO MATCH:", "Passwords do not match");
                    return;
                }

            }
        });

        return root;
    }


    private void setupUi(View view){
        email = view.findViewById(R.id.editText_email);
        password = view.findViewById(R.id.editText_password);
        confirmPassword = view.findViewById(R.id.editText_confirm_password);
        state = view.findViewById(R.id.spinner_state);
        managerName = view.findViewById(R.id.editText_manager_name);
        address = view.findViewById(R.id.editText_address);
        city = view.findViewById(R.id.editText_city);
        zip = view.findViewById(R.id.editText_zip);
        phone = view.findViewById(R.id.editText_phone);
        createAccount = view.findViewById(R.id.button_create_account);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                getActivity().getResources().getStringArray(R.array.state_list));
        state.setAdapter(spinnerAdapter);
    }

    private void setUpManager(){

        String nameText = DataManager.stringValidate(managerName.getText().toString());
        String addressText = DataManager.stringValidate(address.getText().toString());
        String cityText = DataManager.stringValidate(city.getText().toString());
        manager = new Staff();
        if(DataManager.stringValidate(zip.getText().toString()) != null){
        int zipText = Integer.parseInt(DataManager.stringValidate(zip.getText().toString()));
        Log.d("ZIP ", String.valueOf(zipText));
        manager.setZip(zipText);

        }
//        long phoneText = Long.parseLong(DataManager.stringValidate(phone.getText().toString()));
        String stateText = DataManager.stringValidate(state.getSelectedItem().toString());

        manager.setName(nameText);
        manager.setAddress(addressText);
        manager.setCity(cityText);
        //manager.setPhone(phoneText);
        manager.setState(stateText);
        manager.setManager(true);
        manager.setCreated(new Date());
    }
    private void handleEmailCreateAccount(String email, String password, final View view) {

        if(DataManager.validateManager(manager) == null){
            Toast.makeText(getContext(),"MANAGER NULL!!",Toast.LENGTH_SHORT).show();
            return;
        }else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SUCCESS:", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                manager.setId(user.getUid());
                                Log.d("MANAGER ID", manager.getId());
                                dataMan.addRestaurant(restaurant, manager, user);
                                //dataMan.userCheck(view);
                                Intent intent = new Intent();
                                intent.putExtra(HomeScreenActivity.EMPLOYEE_TAG,manager);
                                getContext().startActivity(intent);
                            } else {
                                // If sign in fails, display a postText to the user.
                                if (task.getException().getMessage().equals("The email address is already in use by another account.")) {
                                    Toast.makeText(getContext(), "Email already in use",
                                            Toast.LENGTH_SHORT).show();
                                }
                                //TODO: proper error message handling
                                Toast.makeText(getContext(), task.getException().getMessage().toString(),
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

}
