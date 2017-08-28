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

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class ProfileRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public ProfileRecyclerAdapter(ArrayList data, Player player, Context context){
        //noinspection unchecked
        mDataSet = data;
        mPlayer = player;
        mContext = context;
    }
    public interface AddFellowPlayerInterface{
        void addPlayer();
        void messagePlayer();
    }
    public void setAddFellowPlayerInterface(AddFellowPlayerInterface playerInterface, MainBoardRecyclerAdapter.OnItemSelected postInterface){
        mListener = playerInterface;
        postLitener = postInterface;
    }
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private ArrayList<Post> mDataSet;
    private final Player mPlayer;
    private final Context mContext;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private AddFellowPlayerInterface mListener;
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
            return new VHHeader(v,mPlayer,mListener,postLitener);
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
                    Player player = dataSnapshot.getValue(Player.class);
                    if(mPlayer.getProfilePicURL() != null){

                        Picasso.with(mContext)
                                .load(mPlayer.getProfilePicURL())
                                .transform(new CropCircleTransformation())
                                .into(((ViewHolder)holder).imageView);
                    }
                    assert player != null;
                    ((ViewHolder) holder).mPlayerName.setText(player.getName());
                   // Log.d("SNAPSHOT: ", "");

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //cast holder to VHItem and set data
            ((ViewHolder) holder).mTextView.setText(post.toString());
            Linkify.addLinks(((ViewHolder) holder).mTextView,Linkify.WEB_URLS);
            if(post.getImgURL() != null){
                Picasso.with(mContext).load(post.imgURL).into(((ViewHolder)holder).postImage);
            }

        } else if (holder instanceof VHHeader) {
            //cast holder to VHHeader and set data for header.
            Log.d("Dataset VHHEADER", mPlayer.getName() + "");
            if(mPlayer.getProfilePicURL() != null){
               Picasso.with(mContext)
                       .load(mPlayer.getProfilePicURL())
                       .transform(new CropCircleTransformation())
                       .into(((VHHeader)holder).imageView);
            }
            ((VHHeader)holder).mPlayerName.setText(mPlayer.getName());
            ((VHHeader)holder).gamerTag.setText("Gamer Tag: " + mPlayer.getGamerTag());
            ((VHHeader)holder).psnID.setText("PSN ID: " + mPlayer.getPsnID());
            ((VHHeader)holder).console.setText("Console: " + mPlayer.getPreferredConsole());

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
        final TextView mPlayerName;
        final ImageButton commentsButton;
        final ImageButton upButton;
        final ImageButton downButton;
        final ImageView imageView;
        final ImageView postImage;
        MainBoardRecyclerAdapter.OnItemSelected mListener;
        ArrayList<Post> mDataset = new ArrayList<>();
        public ViewHolder(View itemView,ArrayList<Post> posts, MainBoardRecyclerAdapter.OnItemSelected listener) {
            super(itemView);
            mDataset = posts;
            mListener = listener;
            imageView = (ImageView) itemView.findViewById(R.id.profile_icon);
            mTextView = (TextView) itemView.findViewById(R.id.post_textview);
            mPlayerName = (TextView) itemView.findViewById(R.id.player_name);
            postImage = (ImageView) itemView.findViewById(R.id.imageView);
            commentsButton = (ImageButton) itemView.findViewById(R.id.comments_button);
            upButton = (ImageButton) itemView.findViewById(R.id.up_button);
            downButton = (ImageButton) itemView.findViewById(R.id.down_button);

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

        final TextView gamerTag;
        final TextView psnID;
        final TextView console;
        final TextView mPlayerName;
        final TextView friends;
        final TextView message;
        final ImageView imageView;
        final Player mPlayer;
        final AddFellowPlayerInterface mListener;
        final MainBoardRecyclerAdapter.OnItemSelected postListener;
        public VHHeader(View itemView, Player player, AddFellowPlayerInterface listener, MainBoardRecyclerAdapter.OnItemSelected postsListener) {
            super(itemView);
            mPlayer = player;
            postListener = postsListener;
            mListener = listener;
            imageView = (ImageView) itemView.findViewById(R.id.profile_icon);
            mPlayerName = (TextView) itemView.findViewById(R.id.player_name);
            console = (TextView) itemView.findViewById(R.id.preferred_console);
            psnID = (TextView) itemView.findViewById(R.id.psn_id);
            gamerTag = (TextView) itemView.findViewById(R.id.gamer_tag);
            friends = (TextView) itemView.findViewById(R.id.friends);
            message = (TextView) itemView.findViewById(R.id.message);
            friends.setOnClickListener(this);
            message.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.friends:
                    mListener.addPlayer();
                    break;
                case R.id.message:
                    mListener.messagePlayer();
                    break;
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
//public static void addFriends(Player player){
//    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference friendsRef = database.getReference("Users").child(user).child("fellowPlayers");
//
//
//}
}
