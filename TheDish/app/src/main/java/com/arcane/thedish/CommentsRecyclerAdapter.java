package com.arcane.thedish;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {
    private ArrayList<PostComment> mDataset;
    private DishUser mUser;
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
        PostComment comment = mDataset.get(position);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference playerRef = database.getReference("Users").child(mDataset.get(position).getSender());
        playerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(DishUser.class);
                Picasso.with(mContext)
                        .load(mUser.getProfilePicURL())
                        .transform(new CropCircleTransformation())
                        .into(holder.imageView);
        Log.d("STRING", mUser.getId());
                holder.userName.setText(mUser.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.mTextView.setText(comment.getText());
        Date today = new Date(comment.getTime());
        long oneDay = 86400000;
        if((comment.getTime() - System.currentTimeMillis())/oneDay >= 1 ){
            DateFormat DATE_FORMAT =  SimpleDateFormat.getDateInstance(DateFormat.SHORT);
            String date = DATE_FORMAT.format(today);
            holder.timeText.setText(date);

        }else {
            DateFormat DATE_FORMAT =  SimpleDateFormat.getTimeInstance(DateFormat.SHORT);
            String date = DATE_FORMAT.format(today);
            holder.timeText.setText(date);

        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mTextView;
        final TextView timeText;
        final TextView userName;
        ImageView imageView;
        ArrayList<PostComment> mDataset = new ArrayList<>();
        public ViewHolder(View itemView, ArrayList<PostComment> postComments) {
            super(itemView);
            imageView =  itemView.findViewById(R.id.profile_icon);
            mTextView =  itemView.findViewById(R.id.comment_text);
            timeText = itemView.findViewById(R.id.time_text);
            userName = itemView.findViewById(R.id.user_name);
        }

    }


    public void update(ArrayList<PostComment> postComments){
        mDataset = postComments;
        notifyDataSetChanged();
    }
}
