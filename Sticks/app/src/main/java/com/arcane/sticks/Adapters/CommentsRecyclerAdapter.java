package com.arcane.sticks.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcane.sticks.models.Player;
import com.arcane.sticks.models.PostComment;
import com.arcane.sticks.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;



public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {
    private ArrayList<PostComment> mDataset;
    private Player mPlayer;
    private final Context mContext;
    public CommentsRecyclerAdapter(ArrayList myData, Context context){
        mContext = context;
        //noinspection unchecked
        mDataset = myData;
    }



    @Override
    public CommentsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v,mDataset);
    }

    @Override
    public void onBindViewHolder(final CommentsRecyclerAdapter.ViewHolder holder, int position) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference playerRef = database.getReference("Users").child(mDataset.get(position).getSender());
        playerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPlayer = dataSnapshot.getValue(Player.class);
                Picasso.with(mContext)
                        .load(mPlayer.getProfilePicURL())
                        .transform(new CropCircleTransformation())
                        .into(holder.imageView);
        Log.d("STRING", mPlayer.getId());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.mTextView.setText(mDataset.get(position).getText());

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mTextView;
        ImageView imageView;
        ArrayList<PostComment> mDataset = new ArrayList<>();
        public ViewHolder(View itemView,  ArrayList<PostComment> postComments) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.profile_icon);
            mTextView = (TextView) itemView.findViewById(R.id.comment_text);

        }

    }
    public void update(ArrayList<PostComment> postComments){
        mDataset = postComments;
        notifyDataSetChanged();
    }
}
