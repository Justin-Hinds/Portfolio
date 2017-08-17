package com.arcane.sticks.Models;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.arcane.sticks.R;

import java.util.ArrayList;


public class MainBoardRecyclerAdapter extends RecyclerView.Adapter<MainBoardRecyclerAdapter.ViewHolder> {
    ArrayList<Post> mDataset;
    static final String TAG = "MaindBoardRecycler:";
    OnItemSelected mListener;
    public interface OnItemSelected {
        void onCommentsClicked(Post post);
        void onDownClicked();
        void onUpClicked();
        void onHyperlinkClicked();
    }
    public void setOnInteraction(final OnItemSelected listener){
        mListener = listener;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mTextView;
        TextView mPlayerName;
        ImageButton commentsButton;
        ImageButton upButton;
        ImageButton downButton;
        OnItemSelected mListener;
        ArrayList<Post> mDataset = new ArrayList<>();

        public ViewHolder(View itemView, OnItemSelected listener, ArrayList<Post> posts) {
            super(itemView);
            itemView.setOnClickListener(this);
            mDataset = posts;
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
                    mListener.onUpClicked();
                }
            });
            downButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onDownClicked();
                }
            });

        }

        @Override
        public void onClick(View v) {
        }
    }
    public MainBoardRecyclerAdapter(ArrayList myData){mDataset = myData;}
    @Override
    public MainBoardRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_cards, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //Log.i(TAG, " CreatViewHolder Dataset: " + mDataset);
        MainBoardRecyclerAdapter.ViewHolder vh = new ViewHolder(v,mListener,mDataset);
        return vh;
    }
    public void update(ArrayList<Post> posts){
        mDataset = posts;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position).toString());
        holder.mPlayerName.setText(mDataset.get(position).getUser());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
