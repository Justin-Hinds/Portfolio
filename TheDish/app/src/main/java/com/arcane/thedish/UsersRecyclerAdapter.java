package com.arcane.thedish;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class UsersRecyclerAdapter extends  RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder> {
    private ArrayList<DishUser> mDataset;
    private OnPlayerSelectedListener mListener;
    private final Context mContext;
    public UsersRecyclerAdapter(ArrayList myData, Context context){
        mContext = context;
        //noinspection unchecked
        mDataset = myData;
    }
    public interface OnPlayerSelectedListener{
        void onPlayerSelected(DishUser dishUser);
    }
    public  void setOnPlayerInteraction(final OnPlayerSelectedListener listener){
        mListener = listener;
    }
    @Override
    public UsersRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_users, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //Log.i(TAG, " CreatViewHolder Dataset: " + mDataset);
        return new ViewHolder(v,mListener,mDataset);    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mDataset.get(position).getProfilePicURL() != null){
        String imgURL = mDataset.get(position).getProfilePicURL();
//            Picasso.with(mContext)
//                    .load(imgURL)
//                    .transform(new CropCircleTransformation())
//                    .into(holder.imageView);

        }
//        holder.mTextView.setText(mDataset.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView mTextView;
        final OnPlayerSelectedListener mListener;
        ArrayList<DishUser> mDataset = new ArrayList<>();
        final ImageView imageView;
        public ViewHolder(View itemView, OnPlayerSelectedListener listener, ArrayList<DishUser> dishUsers) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTextView = (TextView) itemView.findViewById(R.id.player_name);
            imageView = (ImageView) itemView.findViewById(R.id.profile_icon);
            mListener = listener;
            mDataset = dishUsers;

        }

        @Override
        public void onClick(View v) {
//            Log.i("POSITION", getAdapterPosition() + "");
//            Log.i("DATA", mDataset.toString() + "");

            mListener.onPlayerSelected(mDataset.get(getAdapterPosition()));
        }
    }
    public void update(ArrayList<DishUser> dishUsers){
        mDataset = dishUsers;
        notifyDataSetChanged();
    }
}
