package com.arcane.sticks.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.arcane.sticks.frags.PostFrag;
import com.arcane.sticks.R;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        PostFrag frag = PostFrag.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,frag, PostFrag.POST_TAG).commit();
    }
}
