package com.arcane.thedish.Frags;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arcane.thedish.Models.DataManager;
import com.arcane.thedish.Models.DishUser;
import com.arcane.thedish.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ChefZatoichi on 10/11/17.
 */

public class CreateEmailFrag extends Fragment {

    DataManager dataMan;
    FirebaseAuth mAuth;
    private Uri profileImageUri;
    private Uri imageUri;
    private EditText userName;
    private EditText faveFoodEdit;
    private ImageView imageView;
    private EditText faveDrinkEdit;
    private EditText faveRestaurantEdit;
    View view;
    ProgressBar progressBar;
    public static final int RC_SIGN_IN = 9101;
    public static final String DISHUSER_INTENT = "DISHUSER_INTENT";
    private static final String TAG = "CreateEmailFrag.Tag";
    private static final int PICK_IMAGE_REQUEST = 1;

    public static CreateEmailFrag newInstance(){return new CreateEmailFrag();}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.create_email_frag, container, false);
        view = root;
        dataMan = new DataManager(getActivity());
        mAuth = FirebaseAuth.getInstance();

        imageView =  root.findViewById(R.id.profile_icon);
        Button emailCreate =  root.findViewById(R.id.create_account);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });
        final EditText emailText =  root.findViewById(R.id.editText_email);
        final EditText passwordText =  root.findViewById(R.id.editText_password);
        progressBar = root.findViewById(R.id.progressBar);
        emailCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (DataManager.stringValidate(emailText.getText().toString()) != null &&
                        DataManager.stringValidate(passwordText.getText().toString()) != null) {
                    final String email = emailText.getText().toString();
                    final String password = passwordText.getText().toString();
                      handleEmailCreateAccount(email, password);
                } else {
                    Toast.makeText(getContext(), "Please enter valid Email and Passwored", Toast.LENGTH_LONG).show();

                }
            }
        });
        return root;
    }






    private void handleEmailCreateAccount(String email, String password) {


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            dataMan.userCheck(imageView,view);
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            // If sign in fails, display a postText to the user.
                            Log.w(TAG, task.getException().getMessage(), task.getException());
                            if (task.getException().getMessage().equals("The email address is already in use by another account.")) {
                                Toast.makeText(getContext(), "Email already in use",
                                        Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(getContext(), "Authentication failed. Please try again",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            imageUri = uri;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void selectPhoto() {
        Intent intent = new Intent();
// set type to image so only images are displayed
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
}
