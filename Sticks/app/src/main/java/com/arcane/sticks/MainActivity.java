package com.arcane.sticks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser mFireUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mFireUser = mAuth.getCurrentUser();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        Log.i("auth", mAuth.toString());
        if(mFireUser == null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }else {
            //TODO: handle UI components.


        }
        MainBoardFrag frag = MainBoardFrag.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,frag,frag.MaindBoard_TAG).commit();


    }
}
