package com.arcane.thedish;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class UsersFrag extends Fragment implements SearchView.OnQueryTextListener {
    private UsersRecyclerAdapter mAdapter;
    private ArrayList<DishUser> myDataset = new ArrayList();
    private SearchView searchView;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference("Users");
    public static UsersFrag newInstance(){return new UsersFrag();}
    private UsersRecyclerAdapter.OnPlayerSelectedListener mListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.user_frag_layout,container,false);

        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        searchView = root.findViewById(R.id.searchview);
        searchView.setElevation(4);
        searchView.setBackgroundColor(Color.WHITE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(getContext(), UsersActivity.class)));
        searchView.setQueryHint(getString(R.string.search_staff));
        searchView.setFocusable(false);
        searchView.setIconified(true);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);

        myDataset = new ArrayList();
        RecyclerView mRecyclerView = (RecyclerView) root.findViewById(R.id.rec_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new UsersRecyclerAdapter(myDataset,getContext());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnPlayerInteraction(mListener);


        myRef.addValueEventListener(valueEventListener);
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getListenerFromContext(context);
    }

    private void getListenerFromContext(Context context) {
        if (context instanceof UsersRecyclerAdapter.OnPlayerSelectedListener) {
            mListener = (UsersRecyclerAdapter.OnPlayerSelectedListener) context;
        } else {
            throw new ClassCastException("Containing activity must " +
                    "implement OnPersonInteractionListener");
        }
    }
    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            myDataset.clear();
            //Log.i("USERS: ", dataSnapshot.getValue().toString());
            for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                //Log.d("USERS ", "Name is: "  + childSnapshot.getValue(Player.class).getName());
                DishUser dishUser = childSnapshot.getValue(DishUser.class);
                // Log.d(TAG, "Time is: "  + post.time);
                //noinspection unchecked
                if(!dishUser.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                myDataset.add(dishUser);

                }



            }
            //noinspection unchecked
            mAdapter.update(myDataset);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myRef.removeEventListener(valueEventListener);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        String qString = query.toLowerCase();
        ArrayList<DishUser> newList = new ArrayList();
        for (DishUser user : myDataset){
            String name = user.getName().toLowerCase();

            if (name.contains(qString)){
                newList.add(user);
            }
        }
        mAdapter.update(newList);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String qString = newText.toLowerCase();
        ArrayList<DishUser> newList = new ArrayList();
        for (DishUser user : myDataset){
           String name = user.getName().toLowerCase();

            if (name.contains(qString)){
                newList.add(user);
            }
        }
        mAdapter.update(newList);
        return false;
    }
}
