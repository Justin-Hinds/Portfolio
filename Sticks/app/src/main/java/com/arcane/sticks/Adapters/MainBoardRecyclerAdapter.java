package com.arcane.sticks.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcane.sticks.models.Player;
import com.arcane.sticks.models.Post;
import com.arcane.sticks.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainBoardRecyclerAdapter extends RecyclerView.Adapter<MainBoardRecyclerAdapter.ViewHolder> {
    private ArrayList<Post> mDataset;
    private static final String TAG = "MaindBoardRecycler:";
    private OnItemSelected mListener;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference();
    private final Context mContext;

    public interface OnItemSelected {
        void onCommentsClicked(Post post);
        void onDownClicked(Post post);
        void onUpClicked(Post post);
        void onHyperlinkClicked();
    }
    public void setOnInteraction(final OnItemSelected listener){
        mListener = listener;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView mTextView;
        final TextView mPlayerName;
        final ImageButton commentsButton;
        final ImageButton upButton;
        final ImageButton downButton;
        final OnItemSelected mListener;
        final ImageView imageView;
        ArrayList<Post> mDataset = new ArrayList<>();

        public ViewHolder(View itemView, OnItemSelected listener, ArrayList<Post> posts) {
            super(itemView);
            itemView.setOnClickListener(this);
            mDataset = posts;
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            mTextView = (TextView) itemView.findViewById(R.id.post_textview);
            mPlayerName = (TextView) itemView.findViewById(R.id.player_name);
            commentsButton = (ImageButton) itemView.findViewById(R.id.comments_button);
            upButton = (ImageButton) itemView.findViewById(R.id.up_button);
            downButton = (ImageButton) itemView.findViewById(R.id.down_button);
            mListener = listener;
            commentsButton.animate();
            Log.d(TAG, "Post: " + mDataset);
            commentsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d(TAG, "Post: " + mDataset.get(getAdapterPosition()).getId());
                    mListener.onCommentsClicked(mDataset.get(getAdapterPosition()));
                }
            });
            upButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Post post = mDataset.get(getAdapterPosition());
                     final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference ref = database.getReference("User Posts").child(post.getUser()).child(post.getId()).child("upCount");
                     final DatabaseReference myRef = database.getReference("Post").child(post.getId()).child("ups");
                    final DatabaseReference myUserPostRef = database.getReference("User Posts").child(post.getUser()).child(post.getId()).child("ups");
                    mListener.onUpClicked(post);
                    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String upId = user;
                    Map<String, Object> upKeys = new HashMap<>();
                    upKeys.put(upId,true);
                    myRef.updateChildren(upKeys);
                    myUserPostRef.updateChildren(upKeys);
                    myUserPostRef.addChildEventListener(new ChildEventListener() {
                        int num = 0;
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Log.d("SNAP", dataSnapshot.toString());
                             num ++;
                            ref.setValue(num);
                            Log.d("NUM: ", num + "");
//
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

                }
            });
            downButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onDownClicked(mDataset.get(getAdapterPosition()));
                    Post post = mDataset.get(getAdapterPosition());
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference ref = database.getReference("User Posts").child(post.getUser()).child(post.getId()).child("downCount");
                    final DatabaseReference myRef = database.getReference("Post").child(post.getId()).child("downs");
                    final DatabaseReference myUserPostRef = database.getReference("User Posts").child(post.getUser()).child(post.getId()).child("downs");
                    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String downId = user;
                    Map<String, Object> downKeys = new HashMap<>();
                    downKeys.put(downId,true);
                    myRef.updateChildren(downKeys);
                    myUserPostRef.updateChildren(downKeys);
                    myUserPostRef.addChildEventListener(new ChildEventListener() {
                        int num = 0;
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Log.d("SNAP", dataSnapshot.toString());
                            num ++;
                            ref.setValue(num);
                            Log.d("NUM: ", num + "");
//
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
                }
            });

        }

        @Override
        public void onClick(View v) {
        }
    }
    public MainBoardRecyclerAdapter(ArrayList myData, Context context){
        //noinspection unchecked
        mDataset = myData;
        mContext = context;
    }
    @Override
    public MainBoardRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_cards, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //Log.i(TAG, " CreatViewHolder Dataset: " + mDataset);
        return new ViewHolder(v,mListener,mDataset);
    }
    public void update(ArrayList<Post> posts){
        mDataset = posts;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        DatabaseReference userRef = database.getReference("Users").child(mDataset.get(position).getUser());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("SNAPSHOT: ", dataSnapshot.toString());
                Player player = dataSnapshot.getValue(Player.class);
                assert player != null;
                holder.mPlayerName.setText(player.getName());
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    //Log.d(TAG, "Value is: "  + childSnapshot.getValue(Post.class));

                    // Log.d(TAG, "Time is: "  + post.time);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       // holder.mTextView.setMovementMethod(new LinkMovementMethod());
        holder.mTextView.setText(mDataset.get(position).toString());
        Linkify.addLinks(holder.mTextView,Linkify.WEB_URLS);
        if(mDataset.get(position).getImgURL() != null){
            Picasso.with(mContext).load(mDataset.get(position).imgURL).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
