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
import com.arcane.thedish.Models.Post;
import com.arcane.thedish.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import static android.R.attr.id;


public class ProfileRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private final DishUser mDishUser;
    private final Context mContext;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static ArrayList<Post> mDataSet;
    private AddFellowStaffInterface mListener;
    private MainBoardRecyclerAdapter.OnItemSelected postLitener;
    public ProfileRecyclerAdapter(ArrayList data, DishUser dishUser, Context context) {
        //noinspection unchecked
        mDataSet = data;
        mDishUser = dishUser;
        mContext = context;
    }

    public void setAddFellowStaffInterface(AddFellowStaffInterface playerInterface, MainBoardRecyclerAdapter.OnItemSelected postInterface) {
        mListener = playerInterface;
        postLitener = postInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_cards, parent, false);
            return new ViewHolder(v, mDataSet, postLitener);
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.profile_header_item, parent, false);
            return new VHHeader(v, mDishUser, mListener, postLitener);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {
            Post post = getItem(position);
            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference userRef = database.getReference("Users").child(post.getUser());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DishUser dishUser = dataSnapshot.getValue(DishUser.class);
                    if (mDishUser.getProfilePicURL() != null) {

                        Picasso.with(mContext)
                                .load(mDishUser.getProfilePicURL())
                                .transform(new CropCircleTransformation())
                                .into(((ViewHolder) holder).imageView);
                    }
                    assert dishUser != null;
                    ((ViewHolder) holder).mStaffName.setText(dishUser.getName());
                    // Log.d("SNAPSHOT: ", "");

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            if (post.getUps().containsKey(id)) {
                ((ViewHolder) holder).upButton.setImageResource(R.mipmap.up_icon_focused);
                ((ViewHolder) holder).downButton.setImageResource(R.mipmap.down_vote_icon);
//            Log.d(TAG, "Post ups: " + post.getUps());

            }
            if (post.getDowns().containsKey(id)) {
                ((ViewHolder) holder).upButton.setImageResource(R.mipmap.up_vote_icon);
                ((ViewHolder) holder).downButton.setImageResource(R.mipmap.down_icon_focused);
//            Log.d(TAG, "Post downs: " + post.getDowns());

            }
            Date today = new Date(post.getTime());
            long oneDay = 86400000;
//        Log.d("TIME DIFF ", System.currentTimeMillis() - post.getTime() + "");
            if ((System.currentTimeMillis() - post.getTime()) / oneDay >= 1) {
                DateFormat DATE_FORMAT = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
                String date = DATE_FORMAT.format(today);
                ((ViewHolder) holder).time.setText(date);

            } else {
                DateFormat DATE_FORMAT = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);
                String date = DATE_FORMAT.format(today);
                ((ViewHolder) holder).time.setText(date);

            }
            //cast holder to VHItem and set data
            ((ViewHolder) holder).mTextView.setText(post.toString());
            Linkify.addLinks(((ViewHolder) holder).mTextView, Linkify.WEB_URLS);
            //set imageBitmap to null to prevent images leaking to other posts
            ((ViewHolder) holder).postImage.setImageBitmap(null);
            if (post.getImgURL() != null) {
                Picasso.with(mContext)
                        .load(post.imgURL)
                        .into(((ViewHolder) holder).postImage);
            }

        } else if (holder instanceof VHHeader) {
            //cast holder to VHHeader and set data for header.
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference currentUserRef = database.getReference("Users").child(user.getUid());
            currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DishUser dishUser = dataSnapshot.getValue(DishUser.class);
                    Log.d("Friends: ", mDishUser.getFriends().toString() + " Their ID: " + dishUser.getId());
                    Log.d("ID: ", dishUser.getId());
                    if(dishUser.getId().equals(mDishUser.getId())){
                        ((VHHeader) holder).friends.setVisibility(View.GONE);
                    }else if (dishUser.getFriends().containsKey(mDishUser.getId())) {
                        ((VHHeader) holder).friends.setText("Request Sent");
                        if(dishUser.getRequests().containsKey(mDishUser.getId())){
                            Log.d("USERNAME",mDishUser.getId());
                            Log.d("OTHERUSER",dishUser.getId());
                            DatabaseReference requestRef2 = database.getReference("Users").child(mDishUser.getId()).child("requests").child(dishUser.getId());
                            DatabaseReference requestRef = database.getReference("Users").child(dishUser.getId()).child("requests").child(mDishUser.getId());
                            DatabaseReference ref = database.getReference("Users").child(dishUser.getId()).child("friends");
                            Map<String, Object> fellowUsers = new HashMap<>();
                            fellowUsers.put(mDishUser.getId(), true);
                            ref.updateChildren(fellowUsers);
                            requestRef.removeValue();
                            requestRef2.removeValue();
                        }
                        if (mDishUser.getFriends().containsKey(dishUser.getId())) {
                            ((VHHeader) holder).friends.setText("Friends");
                        }
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Log.d("Dataset VHHEADER", mDishUser.getName() + "");
            if (mDishUser.getProfilePicURL() != null) {
                Picasso.with(mContext)
                        .load(mDishUser.getProfilePicURL())
                        .transform(new CropCircleTransformation())
                        .into(((VHHeader) holder).imageView);
            }


            ((VHHeader) holder).mStaffName.setText(mDishUser.getName());
            ((VHHeader) holder).faveFood.setText("Favorite Food : " + mDishUser.getFavoriteFood());
            ((VHHeader) holder).faveDrink.setText("Favorite Drink: " + mDishUser.getFavoriteDrink());
            ((VHHeader) holder).faveRestaurant.setText("Favorite Restaurant: " + mDishUser.getFavoriteRestaurant());
        }
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    static Post getItem(int position) {
        return mDataSet.get(position - 1);
    }

    public void update(ArrayList<Post> posts) {
        mDataSet = posts;
        notifyDataSetChanged();
    }

    public interface AddFellowStaffInterface {
        void addFriend();
//        void messagePlayer();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView mTextView;
        final TextView mStaffName;
        final TextView time;
        final ImageButton commentsButton;
        final ImageButton upButton;
        final ImageButton downButton;
        final ImageView imageView;
        final ImageView postImage;
        MainBoardRecyclerAdapter.OnItemSelected mListener;
        ArrayList<Post> mDataset = new ArrayList<>();

        public ViewHolder(View itemView, ArrayList<Post> posts, MainBoardRecyclerAdapter.OnItemSelected listener) {
            super(itemView);
            mDataset = posts;
            mListener = listener;
            time = itemView.findViewById(R.id.time_text);
            imageView = itemView.findViewById(R.id.profile_icon);
            mTextView = itemView.findViewById(R.id.post_textview);
            mStaffName = itemView.findViewById(R.id.user_name);
            postImage = itemView.findViewById(R.id.imageView);
            commentsButton = itemView.findViewById(R.id.comments_button);
            upButton = itemView.findViewById(R.id.up_button);
            downButton = itemView.findViewById(R.id.down_button);

            commentsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCommentsClicked(getItem(getAdapterPosition()));

                }
            });

            upButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Post post = getItem(getAdapterPosition());
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
                    Post post = getItem(getAdapterPosition());
                    mListener.onDownClicked(post);
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
    }

    public static class VHHeader extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView mStaffName;
        final TextView faveFood;
        final TextView faveDrink;
        final TextView faveRestaurant;
        final ImageView imageView;
        final DishUser mDishUser;
        final AddFellowStaffInterface mListener;
        final MainBoardRecyclerAdapter.OnItemSelected postListener;
        public TextView friends;

        public VHHeader(View itemView, DishUser dishUser, AddFellowStaffInterface listener, MainBoardRecyclerAdapter.OnItemSelected postsListener) {
            super(itemView);
            mDishUser = dishUser;
            postListener = postsListener;
            mListener = listener;
            friends = itemView.findViewById(R.id.friends);
            imageView = itemView.findViewById(R.id.profile_icon);
            mStaffName = itemView.findViewById(R.id.user_name);
            faveRestaurant = itemView.findViewById(R.id.favorite_restaurant);
            faveDrink = itemView.findViewById(R.id.favorite_drink);
            faveFood = itemView.findViewById(R.id.favorite_food);
            friends = itemView.findViewById(R.id.friends);
            friends.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.friends:
                    mListener.addFriend();
                    friends.setText("Request sent");
                    break;

                default:
                    break;

            }
        }
    }

}
