package com.arcane.thedish;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


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
    }
    public void setOnInteraction(final OnItemSelected listener){
        mListener = listener;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView mTextView;
        final TextView mPlayerName;
        final TextView time;
        final ImageButton commentsButton;
        final ImageButton upButton;
        final ImageButton downButton;
        final OnItemSelected mListener;
        final ImageView imageView;
        final ImageView profileImage;

        ArrayList<Post> mDataset = new ArrayList<>();

        public ViewHolder(View itemView, OnItemSelected listener, ArrayList<Post> posts) {
            super(itemView);
            itemView.setOnClickListener(this);
            mDataset = posts;
            time = itemView.findViewById(R.id.time_text);
            imageView = itemView.findViewById(R.id.imageView);
            profileImage =  itemView.findViewById(R.id.profile_icon);
            mTextView = itemView.findViewById(R.id.post_textview);
            mPlayerName = itemView.findViewById(R.id.player_name);
            commentsButton =  itemView.findViewById(R.id.comments_button);
            upButton =  itemView.findViewById(R.id.up_button);
            downButton =  itemView.findViewById(R.id.down_button);
            mListener = listener;
            commentsButton.animate();
//            Log.d(TAG, "Post: " + mDataset);
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
                     final DatabaseReference myRef = database.getReference("Posts").child(post.getId()).child("ups");
                    final DatabaseReference myUserPostRef = database.getReference("User Posts").child(post.getUser()).child(post.getId()).child("ups");
                    final DatabaseReference postUpRef = database.getReference("Posts").child(post.getId()).child("upCount");
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
//                            Log.d("SNAP", dataSnapshot.toString());
                             num ++;
                            ref.setValue(num);
                            postUpRef.setValue(num);
                            Log.d("ups: ", num + "");
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
                    final DatabaseReference myRef = database.getReference("Posts").child(post.getId()).child("downs");
                    final DatabaseReference postDownRef = database.getReference("Posts").child(post.getId()).child("upCount");
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
//                            Log.d("SNAP", dataSnapshot.toString());
                            num ++;
                            ref.setValue(num);
                            postDownRef.setValue(num);
                            Log.d("downs: ", num + "");
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
        Log.i("DATA ", mDataset.size() + "");
        DatabaseReference userRef = database.getReference("Users").child(mDataset.get(position).getUser());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("SNAPSHOT: ", dataSnapshot.toString());
                DishUser dishUser = dataSnapshot.getValue(DishUser.class);
                assert dishUser != null;
                holder.mPlayerName.setText(dishUser.getName());
                Picasso.with(mContext)
                        .load(dishUser.getProfilePicURL())
                        .transform(new CropCircleTransformation())
                        .into(holder.profileImage);
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    //Log.d(TAG, "Value is: "  + childSnapshot.getValue(Post.class));

                    // Log.d(TAG, "Time is: "  + post.time);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Post post = mDataset.get(position);
        Date today = new Date(post.getTime());
        long oneDay = 86400000;
        if((post.getTime() - System.currentTimeMillis())/oneDay >= 1 ){
        DateFormat DATE_FORMAT =  SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        String date = DATE_FORMAT.format(today);
            holder.time.setText(date);

        }else {
            DateFormat DATE_FORMAT = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);
            String date = DATE_FORMAT.format(today);
            holder.time.setText(date);

        }




        holder.mTextView.setText(post.toString());
        Linkify.addLinks(holder.mTextView, Linkify.WEB_URLS);

        if(mDataset.get(position).getImgURL() != null){
            Picasso.with(mContext).load(mDataset.get(position).imgURL).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
