package com.arcane.sticks.Models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arcane.sticks.R;

import java.util.ArrayList;

/**
 * Created by ChefZatoichi on 8/16/17.
 */

public class PlayersRecyclerAdapter extends  RecyclerView.Adapter<PlayersRecyclerAdapter.ViewHolder> {
    ArrayList<Player> mDataset;
    OnPlayerSelectedListener mListener;
    public PlayersRecyclerAdapter(ArrayList myData){mDataset = myData;}
    public interface OnPlayerSelectedListener{
        void onPlayerSelected(Player player);
    }
    public  void setOnPlayerInteraction(final OnPlayerSelectedListener listener){
        mListener = listener;
    }
    @Override
    public PlayersRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTextView;
        OnPlayerSelectedListener mListener;
        ArrayList<Player> mDataset = new ArrayList<>();
        public ViewHolder(View itemView, OnPlayerSelectedListener listener, ArrayList<Player> players) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.post_textview);
            mListener = listener;

        }

        @Override
        public void onClick(View v) {
            mListener.onPlayerSelected(mDataset.get(getAdapterPosition()));
        }
    }
    public void update(ArrayList<Player> players){
        mDataset = players;
        notifyDataSetChanged();
    }
}
