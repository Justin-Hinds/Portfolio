package com.arcane.thedish.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcane.thedish.Models.DishUser;
import com.arcane.thedish.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    public static ArrayList<DishUser> mDataSet;
    private OnPlayerSelectedListener mListener;


    public UsersRecyclerAdapter(ArrayList myData, Context context) {
        mContext = context;
        //noinspection unchecked
        mDataSet = myData;
    }

    public void setOnPlayerInteraction(final OnPlayerSelectedListener listener) {
        mListener = listener;
    }

    @Override
    public UsersRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //Log.i(TAG, " CreatViewHolder Dataset: " + userArrayList);
        return new ViewHolder(v, mListener, mDataSet);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        Log.d("USER", userArrayList.get(position).getName());
        DishUser dishUser = mDataSet.get(position);
        if (mDataSet.get(position).getProfilePicURL() != null) {
            String imgURL = mDataSet.get(position).getProfilePicURL();
            Picasso.with(mContext)
                    .load(imgURL)
                    .transform(new CropCircleTransformation())
                    .into(holder.imageView);

        }
//        holder.requestImage.setImageBitmap(null);
        if(mDataSet.get(0).getRequests().containsKey(dishUser.getId())){
            holder.requestImage.setVisibility(View.VISIBLE);
        }
        holder.mTextView.setText(dishUser.getName());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void update(ArrayList<DishUser> dishUsers) {
        mDataSet = dishUsers;
        notifyDataSetChanged();

    }


    public interface OnPlayerSelectedListener {
        void onPlayerSelected(DishUser dishUser);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView mTextView;
        final OnPlayerSelectedListener mListener;
        final ImageView imageView;
        final ImageView requestImage;
        ArrayList<DishUser> userArrayList = new ArrayList<>();

        public ViewHolder(View itemView, OnPlayerSelectedListener listener, ArrayList<DishUser> dishUsers) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTextView = itemView.findViewById(R.id.user_name);
            imageView = itemView.findViewById(R.id.profile_icon);
            mListener = listener;
            requestImage = itemView.findViewById(R.id.request_icon);
            userArrayList = dishUsers;

        }

        @Override
        public void onClick(View v) {
//            Log.i("POSITION", getAdapterPosition() + "");
//            Log.i("DATA", userArrayList.size() + "");

            mListener.onPlayerSelected(mDataSet.get(getAdapterPosition()));
        }
    }
}
