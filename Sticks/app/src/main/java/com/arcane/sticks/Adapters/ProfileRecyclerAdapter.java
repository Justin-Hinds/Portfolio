package com.arcane.sticks.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.arcane.sticks.Models.Player;
import com.arcane.sticks.Models.Post;
import com.arcane.sticks.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class ProfileRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public ProfileRecyclerAdapter(ArrayList data, Player player){
        mDataSet = data;
        mPlayer = player;
    }
    public interface AddFellowPlayerInterface{
        void addPlayer();
    }
    public void setAddFellowPlayerInterface(AddFellowPlayerInterface playerInterface){
        mListener = playerInterface;
    }
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    ArrayList<Post> mDataSet;
    Player mPlayer;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    AddFellowPlayerInterface mListener;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_cards, parent, false);
            return new ViewHolder(v);
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.profile_header_item, parent, false);
            VHHeader vheader = new VHHeader(v,mPlayer,mListener);
            return vheader;
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
                    ((ViewHolder) holder).mPlayerName.setText(player.getName());
                   // Log.d("SNAPSHOT: ", "");

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //cast holder to VHItem and set data
            ((ViewHolder) holder).mTextView.setText(post.toString());

        } else if (holder instanceof VHHeader) {
            //cast holder to VHHeader and set data for header.
            Log.d("Dataset VHHEADER", mPlayer.getName() + "");
            ((VHHeader)holder).mPlayerName.setText(mPlayer.getName());
            ((VHHeader)holder).gamerTag.setText("Gamer Tag: " + mPlayer.getGamerTag());
            ((VHHeader)holder).psnID.setText("PSN ID: " + mPlayer.getPsnID());
            ((VHHeader)holder).console.setText("Console: " + mPlayer.getPreferredConsole());

        }
    }


    public boolean isPositionHeader(int position){
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

        TextView mTextView;
        TextView mPlayerName;
        ImageButton commentsButton;
        ImageButton upButton;
        ImageButton downButton;
        MainBoardRecyclerAdapter.OnItemSelected mListener;
        ArrayList<Post> mDataset = new ArrayList<>();
        public ViewHolder(View itemView) {
            super(itemView);
            //mDataset = posts;
            mTextView = (TextView) itemView.findViewById(R.id.post_textview);
            mPlayerName = (TextView) itemView.findViewById(R.id.player_name);
            commentsButton = (ImageButton) itemView.findViewById(R.id.comments_button);
            upButton = (ImageButton) itemView.findViewById(R.id.up_button);
            downButton = (ImageButton) itemView.findViewById(R.id.down_button);
            //mListener = listener;
        }
    }
    public static class VHHeader extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView gamerTag;
        TextView psnID;
        TextView console;
        TextView mPlayerName;
        TextView friends;
        TextView message;
        Player mPlayer;
        AddFellowPlayerInterface mListener;
        public VHHeader(View itemView, Player player, AddFellowPlayerInterface listener) {
            super(itemView);
            mPlayer = player;
            mListener = listener;
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
public static void addFriends(Player player){
    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference friendsRef = database.getReference("Users").child(user).child("fellowPlayers");


}
}
