package com.arcane.tournantscheduling;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.arcane.tournantscheduling.Models.Staff;

import java.util.ArrayList;



public class RosterRecyclerAdapter extends RecyclerView.Adapter<RosterRecyclerAdapter.ViewHolder> {

    ArrayList<Staff> mDataset;
    Context mContext;
    private OnStaffSelectedListener mListener;

    public RosterRecyclerAdapter(ArrayList myData, Context context) {
        mContext = context;
        //noinspection unchecked
        mDataset = myData;
    }

    public interface OnStaffSelectedListener {
        void onPlayerSelected(Staff staff);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
