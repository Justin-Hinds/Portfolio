package com.arcane.sticks.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.arcane.sticks.Frags.PlayersFrag;
import com.arcane.sticks.Frags.PostFrag;
import com.arcane.sticks.R;

public class PlayersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        PlayersFrag frag = PlayersFrag.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,frag).commit();
    }
}
