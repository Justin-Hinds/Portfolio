package com.arcane.thedish.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.arcane.thedish.Adapters.CustomPagerAdapter;
import com.arcane.thedish.Adapters.MainBoardRecyclerAdapter;
import com.arcane.thedish.Adapters.UsersRecyclerAdapter;
import com.arcane.thedish.Frags.MainBoardFrag;
import com.arcane.thedish.Frags.ProfilePageFrag;
import com.arcane.thedish.Models.DishUser;
import com.arcane.thedish.Models.Post;
import com.arcane.thedish.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements MainBoardRecyclerAdapter.OnItemSelected, UsersRecyclerAdapter.OnPlayerSelectedListener {

    private DishUser dishUser;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseUser mFireUser = mAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        Explode explode = new Explode();
        explode.excludeTarget(android.R.id.statusBarBackground,true);
        explode.excludeTarget(android.R.id.navigationBarBackground,true);
        if(mFireUser == null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        getWindow().setExitTransition(explode);
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
        final DatabaseReference userRef = database.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addValueEventListener(eventListener);

        }

        setContentView(R.layout.activity_main);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getPackageName());
        CustomPagerAdapter mPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), this);
        ViewPager viewPager =  findViewById(R.id.pager);
        viewPager.setAdapter(mPagerAdapter);
//        ViewGroup mRoot = (ViewGroup) findViewById(R.id.container_a);
        TabLayout tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

//        MainBoardFrag frag = MainBoardFrag.newInstance();
        //getSupportFragmentManager().beginTransaction().replace(R.id.container,frag,frag.MaindBoard_TAG).commit();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainboard_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.add_post_setting:
                break;
            case R.id.logout:
                break;
            case R.id.edit_profile:
                break;
            default:
                break;
        }
        if(item.getItemId() == R.id.add_post_setting){
            startActivity(new Intent(this,PostActivity.class));
        }
        if(item.getItemId() == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        if(item.getItemId() == R.id.edit_profile){
            Intent intent = new Intent(this, ProfileEditActivity.class);
            intent.putExtra(ProfilePageFrag.PLAYER_EXTRA, dishUser);
            startActivity(intent);

        }
        return true;
    }
    private final ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            dishUser = dataSnapshot.getValue(DishUser.class);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onPlayerSelected(DishUser dishUser) {
        Intent intent = new Intent(this,ProfilePageActivity.class);
        intent.putExtra(ProfilePageFrag.PLAYER_EXTRA, dishUser);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {

            final DatabaseReference userRef = database.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            userRef.removeEventListener(eventListener);
        }
    }
}
