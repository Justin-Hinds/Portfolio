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
import com.google.firebase.auth.FirebaseUser;
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

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class ProfileRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public ProfileRecyclerAdapter(ArrayList data, DishUser dishUser, Context context){
        //noinspection unchecked
        mDataSet = data;
        mDishUser = dishUser;
        mContext = context;
    }
    public interface AddFellowStaffInterface {
        void addFriend();
//        void messagePlayer();
    }
    public void setAddFellowStaffInterface(AddFellowStaffInterface playerInterface, MainBoardRecyclerAdapter.OnItemSelected postInterface){
        mListener = playerInterface;
        postLitener = postInterface;
    }
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private ArrayList<Post> mDataSet;
    private final DishUser mDishUser;
    private final Context mContext;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private AddFellowStaffInterface mListener;
    private MainBoardRecyclerAdapter.OnItemSelected postLitener;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_cards, parent, false);
            return new ViewHolder(v, mDataSet,postLitener);
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.profile_header_item, parent, false);
            return new VHHeader(v, mDishUser,mListener,postLitener);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {
            Post post = getItem(position);

            DatabaseReference userRef = database.getReference("Users").child(post.getUser());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DishUser dishUser = dataSnapshot.getValue(DishUser.class);
                    if(mDishUser.getProfilePicURL() != null){

                        Picasso.with(mContext)
                                .load(mDishUser.getProfilePicURL())
                                .transform(new CropCircleTransformation())
                                .into(((ViewHolder)holder).imageView);
                    }
                    assert dishUser != null;
                    ((ViewHolder) holder).mStaffName.setText(dishUser.getName());
                   // Log.d("SNAPSHOT: ", "");

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //cast holder to VHItem and set data
            ((ViewHolder) holder).mTextView.setText(post.toString());
            Linkify.addLinks(((ViewHolder) holder).mTextView, Linkify.WEB_URLS);
            if(post.getImgURL() != null){
                Picasso.with(mContext).load(post.imgURL).into(((ViewHolder)holder).postImage);
            }

        } else if (holder instanceof VHHeader) {
            //cast holder to VHHeader and set data for header.
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference currentUserRef = database.getReference("Users").child(user.getUid());
            currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DishUser dishUser = dataSnapshot.getValue(DishUser.class);
                    if(dishUser.getFriends().containsKey(mDishUser.getId())){
                        ((VHHeader)holder).friends.setText("Request Sent");
                        if(mDishUser.getFriends().containsKey(dishUser.getId())){
                            ((VHHeader)holder).friends.setText("Friends");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Log.d("Dataset VHHEADER", mDishUser.getName() + "");
            if(mDishUser.getProfilePicURL() != null){
               Picasso.with(mContext)
                       .load(mDishUser.getProfilePicURL())
                       .transform(new CropCircleTransformation())
                       .into(((VHHeader)holder).imageView);
            }


            ((VHHeader)holder).mStaffName.setText(mDishUser.getName());
            ((VHHeader)holder).faveFood.setText("Favorite Food : " + mDishUser.getFavoriteFood());
            ((VHHeader)holder).faveDrink.setText("Favorite Drink: " + mDishUser.getFavoriteDrink());
            ((VHHeader)holder).faveRestaurant.setText("Favorite Restaurant: " + mDishUser.getFavoriteRestaurant());
        }
    }


    private boolean isPositionHeader(int position){
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

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView mTextView;
        final TextView mStaffName;
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
            imageView =  itemView.findViewById(R.id.profile_icon);
            mTextView =  itemView.findViewById(R.id.post_textview);
            mStaffName =  itemView.findViewById(R.id.user_name);
            postImage =  itemView.findViewById(R.id.imageView);
            commentsButton =  itemView.findViewById(R.id.comments_button);
            upButton =  itemView.findViewById(R.id.up_button);
            downButton =  itemView.findViewById(R.id.down_button);

            commentsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCommentsClicked(mDataset.get(getAdapterPosition() - 1));

                }
            });

            upButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Post post = mDataset.get(getAdapterPosition() - 1);
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference ref = database.getReference("User Posts").child(post.getUser()).child(post.getId()).child("upCount");
                    final DatabaseReference myRef = database.getReference("Posts").child(post.getId()).child("ups");
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
//                            Log.d("SNAP", dataSnapshot.toString());
                            num ++;
                            ref.setValue(num);
                            myRef.setValue(num);
//                            Log.d("NUM: ", num + "");
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
                            myRef.setValue(num);
//                            Log.d("NUM: ", num + "");
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
    }
    public static class VHHeader extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView mStaffName;
        final TextView faveFood;
        final TextView faveDrink;
        final TextView faveRestaurant;
        final ImageView imageView;
        final DishUser mDishUser;
        public  TextView friends;
        final AddFellowStaffInterface mListener;
        final MainBoardRecyclerAdapter.OnItemSelected postListener;
        public VHHeader(View itemView, DishUser dishUser, AddFellowStaffInterface listener, MainBoardRecyclerAdapter.OnItemSelected postsListener) {
            super(itemView);
            mDishUser = dishUser;
            postListener = postsListener;
            mListener = listener;
            friends = itemView.findViewById(R.id.friends);
            imageView =  itemView.findViewById(R.id.profile_icon);
            mStaffName =  itemView.findViewById(R.id.user_name);
            faveRestaurant =  itemView.findViewById(R.id.favorite_restaurant);
            faveDrink =  itemView.findViewById(R.id.favorite_drink);
            faveFood =  itemView.findViewById(R.id.favorite_food);
            friends =  itemView.findViewById(R.id.friends);
            friends.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.friends:
                    mListener.addFriend();
                    friends.setText("Request sent");
                    break;
//                case R.id.message:
//                    mListener.messagePlayer();
//                    break;
                default:
                    break;

            }
        }
    }
    private Post getItem(int position) {
        return mDataSet.get(position - 1);
    }
    public void update(ArrayList<Post> posts){
        mDataSet = posts;
        notifyDataSetChanged();
    }

}
