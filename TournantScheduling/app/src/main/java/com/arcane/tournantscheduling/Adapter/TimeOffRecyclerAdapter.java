package com.arcane.tournantscheduling.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arcane.tournantscheduling.Activities.HomeScreenActivity;
import com.arcane.tournantscheduling.Activities.LoginActivity;
import com.arcane.tournantscheduling.Models.Message;
import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.Models.TimeOff;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.Utils.DataManager;

import java.sql.Time;
import java.util.ArrayList;


public class TimeOffRecyclerAdapter extends RecyclerView.Adapter<TimeOffRecyclerAdapter.ViewHolder> {
    ArrayList<TimeOff> mDataset;
    Context mContext;
    Staff manager;
    DataManager dataManager = new DataManager();
    public TimeOffRecyclerAdapter(ArrayList data, Context context, Staff staff){
        mDataset = data;
        mContext = context;
        manager = staff;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_timeoff,parent,false);


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        if(mDataset.get(position).getApproved() == null && manager.isManager()){
            holder.approved.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.approved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Approve Request")
                            .setMessage("Approve or Deny time off request")
                            .setPositiveButton("APPROVE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TimeOff timeOff = mDataset.get(position);
                                    dataManager.timeOffRequestApproval(true,manager, timeOff.getSender(),mContext,timeOff);
                                    holder.approved.setText("APPROVED");
                                    holder.approved.setTextColor(Color.GREEN);
                                }
                            }).setNegativeButton("DENY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TimeOff timeOff = mDataset.get(position);
                            dataManager.timeOffRequestApproval(false,manager, timeOff.getSender(),mContext, timeOff);
                            holder.approved.setText("DENIED");
                            holder.approved.setTextColor(Color.RED);
                        }
                    }).show();
                }
            });
        }

        TimeOff timeOff = mDataset.get(position);
        String dateString = timeOff.getStart() + " - " + timeOff.getEnd();
        if(manager.getId().equals(timeOff.getSender())){
            holder.name.setText("Your Time Off Request: ");
        }else if(!manager.getId().equals(timeOff.getSender())){
            holder.name.setText(timeOff.getSenderName());
        }
        holder.dates.setText(dateString);
        if(timeOff.getApproved() == null){
            holder.approved.setText("Pending");
        }else if(timeOff.getApproved()){
            holder.approved.setText("APPROVED");
            holder.approved.setTextColor(Color.GREEN);
        }else if(!timeOff.getApproved()){
            holder.approved.setText("DENIED");
            holder.approved.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView dates, approved,name;
        public ViewHolder(View itemView) {
            super(itemView);
            dates = itemView.findViewById(R.id.textView_dates);
            approved = itemView.findViewById(R.id.textView_approved);
            name = itemView.findViewById(R.id.textview_staff_name);
            Log.d("DATA", mDataset.size() + "");
            Log.d("ADAPTER", getAdapterPosition() + "");

//            if(mDataset.get(getAdapterPosition() + 1).getApproved() == null && manager.isManager()){
//                approved.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
//                approved.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        new AlertDialog.Builder(mContext)
//                                .setTitle("Approve Request")
//                                .setMessage("Approve or Deny time off request")
//                                .setPositiveButton("APPROVE", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        TimeOff timeOff = mDataset.get(getAdapterPosition());
//                                        dataManager.timeOffRequestApproval(true,manager, timeOff.getSender(),mContext,timeOff);
//                                    }
//                                }).setNegativeButton("DENY", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                TimeOff timeOff = mDataset.get(getAdapterPosition());
//                                dataManager.timeOffRequestApproval(false,manager, timeOff.getSender(),mContext, timeOff);
//
//                            }
//                        }).show();
//                    }
//                });
//            }
        }
    }
    public void update(ArrayList<TimeOff> timeOffs){
        mDataset = timeOffs;
        notifyDataSetChanged();

    }
}
