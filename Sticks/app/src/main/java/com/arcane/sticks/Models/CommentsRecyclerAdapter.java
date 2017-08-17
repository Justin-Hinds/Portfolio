package com.arcane.sticks.Models;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arcane.sticks.R;

import java.util.ArrayList;

/**
 * Created by ChefZatoichi on 8/17/17.
 */

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {
    ArrayList<PostComment> mDataset;
    public CommentsRecyclerAdapter(ArrayList myData){mDataset = myData;}



    @Override
    public CommentsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        CommentsRecyclerAdapter.ViewHolder vh = new CommentsRecyclerAdapter.ViewHolder(v,mDataset);
        return vh;
    }

    @Override
    public void onBindViewHolder(CommentsRecyclerAdapter.ViewHolder holder, int position) {
        Log.d("STRING", mDataset.get(position).getText());
        holder.mTextView.setText(mDataset.get(position).getText());

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ArrayList<PostComment> mDataset = new ArrayList<>();
        public ViewHolder(View itemView,  ArrayList<PostComment> postComments) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.comment_text);
        }

    }
    public void update(ArrayList<PostComment> postComments){
        mDataset = postComments;
        notifyDataSetChanged();
    }
}
