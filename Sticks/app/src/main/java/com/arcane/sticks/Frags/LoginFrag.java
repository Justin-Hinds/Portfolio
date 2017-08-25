package com.arcane.sticks.frags;

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
import android.widget.Toast;


import com.arcane.sticks.activities.MainActivity;
import com.arcane.sticks.models.DataManager;
import com.arcane.sticks.models.Player;
import com.arcane.sticks.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class LoginFrag extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    public static LoginFrag newInstance(){return new LoginFrag();}
    public static final String LOGIN_TAG = "LOGIN_TAG";
    private static final String TAG = "LoginFrag.Tag";
    private static final int PICK_IMAGE_REQUEST = 1;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    public static final int RC_SIGN_IN = 9101;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference ref = database.getReference("Users");
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private Uri profileImageUri;
    private ImageView imageView;

    @Override
    public void onStart() {
        super.onStart();
        //FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login_layout,container,false);
        LoginButton fbLoginButton = (LoginButton) root.findViewById(R.id.fb_login_button);
        final SignInButton gSignInButton = (SignInButton) root.findViewById(R.id.g_sign_in_button);
        gSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gSignIn();
            }
        });
        imageView = (ImageView) root.findViewById(R.id.profile_icon);
        imageView.setDrawingCacheEnabled(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });
        fbLoginButton.setFragment(this);
        Button emailSign = (Button) root.findViewById(R.id.login);
        Button emailCreate = (Button) root.findViewById(R.id.create_account);
        final EditText emailText = (EditText) root.findViewById(R.id.editText_email);
        final EditText passwordText = (EditText) root.findViewById(R.id.editText_password);
//        final String email = emailText.getText().toString();
//       final String password = passwordText.getText().toString();


         emailSign.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 if(DataManager.stringValidate(emailText.getText().toString()) != null &&
                         DataManager.stringValidate(passwordText.getText().toString()) != null){
                 final String email = emailText.getText().toString();
                 final String password = passwordText.getText().toString();
                 handlEmailSignIn(email,password);
                 }else {
                     Toast.makeText(getContext(),"Please enter valid Email and Passwored",Toast.LENGTH_LONG).show();
                 }
             }
         });
        emailCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DataManager.stringValidate(emailText.getText().toString()) != null &&
                        DataManager.stringValidate(passwordText.getText().toString()) != null){
                final String email = emailText.getText().toString();
                final String password = passwordText.getText().toString();
                handleEmailCreateAccount(email,password);
                }else {
                    Toast.makeText(getContext(),"Please enter valid Email and Passwored",Toast.LENGTH_LONG).show();

                }
            }
        });

        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });

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
        super.onActivityResult(requestCode,resultCode,data);
        Log.d("INT REQUEST CODE", requestCode + "");
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else{
        callbackManager.onActivityResult(requestCode,resultCode,data);
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void handleEmailCreateAccount(String email, String password){



        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            addPlayer(user);
                            startActivity(new Intent(getContext(),MainActivity.class));
                        } else {
                            // If sign in fails, display a postText to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed. Please try again",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    private void handlEmailSignIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            addPlayer(user);
                            startActivity(new Intent(getContext(),MainActivity.class));
                        } else {
                            // If sign in fails, display a postText to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener( getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            addPlayer(user);
                            startActivity(new Intent(getContext(),MainActivity.class));

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a postText to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
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
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getIdToken());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            addPlayer(user);
                            startActivity(new Intent(getContext(),MainActivity.class));
                            //updateUI(user);
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
    private void addPlayer(FirebaseUser user){
        String id = user.getUid();

        final Player player = new Player();
        if(user.getDisplayName() != null){
        player.setName(user.getDisplayName());
        }else {
            player.setName("N00B");
        }
        player.setId(id);

        //TODO: retrieve user data if user exists already indstead of rewriting it
        imageView.buildDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap uploadBitmap = imageView.getDrawingCache();
        //compresses bitmap to png
        uploadBitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        //writes to a byte array
        byte[] imgData = baos.toByteArray();
        //path for image in firebase
        String path = "Profile_Pics/" + player.getId() +"/" + UUID.randomUUID() + ".png";
        StorageReference profileImageRef = storage.getReference(path);
        UploadTask uploadTask = profileImageRef.putBytes(imgData);
        uploadTask.addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profileImageUri = taskSnapshot.getDownloadUrl();
                assert profileImageUri != null;
                player.setProfilePicURL(profileImageUri.toString());

            }
        });
        ref.child(id).setValue(player);
                getActivity().finish();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void selectPhoto(){
        Intent intent = new Intent();
// set type to image so only images are displayed
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
}
