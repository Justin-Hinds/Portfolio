package com.arcane.thedish.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.arcane.thedish.Adapters.MainBoardRecyclerAdapter;
import com.arcane.thedish.Adapters.ProfileRecyclerAdapter;
import com.arcane.thedish.Frags.MainBoardFrag;
import com.arcane.thedish.Frags.ProfilePageFrag;
import com.arcane.thedish.Models.DishUser;
import com.arcane.thedish.Models.Post;
import com.arcane.thedish.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfilePageActivity extends AppCompatActivity implements ProfileRecyclerAdapter.AddFellowStaffInterface, MainBoardRecyclerAdapter.OnItemSelected {
private DishUser dishUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        ProfilePageFrag frag = ProfilePageFrag.newInstance();
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        if(intent.hasExtra(ProfilePageFrag.PLAYER_EXTRA)){
           // Log.i("INTENT", intent.getSerializableExtra(ProfilePageFrag.PLAYER_EXTRA).toString());
            dishUser = (DishUser) intent.getSerializableExtra(ProfilePageFrag.PLAYER_EXTRA);
            frag.setPlayer(dishUser);
        }else {
            Log.i("INTENT FAIL PROFILE", "Post extra not found");
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.container,frag).commit();
    }


    @Override
    public void addFriend() {
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference friendsRef = database.getReference("Users").child(user).child("friends");
        if(!Objects.equals(dishUser.getId(), user)){

            Map<String, Object> fellowPlayers = new HashMap<>();
            fellowPlayers.put(dishUser.getId(), true);
            friendsRef.updateChildren(fellowPlayers);
        }
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

}
