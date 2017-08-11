package com.arcane.sticks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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
        MainBoardFrag frag = MainBoardFrag.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,frag,frag.MaindBoard_TAG).commit();
        if(mFireUser == null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }else {
            //TODO: handle UI components.



        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainboard_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add_post_setting){
            startActivity(new Intent(this,PostActivity.class));
        }
        return true;
    }
}
