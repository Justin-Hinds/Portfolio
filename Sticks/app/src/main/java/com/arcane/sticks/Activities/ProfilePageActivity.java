package com.arcane.sticks.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.arcane.sticks.Frags.MainBoardFrag;
import com.arcane.sticks.Frags.PlayersFrag;
import com.arcane.sticks.Frags.ProfilePageFrag;
import com.arcane.sticks.Models.Player;
import com.arcane.sticks.Models.Post;
import com.arcane.sticks.R;

public class ProfilePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        ProfilePageFrag frag = ProfilePageFrag.newInstance();

        Intent intent = getIntent();
        Player player;
        if(intent.hasExtra(ProfilePageFrag.PLAYER_EXTRA)){
            Log.i("INTENT", intent.getSerializableExtra(ProfilePageFrag.PLAYER_EXTRA).toString());
            player = (Player) intent.getSerializableExtra(ProfilePageFrag.PLAYER_EXTRA);
            frag.setPlayer(player);
        }else {
            Log.i("INTENT FAIL PROFILE", "Post extra not found");
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.container,frag).commit();
    }
}
