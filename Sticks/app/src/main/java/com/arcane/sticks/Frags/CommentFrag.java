package com.arcane.sticks.Frags;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.arcane.sticks.Adapters.CommentsRecyclerAdapter;
import com.arcane.sticks.Models.Post;
import com.arcane.sticks.Models.PostComment;
import com.arcane.sticks.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CommentFrag extends Fragment {


    public static CommentFrag newInstance(){
        return new CommentFrag();
    }
    ImageButton sendButton, pickPhoto;
    EditText commentText;
    Post mPost;

    public static final String TAG = ".CommentsFrag: ";

    private RecyclerView mRecyclerView;
    private CommentsRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList myDataset = new ArrayList();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Comments");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.comments_frag,container,false);
        sendButton = (ImageButton) root.findViewById(R.id.send_button);
        pickPhoto = (ImageButton) root.findViewById(R.id.camera_button);
        commentText = (EditText) root.findViewById(R.id.comment_text);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.rec_view);
        myDataset = new ArrayList();


       // mDataManager = new DataManager(getContext());
//        mRecyclerView = (RecyclerView) root.findViewById(R.id.rec_card_view);
//        if(mDataManager.readSavedData() != null){
//            myDataset = mDataManager.readSavedData();
//        }else{
//            myDataset = new ArrayList();
//        }

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CommentsRecyclerAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();


        DatabaseReference postComRef = database.getReference("Post Comments").child(mPost.getId());
        postComRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String commentID = dataSnapshot.getKey();
                DatabaseReference commentsRef = myRef.child(commentID);
                commentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

               // Log.d("STRING: ", dataSnapshot.getValue(PostComment.class).getText());
                        PostComment postComment = dataSnapshot.getValue(PostComment.class);
                        myDataset.add(postComment);
                        mAdapter.update(myDataset);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                myDataset.clear();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                  //  Log.d(TAG, "Value is: "  + childSnapshot.getValue(PostComment.class));
                    PostComment postComment = childSnapshot.getValue(PostComment.class);
                    //Log.d(TAG, "Time is: "  + postComment.getTime());
                    //myDataset.add(postComment);

                }
               // Log.d("DATASET", myDataset.toString());
               // mAdapter.update(myDataset);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //sendFunction
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String postID = mPost.getId();
               // Log.i("OnClick: " , mPost.getId());
                String commentString = commentText.getText().toString();
                PostComment postComment = new PostComment();
                postComment.setPostID(postID);
                postComment.setTime(System.currentTimeMillis());
                postComment.setText(commentString);
                postComment.setSender(user);
                String commentId = myRef.push().getKey();
                Map<String,Object> commentValues = postComment.toMap();
                Map<String, Object> commentKeys = new HashMap<>();
                commentKeys.put(commentId,true);
                Map<String,Object> childUpdates = new HashMap<>();
                childUpdates.put("/Comments/" + commentId, commentValues);
                DatabaseReference postCommentsRef = database.getReference().child("Post Comments").child(postID);
                postCommentsRef.updateChildren(commentKeys);
                database.getReference().updateChildren(childUpdates);
                getActivity().finishAfterTransition();
            }
        });


        return root;
    }
    public void setPost(Post post){
        mPost = post;
        //Log.i("SET POST: " , post.toString());
    }

}
