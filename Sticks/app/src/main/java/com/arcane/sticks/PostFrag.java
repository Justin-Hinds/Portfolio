package com.arcane.sticks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class PostFrag extends Fragment {
    public static final PostFrag newInstance(){return new PostFrag();}
    public static final String POST_TAG = "POST_TAG";
    public static final String TAG = "PostFrag.Tag";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("messages");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.post_frag_layout, container,false);
        final EditText postMessage = (EditText) root.findViewById(R.id.editText);
        final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Button sendButton = (Button) root.findViewById(R.id.send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post post = new Post();
                post.setMessage(postMessage.getText().toString());
                post.setUser(user);
                myRef.push().setValue(post);
                //myRef.setValue(postMessage.getText().toString());
                getActivity().finish();
                return;
            }
        });
        return root;
    }




}
