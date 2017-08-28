package com.arcane.sticks.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.arcane.sticks.adapters.MainBoardRecyclerAdapter;
import com.arcane.sticks.frags.MainBoardFrag;
import com.arcane.sticks.frags.ProfilePageFrag;
import com.arcane.sticks.models.Player;
import com.arcane.sticks.adapters.ProfileRecyclerAdapter;
import com.arcane.sticks.R;
import com.arcane.sticks.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfilePageActivity extends AppCompatActivity implements ProfileRecyclerAdapter.AddFellowPlayerInterface, MainBoardRecyclerAdapter.OnItemSelected {
private Player player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        ProfilePageFrag frag = ProfilePageFrag.newInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        if(intent.hasExtra(ProfilePageFrag.PLAYER_EXTRA)){
           // Log.i("INTENT", intent.getSerializableExtra(ProfilePageFrag.PLAYER_EXTRA).toString());
            player = (Player) intent.getSerializableExtra(ProfilePageFrag.PLAYER_EXTRA);
            frag.setPlayer(player);
        }else {
            Log.i("INTENT FAIL PROFILE", "Post extra not found");
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.container,frag).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.edit_profile){
            Intent intent = new Intent(this, ProfileEditActivity.class);
            intent.putExtra(ProfilePageFrag.PLAYER_EXTRA,player);
            startActivity(intent);
        }

        return true;
    }

    @Override
    public void addPlayer() {
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference friendsRef = database.getReference("Users").child(user).child("fellowPlayers");
        if(!Objects.equals(player.getId(), user)){

            Map<String, Object> fellowPlayers = new HashMap<>();
            fellowPlayers.put(player.getId(), true);
            friendsRef.updateChildren(fellowPlayers);
        }
    }

    @Override
    public void messagePlayer() {
        Intent intent = new Intent(this,MessageViewActivity.class);
        intent.putExtra(ProfilePageFrag.PLAYER_EXTRA,player);
        startActivity(intent);
    }

    @Override
    public void onCommentsClicked(Post post) {
        ActivityOptions activityOptions =  ActivityOptions.makeSceneTransitionAnimation(this);
        Intent intent = new Intent(this, CommentsActivity.class);
        Log.d("COMMENTS CLICKED: ", post.getId());
        intent.putExtra(MainBoardFrag.POST_EXTRA,post);
        startActivity(intent, activityOptions.toBundle());
    }

    @Override
    public void onDownClicked(Post post) {

    }

    @Override
    public void onUpClicked(Post post) {

    }

    @Override
    public void onHyperlinkClicked() {

    }
}
