package com.arcane.sticks;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class MainBoardRecyclerAdapter extends RecyclerView.Adapter<MainBoardRecyclerAdapter.ViewHolder> {
    ArrayList<Post> mDataset;
    public static class ViewHolder extends RecyclerView.ViewHolder{
    TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.post_textview);
        }
    }
    public MainBoardRecyclerAdapter(ArrayList myData){mDataset = myData;}
    @Override
    public MainBoardRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_cards, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MainBoardRecyclerAdapter.ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    public void update(ArrayList<Post> posts){
        mDataset = posts;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position).toString());

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
