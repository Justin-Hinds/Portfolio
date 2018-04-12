package com.arcane.tournantscheduling.Frags;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arcane.tournantscheduling.Activities.HomeScreenActivity;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.Utils.DataManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFrag extends Fragment {

    public static final String TAG = "LOGIN_TAG";
    private FirebaseAuth mAuth;
    private DataManager datman;
//    private OnFragmentInteractionListener mListener;

    public static LoginFrag newInstance() {
        // Required empty public constructor
        return new LoginFrag();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_login, container, false);
        mAuth = FirebaseAuth.getInstance();
        Button emailSignIn = root.findViewById(R.id.button_login);
        Button createAccount = root.findViewById(R.id.button_create_account);
        TextView forgotPassword = root.findViewById(R.id.textView_forgot_password);

        datman = new DataManager();

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.dialog_forgot_password_layout, null);
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(view).show();
                EditText email = view.findViewById(R.id.editText_email);
                Button resetPassword = view.findViewById(R.id.button_reset);
                resetPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(DataManager.stringValidate(email.getText().toString()) != null){
                            mAuth.sendPasswordResetEmail(email.getText().toString())
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
                });
                // alertDialog.setView(view);
            }
        });
        createAccount.setOnClickListener(view -> {
            CreateAccountFrag frag = CreateAccountFrag.newInstance();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.login_view,frag).commit();
        });

        emailSignIn.setOnClickListener(view -> {
            final EditText emailText =  root.findViewById(R.id.editText_email);
            final EditText passwordText =  root.findViewById(R.id.editText_password);
            final String email = emailText.getText().toString();
            final String password = passwordText.getText().toString();

           if(DataManager.loginValidate(email,password,getContext())){
               Log.d("Email", email);
               handleEmailSignIn(email, password);
           }
        });
        return root;
    }





private void handleEmailSignIn(String email, String password){
    mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        startActivity(new Intent(getContext(),HomeScreenActivity.class));
                        getActivity().finish();
                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                       // updateUI(null);
                    }

                }
            });
    }

}
