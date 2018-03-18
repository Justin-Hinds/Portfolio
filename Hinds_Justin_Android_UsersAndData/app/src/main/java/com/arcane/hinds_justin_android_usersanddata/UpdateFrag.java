package com.arcane.hinds_justin_android_usersanddata;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateFrag extends Fragment {
    public static UpdateFrag newInstance(){return new UpdateFrag();}
    private final String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference ref = database.getReference().child("Users");
    private final DatabaseReference ageRef = ref.child(currentUserID).child("age");
    private final DatabaseReference nameRef = ref.child(currentUserID).child("name");
    private EditText editTextAge;
    private EditText editTextName;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_update,container,false);
        editTextName = root.findViewById(R.id.editText_name);
        editTextAge = root.findViewById(R.id.editText_age);
        ImageButton deleteName = root.findViewById(R.id.button_name_delete);
        ImageButton deleteAge = root.findViewById(R.id.button_age_delete);
        deleteAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ageRef.removeValue();
                editTextAge.setText("");
            }
        });
        deleteName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameRef.removeValue();
                editTextName.setText("");
            }
        });
        ref.child(currentUserID).addValueEventListener(valueEventListener);
        return root;
    }

    public void save(){
        String name = DataManager.stringValidate(editTextName.getText().toString());
        int age = Integer.parseInt(editTextAge.getText().toString());
        User user = new User();
        if(age > 0){
        user.setAge(age);
        }
        if(name != null){
        user.setName(name);
        }
        user.setId(currentUserID);
        ref.child(currentUserID).setValue(user);
        getActivity().finish();
    }
private final ValueEventListener valueEventListener = new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        User user =  dataSnapshot.getValue(User.class);
        editTextName.setText(user != null ? user.getName() : null);
        editTextAge.setText(Integer.toString(user != null ? user.getAge() : 0));
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
};
}
