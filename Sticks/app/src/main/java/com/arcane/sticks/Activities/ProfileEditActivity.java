package com.arcane.sticks.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.arcane.sticks.frags.ProfileEditFrag;
import com.arcane.sticks.R;

public class ProfileEditActivity extends AppCompatActivity {
    private ProfileEditFrag frag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         frag = ProfileEditFrag.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,frag).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_edit_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.save_changes){
            frag.save();
        }
        return true;
    }
}
