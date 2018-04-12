package com.arcane.tournantscheduling.Frags;


import android.app.AlertDialog;
import android.arch.lifecycle.ViewModel;
import android.content.DialogInterface;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.Utils.DataManager;
import com.arcane.tournantscheduling.Models.Staff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CreateStaffFrag extends Fragment {
    public static final String TAG = "CREATE_STAFF_FRAG";
    private FirebaseAuth mAuth1;
    private FirebaseAuth mAuth2;
    private DataManager dataMan;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Spinner state;
    EditText employeeName;
    EditText address;
    EditText city;
    EditText zip;
    EditText phone;
    EditText email;
    EditText password;
    EditText confirmPassword;
    Button createAccount;
    ProgressBar progressBar;
    Staff employee;
    Staff manager;
    public static CreateStaffFrag newInstance() {
        return new CreateStaffFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_create_staff, container, false);
        dataMan = new DataManager();
        setupUi(root);
        getUser();
        mAuth1 = FirebaseAuth.getInstance();
        progressBar = root.findViewById(R.id.progressBar);
        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl(getString(R.string.database_url))
                .setApiKey(getString(R.string.web_api_key))
                .setApplicationId(getString(R.string.tournant_id)).build();

        FirebaseApp myApp = FirebaseApp.initializeApp(getActivity().getApplicationContext(),firebaseOptions,
                "AnyAppName");

        mAuth2 = FirebaseAuth.getInstance(myApp);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DataManager.stringValidate(email.getText().toString()) != null){
                    progressBar.setVisibility(View.VISIBLE);
                    final String emailString = email.getText().toString();
                    setUpEmployee();
                    DataManager.hideKeyboard(getActivity());
                    Log.d("EMPLOYEE:", employee.getName());

                    createAccount(emailString);
                }

            }
        });

        return root;
    }

    private String autoGeneratePassword(){
        String nameText = DataManager.stringValidate(employeeName.getText().toString());
        String passCode;
        if(DataManager.stringValidate(phone.getText().toString()) != null && nameText != null) {
            String[] separated = nameText.split(" ");
            Log.d("LENGTH ", separated.length + "");
            if (separated.length > 1) {
                Character s = separated[0].toUpperCase().charAt(0);
                String s2 = separated[1];
                passCode = s + s2;
                String phoneText = DataManager.stringValidate(phone.getText().toString());
                Pattern p = Pattern.compile("(\\d{4})$");
                Matcher m = p.matcher(phoneText);
                if (m.find()) {
                    Log.d("Last four ", m.group(m.groupCount()));
                    passCode = passCode + m.group(m.groupCount());
                    Log.d("PASSCODE ", passCode);
                    return passCode;
                }
            }else {
                Toast.makeText(getContext(),"Add a last name",Toast.LENGTH_SHORT).show();
            }
        }

        return null;
    }


    private void createAccount(String email) {
        String password = autoGeneratePassword();
        mAuth2.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            String ex = task.getException().toString();
                            Toast.makeText(getContext(), "Registration Failed"+ex,
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            //Toast.makeText(getContext(), "Registration successful",
//                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth2.getCurrentUser();

                            employee.setId(mAuth2.getCurrentUser().getUid());
                            dataMan.addEmployee(user,employee,manager);
                            mAuth2.signOut();
                            progressBar.setVisibility(View.GONE);
                            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                                    .setTitle("Registration Successful")
                                    .setMessage("Please note that " + employeeName.getText().toString()+ "'s " + "password is "
                                            + password +". " + "Instruct them to change this immediately." )
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            clearForm();
                                            dialog.dismiss();
                                        }
                                    }).show();
//                            DataManager.hideKeyboard(getActivity());
                        }


                    }
                });
    }
    private void clearForm(){
        email.setText("");
        employeeName.setText("");
        address.setText("");
        city.setText("");
        zip.setText("");
        phone.setText("");
        state.setSelection(0);
    }
    private void setupUi(View view){
        email = view.findViewById(R.id.editText_email);
        password = view.findViewById(R.id.editText_password);
        confirmPassword = view.findViewById(R.id.editText_confirm_password);
        state = view.findViewById(R.id.spinner_state);
        employeeName = view.findViewById(R.id.editText_employee_name);
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
    private void getUser(){
        db.collection("Restaurants").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    db.collection("Restaurants").document(document.getId()).collection("Users")
                            .whereEqualTo("id",mAuth1.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                            if (task2.isSuccessful()) {
                               for( DocumentSnapshot document2 : task2.getResult()){
                                    Log.d(TAG, document2.getId() + " => " + document2.getData());
                                    manager = document2.toObject(Staff.class);
                                    Log.d("NAME", manager.getName());
                               }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task2.getException());
                            }
                        }
                    });
                    //Log.d(TAG, document.getId() + " => " + document.getData());
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }
    private void setUpEmployee(){

        String nameText = DataManager.stringValidate(employeeName.getText().toString());
        String addressText = DataManager.stringValidate(address.getText().toString());
        String cityText = DataManager.stringValidate(city.getText().toString());
        employee = new Staff();
        if(DataManager.stringValidate(zip.getText().toString()) != null){
            int zipText = Integer.parseInt(DataManager.stringValidate(zip.getText().toString()));
            Log.d("ZIP ", String.valueOf(zipText));
            employee.setZip(zipText);

        }
            if(DataManager.stringValidate(phone.getText().toString()) != null){
            long phoneText = Long.parseLong(DataManager.stringValidate(phone.getText().toString()));
            employee.setPhone(phoneText);
            }
        String stateText = DataManager.stringValidate(state.getSelectedItem().toString());

        employee.setName(nameText);
        employee.setAddress(addressText);
        employee.setCity(cityText);
        employee.setState(stateText);
        employee.setManager(false);
        employee.setCreated(new Date());
    }
}
