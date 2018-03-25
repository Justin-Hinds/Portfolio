package com.arcane.tournantscheduling;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arcane.tournantscheduling.Models.Day;
import com.arcane.tournantscheduling.Models.Staff;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleRecyclerAdapter.ViewHolder> {
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference();
    private final Context mContext;
    private ArrayList<Day> mDataset;

    public ScheduleRecyclerAdapter(ArrayList myData, Context context) {
        //noinspection unchecked
        mDataset = myData;
        mContext = context;
    }
    @Override
    public ScheduleRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_scheduled_day,parent,false);
        return new ViewHolder(v,mDataset);
    }

    @Override
    public void onBindViewHolder(ScheduleRecyclerAdapter.ViewHolder holder, int position) {
        Day day = mDataset.get(position);
        holder.month.setText( day.getMonth());
        String[] separated = day.getDate().split("-");
        holder.day.setText(separated[1]);
        String scheuledTime = day.getHour() + ":" + day.getMin() + " - " + day.getHourOut() + ":" + day.getMinOut();
        holder.time.setText( scheuledTime);
        Log.d("LENGTH ", separated.length + "");
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView month;
        TextView time;
        TextView day;
        TextView dayOfWeek;

        public ViewHolder(View itemView, ArrayList<Day> scheduledDays) {
            super(itemView);
            month = itemView.findViewById(R.id.textView_month);
            time = itemView.findViewById(R.id.textView_scheduled_time);
            day = itemView.findViewById(R.id.textView_date);
            dayOfWeek = itemView.findViewById(R.id.textView_weekday);
        }
    }
}
