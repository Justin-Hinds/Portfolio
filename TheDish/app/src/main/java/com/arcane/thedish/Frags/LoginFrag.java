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

import com.arcane.thedish.Activities.CreateEmailActivity;
import com.arcane.thedish.Activities.MainActivity;
import com.arcane.thedish.Models.DataManager;
import com.arcane.thedish.Models.DishUser;
import com.arcane.thedish.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class LoginFrag extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    public static final String LOGIN_TAG = "LOGIN_TAG";
    public static final int RC_SIGN_IN = 9101;
    private static final String TAG = "LoginFrag.Tag";
    private static final int PICK_IMAGE_REQUEST = 1;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference ref = database.getReference("Users");
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private ImageView imageView;
    private DishUser currentDishUser;
    private ProgressBar progressBar;
    private Uri imageUri;
    private DataManager datman;
    private final ValueEventListener valueEvent = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                DishUser dishUser = child.getValue(DishUser.class);
                Log.d("SNAP: ", child.getValue().toString());
                String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (user.equals(dishUser != null ? dishUser.getId() : null)) {
                    currentDishUser = dishUser;
                    if (currentDishUser.getId().equals(user)) {
                        startActivity(new Intent(getContext(), MainActivity.class));
                        getActivity().finish();
                        return;
                    }
                }
            }
          //  addPlayer(mAuth.getCurrentUser());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public static LoginFrag newInstance() {
        return new LoginFrag();
    }

    @Override
    public void onStart() {
        super.onStart();
        //FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login_layout, container, false);
        final SignInButton gSignInButton = root.findViewById(R.id.g_sign_in_button);
        gSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gSignIn();
            }
        });
        imageView =  root.findViewById(R.id.profile_icon);

        datman = new DataManager(getActivity());
        Button emailSign =  root.findViewById(R.id.login);
        Button emailCreate =  root.findViewById(R.id.create_account);
        final EditText emailText =  root.findViewById(R.id.editText_email);
        final EditText passwordText =  root.findViewById(R.id.editText_password);
        progressBar = root.findViewById(R.id.progressBar);


        emailSign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (DataManager.stringValidate(emailText.getText().toString()) != null &&
                        DataManager.stringValidate(passwordText.getText().toString()) != null) {
                    final String email = emailText.getText().toString();
                    final String password = passwordText.getText().toString();
                    handleEmailSignIn(email, password);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Please enter valid Email and Passwored", Toast.LENGTH_LONG).show();
                }
            }
        });
        emailCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
//                if (DataManager.stringValidate(emailText.getText().toString()) != null &&
//                        DataManager.stringValidate(passwordText.getText().toString()) != null) {
//                    final String email = emailText.getText().toString();
//                    final String password = passwordText.getText().toString();

                    Intent intent = new Intent(getContext(),CreateEmailActivity.class);
                    startActivity(intent);
                  //  handleEmailCreateAccount(email, password);
//                } else {
//                    Toast.makeText(getContext(), "Please enter valid Email and Passwored", Toast.LENGTH_LONG).show();

//                }
            }
        });

        mAuth = FirebaseAuth.getInstance();

        //Google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        return root;
    }

    private void gSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        getActivity().startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.d("INT REQUEST CODE", requestCode + "");
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
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

//    private void handleEmailCreateAccount(String email, String password) {
//
//
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            userCheck();
//                        } else {
//                            // If sign in fails, display a postText to the user.
//                            Log.w(TAG, task.getException().getMessage(), task.getException());
//                            if (task.getException().getMessage().equals("The email address is already in use by another account.")) {
//                                Toast.makeText(getContext(), "Email already in use",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                            Toast.makeText(getContext(), "Authentication failed. Please try again",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//
//    }

    private void handleEmailSignIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(getContext(), MainActivity.class));
                            progressBar.setVisibility(View.INVISIBLE);

                        } else {
                            // If sign in fails, display a postText to the user.
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        //Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            assert acct != null;
            String token = acct.getIdToken();
            Log.i(TAG, token);
            firebaseAuthWithGoogle(acct);
            //updateUI(true);
        } else {
            Log.d("SIGNIN", "FAILED");
            progressBar.setVisibility(View.INVISIBLE);
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getIdToken());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
                            //userCheck();
                            datman.userCheck(null,null);

                        } else {
                            // If sign in fails, display a postText to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // ...
                    }
                });
    }

//
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    @Override
    public void onPause() {
        super.onPause();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ref.removeEventListener(valueEvent);
    }
}
