package com.arcane.tournantscheduling.Frags;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arcane.tournantscheduling.Activities.HomeScreenActivity;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.Utils.DataManager;
import com.arcane.tournantscheduling.Models.Restaurant;
import com.arcane.tournantscheduling.Models.Staff;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
//    EditText phone;
    EditText email;
    EditText password;
    EditText confirmPassword;

    TextView textName;
    TextView textAddress;
    TextView textCity;
    TextView textZip;
//    TextView textPhone;
    TextView textEmail;
    TextView textPassword;
    TextView textConfirmPassword;

    Button createAccount;
    Restaurant restaurant;
    Staff manager;
    ProgressBar progressBar;
    public static CreateAccountSecondFrag newInstance() {
        return new CreateAccountSecondFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//       getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mAuth = FirebaseAuth.getInstance();
        dataMan = new DataManager();
        final View root = inflater.inflate(R.layout.fragment_create_account_second, container, false);
        progressBar = root.findViewById(R.id.progress_bar);
        setupUi(root);
        setUpLabels();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        Bundle bundle = getArguments();
        if(bundle.getParcelable("restaurant") != null){
        restaurant = bundle.getParcelable("restaurant");
        }


        createAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                setUpManager();
                if(password.getText().toString().equals(confirmPassword.getText().toString())){
                    final String emailString = email.getText().toString();
                    final String passwordString = password.getText().toString();
                   if(DataManager.loginValidate(emailString,passwordString,getContext())){
                    handleEmailCreateAccount(emailString, passwordString,root);
                   } else {
                       Toast.makeText(getContext(), "Please enter valid Email and Passwored", Toast.LENGTH_LONG).show();
                       progressBar.setVisibility(View.GONE);
                   }
                }else {
                    Log.d("NO MATCH:", "Passwords do not match");
                    progressBar.setVisibility(View.GONE);
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle("Passwords do not match")
                            .setMessage("Please make sure both passwords are correct")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
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
        managerName = view.findViewById(R.id.editText_employee_name);
        address = view.findViewById(R.id.editText_address);
        city = view.findViewById(R.id.editText_city);
        zip = view.findViewById(R.id.editText_zip);
//        phone = view.findViewById(R.id.editText_phone);
        createAccount = view.findViewById(R.id.button_create_account);

        textName = view.findViewById(R.id.Textview_name);
        textAddress = view.findViewById(R.id.Textview_address);
        textCity = view.findViewById(R.id.textview_city);
        textZip = view.findViewById(R.id.textview_zip);
        textEmail = view.findViewById(R.id.textview_email);
//        textPhone = view.findViewById(R.id.textview_phone);
        textPassword = view.findViewById(R.id.textview_password);
        textConfirmPassword = view.findViewById(R.id.textview_confirm);

        textName.setVisibility(View.GONE);
        textAddress.setVisibility(View.GONE);
        textCity.setVisibility(View.GONE);
        textZip.setVisibility(View.GONE);
        textEmail.setVisibility(View.GONE);
//        textPhone.setVisibility(View.GONE);
        textPassword.setVisibility(View.GONE);
        textConfirmPassword.setVisibility(View.GONE);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                getActivity().getResources().getStringArray(R.array.state_list));
        state.setAdapter(spinnerAdapter);
    }

    private void setUpManager(){

        String nameText = DataManager.stringValidate(managerName.getText().toString());
        String addressText = DataManager.stringValidate(address.getText().toString());
        String cityText = DataManager.stringValidate(city.getText().toString());
        String emailText = DataManager.stringValidate(email.getText().toString());
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
        manager.setEmail(emailText);
        manager.setState(stateText);
        manager.setManager(true);
        manager.setCreated(new Date());
    }
    private void handleEmailCreateAccount(String email, String password, final View view) {

        if(DataManager.validateManager(manager,getContext()) == null){
            //Toast.makeText(getContext(),"MANAGER NULL!!",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SUCCESS:", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            manager.setId(user.getUid());
                            Log.d("MANAGER ID", manager.getId());
                            dataMan.addRestaurant(restaurant, manager, user);
                            //dataMan.userCheck(view);
                            Intent intent = new Intent(getContext(),HomeScreenActivity.class);
                            intent.putExtra(HomeScreenActivity.EMPLOYEE_TAG,manager);
                            progressBar.setVisibility(View.GONE);
                            getContext().startActivity(intent);
                        } else {
                            // If sign in fails, display a postText to the user.
                            if (task.getException().getMessage().equals("The email address is already in use by another account.")) {
                                Toast.makeText(getContext(), "Email already in use",
                                        Toast.LENGTH_SHORT).show();
                            }
                                progressBar.setVisibility(View.GONE);
                            //TODO: proper error message handling
                            Toast.makeText(getContext(), task.getException().getMessage().toString(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    });
        }
    }

    private void setUpLabels(){
        managerName.addTextChangedListener(new TextWatcher() {
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
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    textEmail.setVisibility(View.VISIBLE);
                }else {
                    textEmail.setVisibility(View.GONE);
                }
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    textPassword.setVisibility(View.VISIBLE);
                }else {
                    textPassword.setVisibility(View.GONE);
                }
            }
        });
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    textConfirmPassword.setVisibility(View.VISIBLE);
                }else {
                    textConfirmPassword.setVisibility(View.GONE);
                }
            }
        });
    }
}
