package com.arcane.thedish.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.arcane.thedish.Frags.PostFrag;
import com.arcane.thedish.R;


public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        PostFrag frag = PostFrag.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,frag, PostFrag.POST_TAG).commit();
    }
}
