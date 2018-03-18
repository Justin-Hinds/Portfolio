package com.arcane.hinds_justin_android_usersanddata;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainFrag extends Fragment {
    public static MainFrag newInstance(){return new MainFrag();}
    private TextView name;
    private TextView age;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference ref = database.getReference().child("Users");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_main,container,false);
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        name = root.findViewById(R.id.textview_name);
        age = root.findViewById(R.id.textView_age);
        ref.child(currentUserID).addValueEventListener(valueEventListener);
        ref.child(currentUserID).keepSynced(true);
        return root;
    }



    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d("SNAP", dataSnapshot.toString());
            User user =  dataSnapshot.getValue(User.class);
            name.setText(user != null ? user.getName() : null);
            age.setText(Integer.toString(user != null ? user.getAge() : 0));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
