package com.arcane.sticks.Models;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.arcane.sticks.R;

import java.util.ArrayList;



public class ProfileRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public ProfileRecyclerAdapter(ArrayList data){mDataSet = data;}
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    ArrayList<Post> mDataSet;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_cards, parent, false);
            return new ViewHolder(v);
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.profile_header_item, parent, false);
            VHHeader vheader = new VHHeader(v);
            return vheader;
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            Post post = getItem(position);
            //cast holder to VHItem and set data
            ((ViewHolder) holder).mTextView.setText(post.toString());
            ((ViewHolder) holder).mPlayerName.setText(post.getUser());

        } else if (holder instanceof VHHeader) {
            //cast holder to VHHeader and set data for header.
        }
    }


    public boolean isPositionHeader(int position){
        return position == 0;
    }
    @Override
    public int getItemCount() {
        return mDataSet.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView mTextView;
        TextView mPlayerName;
        ImageButton commentsButton;
        ImageButton upButton;
        ImageButton downButton;
        MainBoardRecyclerAdapter.OnItemSelected mListener;
        ArrayList<Post> mDataset = new ArrayList<>();
        public ViewHolder(View itemView) {
            super(itemView);
            //mDataset = posts;
            mTextView = (TextView) itemView.findViewById(R.id.post_textview);
            mPlayerName = (TextView) itemView.findViewById(R.id.player_name);
            commentsButton = (ImageButton) itemView.findViewById(R.id.comments_button);
            upButton = (ImageButton) itemView.findViewById(R.id.up_button);
            downButton = (ImageButton) itemView.findViewById(R.id.down_button);
            //mListener = listener;
        }
    }
    public static class VHHeader extends RecyclerView.ViewHolder {

        public VHHeader(View itemView) {
            super(itemView);

        }
    }
    private Post getItem(int position) {
        return mDataSet.get(position - 1);
    }
    public void update(ArrayList<Post> posts){
        mDataSet = posts;
        notifyDataSetChanged();
    }
}
