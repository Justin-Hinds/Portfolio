package com.arcane.sticks.Activities;

import android.content.Intent;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;

import com.arcane.sticks.Frags.CommentFrag;
import com.arcane.sticks.Frags.MainBoardFrag;
import com.arcane.sticks.Models.Post;
import com.arcane.sticks.R;

public class CommentsActivity extends AppCompatActivity {
    Post post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        CommentFrag frag = CommentFrag.newInstance();
        getWindow().setAllowEnterTransitionOverlap(false);
        Slide slide = new Slide(Gravity.BOTTOM);
        //slide.setDuration(1000);
        Intent intent = getIntent();
        Post post;
        if(intent.hasExtra(MainBoardFrag.POST_EXTRA)){
            //Log.i("INTENT", intent.getSerializableExtra(MainBoardFrag.POST_EXTRA).toString());
            post = (Post) intent.getSerializableExtra(MainBoardFrag.POST_EXTRA);
            frag.setPost(post);
        }else {
            Log.i("INTENT FAIL COMMENTS", "Post extra not found");
        }
        slide.setInterpolator(new FastOutLinearInInterpolator());
        slide.excludeTarget(android.R.id.statusBarBackground,true);
        slide.excludeTarget(android.R.id.navigationBarBackground,true);
        getWindow().setEnterTransition(slide);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag).commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
