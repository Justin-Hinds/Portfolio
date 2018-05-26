package com.arcane.tournantscheduling.Adapter;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arcane.tournantscheduling.Models.Day;
import com.arcane.tournantscheduling.Models.Message;
import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.Utils.DataManager;
import com.arcane.tournantscheduling.ViewModels.RosterViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleRecyclerAdapter.ViewHolder> {
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference();
    private OnDaySelectedListener mListener;
    private ArrayList<Day> mDataset = new ArrayList<>();
    FragmentActivity mContext;
    RosterViewModel rosterViewModel;
    Staff currentuser;
    Staff selecteduser;
    DataManager dataManager = new DataManager();
    public ScheduleRecyclerAdapter(ArrayList myData, FragmentActivity context) {
        //noinspection unchecked
        mDataset = myData;
        mContext = context;
        rosterViewModel = ViewModelProviders.of(mContext).get(RosterViewModel.class);
        currentuser = rosterViewModel.getCurrentUser();
        selecteduser = rosterViewModel.getSelectedUser();
    }

    @Override
    public ScheduleRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_scheduled_day,parent,false);
        return new ViewHolder(v,mDataset,mListener);
    }
    public interface OnDaySelectedListener{
        void onScheduleSelected(Day day);
    }
    public void setOnDaySelectedListener(OnDaySelectedListener listener){
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(ScheduleRecyclerAdapter.ViewHolder holder, int position) {
        Day day = mDataset.get(position);
        holder.month.setText( day.getMonth());
        String[] separated = day.getDate().split("-");
        holder.day.setText(separated[1]);
        String scheduledTime = day.getInTime() + " - " + day.getOutTime();
        holder.time.setText(scheduledTime);
        holder.dayOfWeek.setText(day.getDayOfWeek());
        //Log.d("LENGTH ", separated.length + "");
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        TextView month;
        TextView time;
        TextView day;
        TextView dayOfWeek;
        OnDaySelectedListener listener;


        public ViewHolder(View itemView, ArrayList<Day> scheduledDays, OnDaySelectedListener newListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            if(currentuser.isManager()){
                itemView.setOnLongClickListener(this);
//                if(mDataset.get(getAdapterPosition() - 1).getUserId().equals(currentuser.getId())){
//                    selecteduser = currentuser;
//                }
                if(selecteduser == null){
                    selecteduser = currentuser;
                }
                Log.d("IS","MANAGER");
            }
            month = itemView.findViewById(R.id.textView_month);
            time = itemView.findViewById(R.id.textView_scheduled_time);
            day = itemView.findViewById(R.id.textView_date);
            dayOfWeek = itemView.findViewById(R.id.textView_weekday);
            listener = newListener;
        }

        @Override
        public void onClick(View v) {
            listener.onScheduleSelected(mDataset.get(getAdapterPosition()));
            Log.d("SHORT","CLICK");

        }

        @Override
        public boolean onLongClick(View v) {
            deleteSchedulePrompt(getAdapterPosition());
            Log.d("LONG","CLICK");
            return true;
        }
    }


    public void update(ArrayList<Day> days) {
        mDataset = days;
        notifyDataSetChanged();
        if(mDataset.size()>1){
        }
    }


    private void deleteSchedulePrompt( int position){
        new AlertDialog.Builder(mContext)
                .setTitle("Delete Schedule")
                .setMessage("Are you sure you want to delete this schedule?" )
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataManager.deleteScheduledDay(mDataset.get(position),selecteduser,mContext);
                        mDataset.remove(position);
                        notifyItemRemoved(position);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
