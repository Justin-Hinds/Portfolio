package com.arcane.sticks.Frags;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.arcane.sticks.Models.Player;
import com.arcane.sticks.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileEditFrag extends Fragment {
    public static ProfileEditFrag newInstance(){return new ProfileEditFrag();}
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference playerRef = database.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    EditText playerNameEdit;
    EditText psnIDEdit;
    ImageView imageView;
    EditText gamerTagEdit;
    EditText consoleEdit;
    Player player;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_edit_layout,container,false);
        playerNameEdit = (EditText) root.findViewById(R.id.player_name);
        psnIDEdit = (EditText) root.findViewById(R.id.psn_id);
        gamerTagEdit = (EditText) root.findViewById(R.id.gamer_tag);
        consoleEdit = (EditText) root.findViewById(R.id.preferred_console);
        imageView = (ImageView) root.findViewById(R.id.profile_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        playerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                player = dataSnapshot.getValue(Player.class);
                playerNameEdit.setText(player.getName());
                psnIDEdit.setText(player.getPsnID());
                gamerTagEdit.setText(player.getGamerTag());
                consoleEdit.setText(player.getPreferredConsole());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return root;
    }


    public void save(){
       // Log.i("SAVE: ", "HIT");
        String name = playerNameEdit.getText().toString();
        String console = consoleEdit.getText().toString();
        String psnID = psnIDEdit.getText().toString();
        String gamerTag = gamerTagEdit.getText().toString();
        player.setName(name);
        player.setGamerTag(gamerTag);
        player.setPreferredConsole(console);
        player.setPsnID(psnID);

        playerRef.setValue(player);

        getActivity().finish();
    }
}
