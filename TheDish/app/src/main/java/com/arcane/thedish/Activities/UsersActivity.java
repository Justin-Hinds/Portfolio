package com.arcane.thedish.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.arcane.thedish.Frags.UsersFrag;
import com.arcane.thedish.R;

public class UsersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        UsersFrag frag = UsersFrag.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,frag).commit();
    }
}
