package com.arcane.tournantscheduling.Frags;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcane.tournantscheduling.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeeProfileEditFrag extends Fragment {

    public static final String TAG = "EMPLOYEE_PROFILE_EDIT_FRAG";

    public static EmployeeProfileEditFrag newInstance() {
        return new EmployeeProfileEditFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_employee_profile_edit, container, false);



        return root;
    }

}
