package com.arcane.sticks;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class MainBoardRecyclerAdapter extends RecyclerView.Adapter<MainBoardRecyclerAdapter.ViewHolder> {
    ArrayList mDataset;
    TextView mTextView;
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
    public MainBoardRecyclerAdapter(ArrayList myData){mDataset = myData;}
    @Override
    public MainBoardRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_cards, parent, false);
        // set the view's size, margins, paddings and layout parameters

        MainBoardRecyclerAdapter.ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
