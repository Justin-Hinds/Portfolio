package com.arcane.tournantscheduling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginFrag loginFrag = LoginFrag.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.login_view,loginFrag).commit();
    }
}
