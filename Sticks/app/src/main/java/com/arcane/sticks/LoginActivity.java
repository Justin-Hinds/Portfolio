package com.arcane.sticks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginFrag frag = LoginFrag.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,frag,frag.LOGIN_TAG).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LoginFrag.RC_SIGN_IN){
           LoginFrag frag = (LoginFrag) getSupportFragmentManager().findFragmentByTag(LoginFrag.LOGIN_TAG);
            frag.onActivityResult(requestCode,resultCode,data);
        }else {
        super.onActivityResult(requestCode, resultCode, data);

        }
    }
}
