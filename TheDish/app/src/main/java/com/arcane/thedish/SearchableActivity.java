package com.arcane.thedish;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by ChefZatoichi on 9/14/17.
 */

public class SearchableActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }



    private void doMySearch(String query){
        Toast.makeText(getBaseContext(),query + " Activity",Toast.LENGTH_LONG).show();
    }
}
