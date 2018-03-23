package com.arcane.tournantscheduling;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arcane.tournantscheduling.Models.Staff;

import java.util.ArrayList;



public class RosterRecyclerAdapter extends RecyclerView.Adapter<RosterRecyclerAdapter.ViewHolder> {

    private ArrayList<Staff> mDataset;
    private Context mContext;
    private OnStaffSelectedListener mListener;

    public RosterRecyclerAdapter(ArrayList myData, Context context) {
        mContext = context;
        //noinspection unchecked
        mDataset = myData;
    }

    public interface OnStaffSelectedListener {
        void OnStaffSelected(Staff staff);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_roster,parent,false);
        return new ViewHolder(v,mDataset);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.staffName.setText(mDataset.get(position).getName());
        Log.d("BIND", "HIT");
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView staffName;
        ArrayList<Staff> userArrayList;
        public ViewHolder(View itemView, ArrayList<Staff> data) {
            super(itemView);
            itemView.setOnClickListener(this);
            userArrayList = data;
            staffName = itemView.findViewById(R.id.textview_staff_name);
        }

        @Override
        public void onClick(View view) {
            Log.d("ROSTER NAME", staffName.getText().toString());
        }
    }
    public void update(ArrayList<Staff> staff) {
        mDataset = staff;
        notifyDataSetChanged();
        if(mDataset.size()>1){

        Log.d("UPDATE", mDataset.size() + mDataset.get(0).getName());
        }
    }
}
