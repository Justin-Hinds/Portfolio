package com.arcane.hinds_justin_android_usersanddata;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.ContentValues.TAG;


public class CreateAccountFrag extends Fragment {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DataManager dataMan;
    private View view;
    public static CreateAccountFrag newInstance(){return new CreateAccountFrag();}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_create_account,container,false);
        dataMan = new DataManager(getContext());
        view = root;

        final EditText emailEditText = root.findViewById(R.id.editText_email);
        final EditText passwordEditText = root.findViewById(R.id.editText_password);

        Button createAccountButton = root.findViewById(R.id.button_create_account);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DataManager.stringValidate(emailEditText.getText().toString()) != null &&
                        DataManager.passwordValidate(passwordEditText.getText().toString(),getContext()) != null) {
                    final String email = emailEditText.getText().toString();
                    final String password = passwordEditText.getText().toString();
                    createAccountEmail(email, password);
                } else {
                    Toast.makeText(getContext(), "Please enter valid Email and Passwored", Toast.LENGTH_LONG).show();

                }
            }
        });

        return root;
    }

    private void createAccountEmail(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            dataMan.userCheck(view);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }
}
