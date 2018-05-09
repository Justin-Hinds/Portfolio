package com.arcane.tournantscheduling.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.arcane.tournantscheduling.Frags.LoginFrag;
import com.arcane.tournantscheduling.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginFrag loginFrag = LoginFrag.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.login_view,loginFrag).addToBackStack(LoginFrag.TAG).commit();
    }
}
