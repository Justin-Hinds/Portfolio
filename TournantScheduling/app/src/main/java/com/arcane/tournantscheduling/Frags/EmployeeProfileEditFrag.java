package com.arcane.tournantscheduling.Frags;


import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.arcane.tournantscheduling.Activities.HomeScreenActivity;
import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.Utils.DataManager;
import com.arcane.tournantscheduling.ViewModels.RosterViewModel;


public class EmployeeProfileEditFrag extends Fragment {

    public static final String TAG = "EMPLOYEE_PROFILE_EDIT_FRAG";
    public static EmployeeProfileEditFrag newInstance() {
        return new EmployeeProfileEditFrag();
    }

    Spinner state;
    EditText employeeName;
    EditText address;
    EditText city;
    EditText zip;
    EditText phone;
    EditText email;
    Button buttonUpdate;
    Switch managerSwitch;
    Spinner sectionSpinner;
    ProgressBar progressBar;
    Staff employee;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_employee_profile_edit, container, false);
        RosterViewModel rosterViewModel = ViewModelProviders.of(getActivity()).get(RosterViewModel.class);
        employee = rosterViewModel.getSelectedUser();
        setupUi(root);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle("Update Employee")
                        .setMessage("Are you sure you want to make these updates?" )
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setupEmployeeUpdate();
                                rosterViewModel.updateUserProfile(employee);
                                Toast.makeText(getContext(),employee.getName() + " has been updated.",Toast.LENGTH_SHORT).show();
                                getActivity().getSupportFragmentManager().popBackStack();
                                dialog.dismiss();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        return root;
    }

    private void setupUi(View view){
        email = view.findViewById(R.id.editText_email);
        state = view.findViewById(R.id.spinner_state);
        employeeName = view.findViewById(R.id.editText_employee_name);
        address = view.findViewById(R.id.editText_address);
        city = view.findViewById(R.id.editText_city);
        zip = view.findViewById(R.id.editText_zip);
        phone = view.findViewById(R.id.editText_phone);
        buttonUpdate = view.findViewById(R.id.button_update);
        sectionSpinner = view.findViewById(R.id.spinner_section);
        managerSwitch = view.findViewById(R.id.switch_manager);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                getActivity().getResources().getStringArray(R.array.state_list));
        state.setAdapter(spinnerAdapter);

        ArrayAdapter<String> sectionSpinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                getActivity().getResources().getStringArray(R.array.section_strings));
        sectionSpinner.setAdapter(sectionSpinnerAdapter);

        if(employee != null){
            int spinPosition = spinnerAdapter.getPosition(employee.getState());
            email.setText(employee.getEmail());
            employeeName.setText(employee.getName());
            address.setText(employee.getAddress());
            state.setSelection(spinPosition);
            city.setText(employee.getCity());
            zip.setText(String.valueOf(employee.getZip()));
            phone.setText(String.valueOf(employee.getPhone()));
            managerSwitch.setChecked(employee.isManager());
            if(employee.getSection()!=null){
                int sectionPostition = sectionSpinnerAdapter.getPosition(employee.getSection());
                sectionSpinner.setSelection(sectionPostition);
            }
        }
    }

    private void setupEmployeeUpdate(){
        if(DataManager.stringValidate(zip.getText().toString()) != null){
            int zipText = Integer.parseInt(DataManager.stringValidate(zip.getText().toString()));
            employee.setZip(zipText);

        }
        if(DataManager.stringValidate(phone.getText().toString()) != null){
            long phoneText = Long.parseLong(DataManager.stringValidate(phone.getText().toString()));
            employee.setPhone(phoneText);
        }
        String nameString = DataManager.stringValidate(employeeName.getText().toString());
        String emailString = DataManager.stringValidate(email.getText().toString());
        String addressString = DataManager.stringValidate(address.getText().toString());
        String stateString = DataManager.stringValidate(state.getSelectedItem().toString());
        String cityString = DataManager.stringValidate(city.getText().toString());
        String sectionString = DataManager.stringValidate(sectionSpinner.getSelectedItem().toString());
        employee.setName(nameString);
        employee.setEmail(emailString);
        employee.setAddress(addressString);
        employee.setState(stateString);
        employee.setCity(cityString);
        employee.setSection(sectionString);
        employee.setManager(managerSwitch.isChecked());
    }

    @Override
    public void onStart() {
        super.onStart();
        ((HomeScreenActivity) getActivity()).inflateToolbar(R.menu.menu_employee_edit);

    }

    @Override
    public void onPause() {
        super.onPause();
        ((HomeScreenActivity) getActivity()).clearToolbar();
    }
}
