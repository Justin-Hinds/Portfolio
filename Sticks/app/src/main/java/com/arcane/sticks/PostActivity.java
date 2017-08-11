package com.arcane.sticks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        PostFrag frag = PostFrag.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,frag,frag.POST_TAG).commit();
    }
}
