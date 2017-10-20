package com.arcane.thedish.Frags;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.arcane.thedish.Activities.CreateEmailActivity;
import com.arcane.thedish.Adapters.UsersRecyclerAdapter;
import com.arcane.thedish.Models.DishUser;
import com.arcane.thedish.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("unchecked")
public class UsersFrag extends Fragment implements SearchView.OnQueryTextListener {
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference("Users");
    private UsersRecyclerAdapter mAdapter;
    private DishUser mUser;
    private ArrayList<DishUser> myDataset = new ArrayList();
    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            myDataset.clear();
            //Log.i("USERS: ", dataSnapshot.getValue().toString());
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                //Log.d("USERS ", "Name is: "  + childSnapshot.getValue(Player.class).getName());
                DishUser dishUser = childSnapshot.getValue(DishUser.class);
                //noinspection unchecked
                if (dishUser.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    myDataset.add(0,dishUser);
                    mUser = dishUser;


                }else {
                    myDataset.add(dishUser);

                }


            }
            if(mUser != null){
                if(mUser.getRequests().size() > 0){
                    Log.d("Requests: ", mUser.getRequests().toString());
                    friendRequestAlert();
                }
            }

            //noinspection unchecked
            mAdapter.update(myDataset);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private SearchView searchView;
    private UsersRecyclerAdapter.OnPlayerSelectedListener mListener;

    public static UsersFrag newInstance() {
        return new UsersFrag();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.user_frag_layout, container, false);

        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        searchView = root.findViewById(R.id.searchview);
        searchView.setElevation(4);
        searchView.setBackgroundColor(Color.WHITE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(getContext(), CreateEmailActivity.class)));
        searchView.setQueryHint(getString(R.string.search_staff));
        searchView.setFocusable(false);
        searchView.setIconified(true);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        myDataset = new ArrayList();
        RecyclerView mRecyclerView =  root.findViewById(R.id.rec_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new UsersRecyclerAdapter(myDataset, getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard();
                return false;
            }
        });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

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
        for (DishUser user : myDataset) {
            String name = user.getName().toLowerCase();

            if (name.contains(qString)) {
                newList.add(user);
            }
        }
        mAdapter.update(newList);
        return false;
    }
    private void hideKeyboard(){
        InputMethodManager inputMan = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(!searchView.hasFocus()){
        inputMan.hideSoftInputFromInputMethod(getActivity().getCurrentFocus().getWindowToken(),0);
        }
    }

    private void friendRequestAlert(){

        StringBuilder stringBuilder = new StringBuilder();
        for(DishUser user : myDataset){
            if (mUser.getRequests().containsKey(user.getId())){
                stringBuilder.append(user.getName()).append(", ");
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("You have requests from.")
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for(DishUser user : myDataset){
                            if(mUser.getRequests().containsKey(user.getId())){
                                DatabaseReference requestRef2 = database.getReference("Users").child(user.getId()).child("requests").child(mUser.getId());
                                DatabaseReference requestRef = database.getReference("Users").child(mUser.getId()).child("requests").child(user.getId());
                                DatabaseReference ref = database.getReference("Users").child(mUser.getId()).child("friends");
                                Map<String, Object> fellowUsers = new HashMap<>();
                                fellowUsers.put(user.getId(), true);
                                ref.updateChildren(fellowUsers);
                                requestRef.removeValue();
                                requestRef2.removeValue();
                            }
                        }
                        dialogInterface.dismiss();
                    }
                })

                .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for(DishUser user : myDataset){
                            if(mUser.getRequests().containsKey(user.getId())){
                                DatabaseReference requestRef = database.getReference("Users").child(mUser.getId()).child("requests").child(user.getId());
                                DatabaseReference ref = database.getReference("Users").child(user.getId()).child("friends");
                                ref.child(mUser.getId()).removeValue();
                                requestRef.removeValue();
                            }
                        }
                        dialogInterface.dismiss();
                    }
                })
                .setNeutralButton("Ignore", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setMessage(stringBuilder.toString());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        String qString = newText.toLowerCase();
        ArrayList<DishUser> newList = new ArrayList();

        for (DishUser user : myDataset) {
            String name = user.getName().toLowerCase();

            if (name.contains(qString)) {
                newList.add(user);
            }
        }
        for (DishUser user : newList){
        Log.d("NEW LIST", user.getName());

        }

        mAdapter.update(newList);
        return false;
    }
}
