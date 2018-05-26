package com.arcane.tournantscheduling.Adapter;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arcane.tournantscheduling.Models.Day;
import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.ViewModels.RosterViewModel;
import com.arcane.tournantscheduling.ViewModels.ScheduleViewModel;

import java.util.ArrayList;



public class RosterScheduleRecAdapter extends RecyclerView.Adapter<RosterScheduleRecAdapter.ViewHolder> {

    private ArrayList<Staff> mDataset;
    private ArrayList<Staff> mSecondDataset = new ArrayList<>();
    private FragmentActivity mContext;
    private OnScheduleSelectedListener mListener;
    private Staff selecteduser;
    ArrayList<Day> arrayList = new ArrayList();
    String selectedDay;

    public RosterScheduleRecAdapter(ArrayList myData, FragmentActivity context) {
        mContext = context;
        Log.d("Adapter","Constructor");
        ScheduleViewModel scheduleViewModel = ViewModelProviders.of(context).get(ScheduleViewModel.class);
        RosterViewModel rosterViewModel = ViewModelProviders.of(context).get(RosterViewModel.class);
        selecteduser = rosterViewModel.getSelectedUser();
        selectedDay = scheduleViewModel.getDateString();
        scheduleViewModel.getCompanyLiveSchedule(rosterViewModel.getCurrentUser()).observe(context, new Observer<ArrayList<Day>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Day> days) {
                arrayList = days;
                notifyDataSetChanged();
            }
        });
        mDataset = myData;
    }

    public interface OnScheduleSelectedListener {
        void onScheduledStaffSelected(Staff staff, View view);
        void onOutTimeSelected(Staff staff, View view);
    }
    public void setOnScheduledSelectedListener(RosterScheduleRecAdapter.OnScheduleSelectedListener listener){
        mListener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_roster_schedule_item,parent,false);
        Log.d("ViewHolder","onCreate");
        return new ViewHolder(v,mDataset);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        for(Day day : arrayList){
            if(day.getDate().equals(selectedDay) && day.getUserId().equals(mDataset.get(position).getId())){
                holder.in.setText(day.getInTime());
                holder.out.setText(day.getOutTime());
            }
        }
        holder.staffName.setText(mDataset.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView staffName;
        TextView in;
        TextView out;
        ArrayList<Staff> userArrayList;
        public ViewHolder(View itemView, ArrayList<Staff> data) {
            super(itemView);
            Log.d("ViewHolder","ViewHolder");

            itemView.setOnClickListener(this);
            userArrayList = data;
            staffName = itemView.findViewById(R.id.textview_staff_name);
            in = itemView.findViewById(R.id.in);
            out = itemView.findViewById(R.id.out);
            out.setOnClickListener(view -> {
                mListener.onOutTimeSelected(mDataset.get(getAdapterPosition()),itemView);
                Toast.makeText(mContext,"TIME OUT", Toast.LENGTH_SHORT).show();

            });
            in.setOnClickListener(view -> {
                mListener.onScheduledStaffSelected(mDataset.get(getAdapterPosition()),itemView);
                Toast.makeText(mContext,"TIME IN", Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public void onClick(View view) {
            //Log.d("ROSTER NAME", staffName.getText().toString());
            mSecondDataset.add(mDataset.get(getAdapterPosition()));
        }
    }
    public void update(ArrayList<Staff> staff) {
        mDataset = staff;
        notifyDataSetChanged();
        if(mDataset.size()>1){
           // Log.d("UPDATE SCHEDULE ROSTER", mDataset.size() + mDataset.get(0).getName());
        }
    }
}
