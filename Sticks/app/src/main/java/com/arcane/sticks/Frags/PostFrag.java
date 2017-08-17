package com.arcane.sticks.Frags;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.arcane.sticks.Models.Post;
import com.arcane.sticks.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class PostFrag extends Fragment {
    public static final PostFrag newInstance(){return new PostFrag();}
    public static final String POST_TAG = "POST_TAG";
    public static final String TAG = "PostFrag.Tag";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Posts");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.post_frag_layout, container,false);
        final EditText postMessage = (EditText) root.findViewById(R.id.editText);
        final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference childRef = myRef.push();
        Button sendButton = (Button) root.findViewById(R.id.send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference userPostRef = database.getReference("User Posts").child(user);
                Post post = new Post();
                post.setPostText(postMessage.getText().toString());
                post.setUser(user);
                post.setTime(System.currentTimeMillis());
                //post.setComments(new HashMap<String, Boolean>());
                post.setDowns(new HashMap<String, Boolean>());
                post.setUps(new HashMap<String, Boolean>());
                post.setHyperLink(null);
                post.setImgURL(null);

                String postID = childRef.getKey();
                post.setId(postID);
                Map<String,Object> postValues = post.toMap();
                Map<String,Object> childUpdates = new HashMap<>();
                childUpdates.put("/Posts/" + postID, postValues);
                childUpdates.put("/User Posts/" + user + "/" + postID, postValues);
               // childRef.setValue(post);

                database.getReference().updateChildren(childUpdates);
                getActivity().finish();
                return;
            }
        });
        return root;
    }




}
