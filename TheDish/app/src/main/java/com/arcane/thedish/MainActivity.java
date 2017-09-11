package com.arcane.thedish;

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
import android.view.ViewGroup;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements MainBoardRecyclerAdapter.OnItemSelected, UsersRecyclerAdapter.OnPlayerSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        Explode explode = new Explode();
        explode.excludeTarget(android.R.id.statusBarBackground,true);
        explode.excludeTarget(android.R.id.navigationBarBackground,true);
        getWindow().setExitTransition(explode);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getPackageName());
        CustomPagerAdapter mPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), this);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(mPagerAdapter);
        ViewGroup mRoot = (ViewGroup) findViewById(R.id.container_a);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mFireUser = mAuth.getCurrentUser();
      //  Log.i("auth", mAuth.toString());
        MainBoardFrag frag = MainBoardFrag.newInstance();
        //getSupportFragmentManager().beginTransaction().replace(R.id.container,frag,frag.MaindBoard_TAG).commit();
        if(mFireUser == null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainboard_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add_post_setting){
            startActivity(new Intent(this,PostActivity.class));
        }
        if(item.getItemId() == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        return true;
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

    @Override
    public void onPlayerSelected(DishUser dishUser) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


//    @Override
//    public void onPlayerSelected(DishUser dishUser) {
//        Intent intent = new Intent(this,ProfilePageActivity.class);
//        intent.putExtra(ProfilePageFrag.PLAYER_EXTRA, dishUser);
//        startActivity(intent);
//    }
}
