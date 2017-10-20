package com.arcane.thedish.Adapters;

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

import com.arcane.thedish.Models.DishUser;
import com.arcane.thedish.Models.ImageLoadTask;
import com.arcane.thedish.Models.Post;
import com.arcane.thedish.R;
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
    private static final String TAG = "MaindBoardRecycler:";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference();
    private final Context mContext;
    private ArrayList<Post> mDataset;
    private OnItemSelected mListener;

    public MainBoardRecyclerAdapter(ArrayList myData, Context context) {
        //noinspection unchecked
        mDataset = myData;
        mContext = context;
    }

    public void setOnInteraction(final OnItemSelected listener) {
        mListener = listener;
    }

    @Override
    public MainBoardRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_cards, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //Log.i(TAG, " CreatViewHolder Dataset: " + userArrayList);
        return new ViewHolder(v, mListener, mDataset);
    }

    public void update(ArrayList<Post> posts) {
        mDataset = posts;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Post post = mDataset.get(position);
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = database.getReference("Users").child(post.getUser());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DishUser dishUser = dataSnapshot.getValue(DishUser.class);
                assert dishUser != null;
                holder.mUserName.setText(dishUser.getName());

                Picasso.with(mContext)
                        .load(dishUser.getProfilePicURL())
                        .transform(new CropCircleTransformation())
                        .into(holder.profileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(!post.getUps().containsKey(id) && !post.getDowns().containsKey(id)){
            holder.upButton.setImageResource(R.mipmap.up_vote_icon);
            holder.downButton.setImageResource(R.mipmap.down_vote_icon);

        }
        if (post.getUps().containsKey(id)) {
            holder.upButton.setImageResource(R.mipmap.up_icon_focused);
            holder.downButton.setImageResource(R.mipmap.down_vote_icon);
//            Log.d(TAG, "Post ups: " + post.getUps());

        }
        if (post.getDowns().containsKey(id)) {
            holder.upButton.setImageResource(R.mipmap.up_vote_icon);
            holder.downButton.setImageResource(R.mipmap.down_icon_focused);
//            Log.d(TAG, "Post downs: " + post.getDowns());
        }
        Date today = new Date(post.getTime());
        long oneDay = 86400000;
//        Log.d("TIME DIFF ", System.currentTimeMillis() - post.getTime() + "");
        if ((System.currentTimeMillis() - post.getTime()) / oneDay >= 1) {
            DateFormat DATE_FORMAT = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
            String date = DATE_FORMAT.format(today);
            holder.time.setText(date);

        } else {
            DateFormat DATE_FORMAT = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);
            String date = DATE_FORMAT.format(today);
            holder.time.setText(date);

        }
        //set imageBitmap to null to prevent images leaking to other posts
        holder.imageView.setImageBitmap(null);
        if (mDataset.get(position).getImgURL() != null) {
//            ImageLoadTask task = new ImageLoadTask(holder.imageView);
//            task.execute(post.getImgURL());
//
//            if (task != null){
//                task.cancel(true);
//            }
            Picasso.with(holder.imageView.getContext())
                    .load(post.getImgURL())
                    .into(holder.imageView);

        }
//        else {
//            holder.imageView.setVisibility(View.GONE);
//        }

        holder.mTextView.setText(post.toString());
        Linkify.addLinks(holder.mTextView, Linkify.WEB_URLS);


    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        ImageLoadTask task = new ImageLoadTask(holder.imageView);
//
        task.cancel(true);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnItemSelected {
        void onCommentsClicked(Post post);

        void onDownClicked(Post post);

        void onUpClicked(Post post);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView mTextView;
        final TextView mUserName;
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
            profileImage = itemView.findViewById(R.id.profile_icon);
            mTextView = itemView.findViewById(R.id.post_textview);
            mUserName = itemView.findViewById(R.id.user_name);
            commentsButton = itemView.findViewById(R.id.comments_button);
            upButton = itemView.findViewById(R.id.up_button);
            downButton = itemView.findViewById(R.id.down_button);
            mListener = listener;
            commentsButton.animate();
            commentsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCommentsClicked(mDataset.get(getAdapterPosition()));
                }
            });

//
            upButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Post post = mDataset.get(getAdapterPosition());
                    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String upId = user;
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference ref = database.getReference("User Posts").child(post.getUser()).child(post.getId()).child("upCount");
                    final DatabaseReference myUpsRef = database.getReference("Posts").child(post.getId()).child("ups");
                    final DatabaseReference myUserPostRef = database.getReference("User Posts").child(post.getUser()).child(post.getId()).child("ups");
                    final DatabaseReference postUpRef = database.getReference("Posts").child(post.getId()).child("upCount");
                    final DatabaseReference myDownsRefUsersPost = database.getReference("User Posts").child(post.getUser()).child(post.getId()).child("downs").child(upId);
                    final DatabaseReference myDownsRef = database.getReference("Posts").child(post.getId()).child("downs").child(upId);


                    mListener.onUpClicked(post);
                    Map<String, Object> upKeys = new HashMap<>();
                    upKeys.put(upId, true);
                    myUpsRef.updateChildren(upKeys);
                    myUserPostRef.updateChildren(upKeys);
                    myDownsRef.removeValue();
                    myDownsRefUsersPost.removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            myUserPostRef.addChildEventListener(new ChildEventListener() {
                                int num = 0;

                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                            Log.d("SNAP", dataSnapshot.toString());
                                    num++;
                                    ref.setValue(num);
                                    postUpRef.setValue(num);
//
                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {
                                    Log.d("NUM:", num + "");
                                    num--;
                                    ref.setValue(num);
                                    postUpRef.setValue(num);
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
            });
            downButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onDownClicked(mDataset.get(getAdapterPosition()));
                    Post post = mDataset.get(getAdapterPosition());
                    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String downId = user;

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference ref = database.getReference("User Posts").child(post.getUser()).child(post.getId()).child("downCount");
                    final DatabaseReference myRef = database.getReference("Posts").child(post.getId()).child("downs");
                    final DatabaseReference postDownRef = database.getReference("Posts").child(post.getId()).child("downCount");
                    final DatabaseReference myUserPostRef = database.getReference("User Posts").child(post.getUser()).child(post.getId()).child("downs");

                    final DatabaseReference myUpsRefUsersPost = database.getReference("User Posts").child(post.getUser()).child(post.getId()).child("ups").child(downId);
                    final DatabaseReference myUpsRef = database.getReference("Posts").child(post.getId()).child("ups").child(downId);


                    final Map<String, Object> downKeys = new HashMap<>();
                    downKeys.put(downId, true);
                    myRef.updateChildren(downKeys, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            myUserPostRef.updateChildren(downKeys);
                        }
                    });
                    // ImageButton imageButton = (ImageButton) v;
                    // imageButton.setImageResource(R.mipmap.down_icon_focused);
                    //upButton.setImageResource(R.mipmap.up_vote_icon);
                    myUpsRef.removeValue();
                    myUpsRefUsersPost.removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            //TODO: Completion Handler
                            myUserPostRef.addChildEventListener(new ChildEventListener() {
                                int num = 0;

                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                            Log.d("SNAP", dataSnapshot.toString());
                                    num++;
                                    ref.setValue(num);
                                    postDownRef.setValue(num);
//
                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {
                                    Log.d("NUM:", num + "");
                                    num--;
                                    ref.setValue(num);
                                    postDownRef.setValue(num);
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
            });

        }

        @Override
        public void onClick(View v) {
        }
    }

}
