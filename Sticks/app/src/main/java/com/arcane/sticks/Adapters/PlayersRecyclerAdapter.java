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
import com.arcane.sticks.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class PlayersRecyclerAdapter extends  RecyclerView.Adapter<PlayersRecyclerAdapter.ViewHolder> {
    private ArrayList<Player> mDataset;
    private OnPlayerSelectedListener mListener;
    private final Context mContext;
    public PlayersRecyclerAdapter(ArrayList myData, Context context){
        mContext = context;
        //noinspection unchecked
        mDataset = myData;
    }
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
        return new ViewHolder(v,mListener,mDataset);    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mDataset.get(position).getProfilePicURL() != null){
        String imgURL = mDataset.get(position).getProfilePicURL();
            Picasso.with(mContext)
                    .load(imgURL)
                    .transform(new CropCircleTransformation())
                    .into(holder.imageView);

        }
        holder.mTextView.setText(mDataset.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView mTextView;
        final OnPlayerSelectedListener mListener;
        ArrayList<Player> mDataset = new ArrayList<>();
        final ImageView imageView;
        public ViewHolder(View itemView, OnPlayerSelectedListener listener, ArrayList<Player> players) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTextView = (TextView) itemView.findViewById(R.id.player_name);
            imageView = (ImageView) itemView.findViewById(R.id.profile_icon);
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
