package com.arcane.sticks.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.arcane.sticks.Frags.MainBoardFrag;
import com.arcane.sticks.Models.CustomPagerAdapter;
import com.arcane.sticks.Models.MainBoardRecyclerAdapter;
import com.arcane.sticks.Models.Player;
import com.arcane.sticks.Models.PlayersRecyclerAdapter;
import com.arcane.sticks.Models.Post;
import com.arcane.sticks.R;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements MainBoardRecyclerAdapter.OnItemSelected, PlayersRecyclerAdapter.OnPlayerSelectedListener{
    FirebaseAuth mAuth;
    FirebaseUser mFireUser;
    CustomPagerAdapter mPagerAdapter;
    MainBoardFrag frag;
    ViewGroup mRoot;
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
        mPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(),this);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(mPagerAdapter);
        mRoot = (ViewGroup) findViewById(R.id.container_a);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        mAuth = FirebaseAuth.getInstance();
        mFireUser = mAuth.getCurrentUser();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        Log.i("auth", mAuth.toString());
         frag = MainBoardFrag.newInstance();
        //getSupportFragmentManager().beginTransaction().replace(R.id.container,frag,frag.MaindBoard_TAG).commit();
        if(mFireUser == null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }else {
            //TODO: handle UI components.



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
        return true;
    }

    @Override
    public void onCommentsClicked(Post post) {
        ActivityOptions activityOptions =  ActivityOptions.makeSceneTransitionAnimation(this);
        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra(MainBoardFrag.POST_EXTRA,post);
        startActivity(intent, activityOptions.toBundle());
    }

    @Override
    public void onDownClicked() {

    }

    @Override
    public void onUpClicked() {

    }

    @Override
    public void onHyperlinkClicked() {

    }

    @Override
    public void onPlayerSelected(Player player) {

    }
}
