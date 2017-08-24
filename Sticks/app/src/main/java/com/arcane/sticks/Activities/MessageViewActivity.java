package com.arcane.sticks.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.arcane.sticks.Frags.MessageViewFrag;
import com.arcane.sticks.Frags.PlayersFrag;
import com.arcane.sticks.Frags.ProfilePageFrag;
import com.arcane.sticks.Models.Player;
import com.arcane.sticks.R;

public class MessageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_view);
        MessageViewFrag frag = MessageViewFrag.newInstance();

        Intent intent = getIntent();
        Player player;
        if(intent.hasExtra(ProfilePageFrag.PLAYER_EXTRA)){
            //Log.i("INTENT", intent.getSerializableExtra(MainBoardFrag.POST_EXTRA).toString());
            player = (Player) intent.getSerializableExtra(ProfilePageFrag.PLAYER_EXTRA);
            frag.setmPlayer(player);
        }else {
            Log.i("INTENT FAIL COMMENTS", "Post extra not found");
        }


        getSupportFragmentManager().beginTransaction().replace(R.id.container,frag).commit();
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
