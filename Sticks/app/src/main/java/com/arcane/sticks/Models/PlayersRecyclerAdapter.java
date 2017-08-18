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
// create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //Log.i(TAG, " CreatViewHolder Dataset: " + mDataset);
        PlayersRecyclerAdapter.ViewHolder vh = new PlayersRecyclerAdapter.ViewHolder(v,mListener,mDataset);
        return vh;    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position).getName());
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
            itemView.setOnClickListener(this);
            mTextView = (TextView) itemView.findViewById(R.id.player_name);
            mListener = listener;
            mDataset = players;

        }

        @Override
        public void onClick(View v) {
            Log.i("POSITION", getAdapterPosition() + "");
            Log.i("DATA", mDataset.toString() + "");

            mListener.onPlayerSelected(mDataset.get(getAdapterPosition()));
        }
    }
    public void update(ArrayList<Player> players){
        mDataset = players;
        notifyDataSetChanged();
    }
}
