package com.arcane.tournantscheduling.Frags;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.Utils.DataManager;
import com.arcane.tournantscheduling.Utils.NetworkUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class SettingsFragment extends Fragment {
    public static final String TAG = "SETTINGS_FRAG";
    TextView reset;
    AlertDialog alertDialog;
    private FirebaseAuth mAuth;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // addPreferencesFromResource(R.xml.settings_manager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        mAuth = FirebaseAuth.getInstance();
        reset = root.findViewById(R.id.reset_password);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtils.isConnected(getContext())) {
                    View view = getLayoutInflater().inflate(R.layout.dialog_forgot_password_layout, null);
                    alertDialog = new AlertDialog.Builder(getContext()).setView(view).show();
                    EditText email = view.findViewById(R.id.editText_email);
                    Button resetPasswordButton = view.findViewById(R.id.button_reset);
                    resetPasswordButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            resetPassword(email.getText().toString());

                        }
                    });
                }else {
                    showNoConnectionDialog();
                }
                // alertDialog.setView(view);

            }
        });


        return root;
    }


    private void resetPassword(String email){
        if(DataManager.stringValidate(email) != null){
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(),"Email sent.",Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        }
                    });
        }
    }
    private void showNoConnectionDialog(){
        alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("No Connection")
                .setMessage("Please make sure that you are connected to the internet" )
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
