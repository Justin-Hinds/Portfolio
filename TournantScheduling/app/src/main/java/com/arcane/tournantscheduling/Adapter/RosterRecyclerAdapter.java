package com.arcane.tournantscheduling.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.arcane.tournantscheduling.Frags.MessagesFrag;
import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.Frags.RosterFrag;
import com.arcane.tournantscheduling.Frags.ScheduleRosterFrag;

import java.util.ArrayList;



public class RosterRecyclerAdapter extends RecyclerView.Adapter<RosterRecyclerAdapter.ViewHolder> {

    private ArrayList<Staff> mDataset;
    private ArrayList<Staff> mSecondDataset = new ArrayList<>();
    private Context mContext;
    private String fragTag;

    private OnStaffSelectedListener mListener;
    public RosterRecyclerAdapter(ArrayList myData, Context context, String tag) {
        mContext = context;
        //noinspection unchecked
        fragTag = tag;
        mDataset = myData;
    }

    public interface OnStaffSelectedListener {
        void OnStaffSelected(Staff staff);
        void OnNewChatSelected(Staff staff);
        void OnStaffChecked(int position, ArrayList<Staff> staffMembers);
    }
    public void setOnStaffSelectedListener(RosterRecyclerAdapter.OnStaffSelectedListener listener){
        mListener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_roster,parent,false);
            return new ViewHolder(v,mDataset);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.staffName.setText(mDataset.get(position).getName());
        if(fragTag.equals(RosterFrag.TAG)|| fragTag.equals(MessagesFrag.TAG)){
            holder.checkBox.setVisibility(View.GONE);
        }else if(fragTag.equals(ScheduleRosterFrag.TAG)){
            holder.checkBox.setVisibility(View.VISIBLE);
        }

//        if(!RosterFrag.isInActionMode){
//            holder.checkBox.setVisibility(View.GONE);
//        }else{
//            if(RosterFrag.inSchedulingMode) {
//                holder.checkBox.setVisibility(View.VISIBLE);
//                holder.checkBox.setChecked(false);
//            }
//        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView staffName;
        CheckBox checkBox;
        ArrayList<Staff> userArrayList;
        public ViewHolder(View itemView, ArrayList<Staff> data) {
            super(itemView);
            itemView.setOnClickListener(this);
            userArrayList = data;
            staffName = itemView.findViewById(R.id.textview_staff_name);
            checkBox = itemView.findViewById(R.id.checkBox);
            Log.d("TAG", fragTag);
            checkBox.setOnClickListener(view -> {
                if(checkBox.isChecked()) {
                        if (!mSecondDataset.contains(mDataset.get(getAdapterPosition()))) {
                        mSecondDataset.add(mDataset.get(getAdapterPosition()));
                    }
                    if (mSecondDataset != null) {
                        mListener.OnStaffChecked(getAdapterPosition(), mSecondDataset);
                    }
                }else {
                    mSecondDataset.remove(mSecondDataset.get(getAdapterPosition()));
                }
            });

        }

        @Override
        public void onClick(View view) {
            if(fragTag.equals(MessagesFrag.TAG)){
//                Log.d("TAG", fragTag);
//                Log.d("POSITION", mDataset.get(getAdapterPosition()).getName());
                mListener.OnNewChatSelected(mDataset.get(getAdapterPosition()));
            }
            if(fragTag.equals(RosterFrag.TAG)){
                Log.d("TAG", fragTag);
            }
            if (fragTag.equals(ScheduleRosterFrag.TAG)) {

            }
        }
    }
    public void update(ArrayList<Staff> staff) {
        mDataset = staff;
        notifyDataSetChanged();
        if(mDataset.size()>1){

       // Log.d("UPDATE", mDataset.size() + mDataset.get(0).getName());
        }
    }
}
