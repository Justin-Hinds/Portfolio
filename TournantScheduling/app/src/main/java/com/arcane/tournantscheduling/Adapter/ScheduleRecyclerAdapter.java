package com.arcane.tournantscheduling.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arcane.tournantscheduling.Models.Day;
import com.arcane.tournantscheduling.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleRecyclerAdapter.ViewHolder> {
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference();
    private final Context mContext;
    private OnDaySelectedListener mListener;
    private ArrayList<Day> mDataset = new ArrayList<>();

    public ScheduleRecyclerAdapter(ArrayList myData, Context context) {
        //noinspection unchecked
        mDataset = myData;
        mContext = context;
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
        String scheduledTime = day.getHour() + ":" + day.getMin() + " - " + day.getHourOut() + ":" + day.getMinOut();
        holder.time.setText( scheduledTime);
        //Log.d("LENGTH ", separated.length + "");
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView month;
        TextView time;
        TextView day;
        TextView dayOfWeek;
        OnDaySelectedListener listener;


        public ViewHolder(View itemView, ArrayList<Day> scheduledDays, OnDaySelectedListener newListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            month = itemView.findViewById(R.id.textView_month);
            time = itemView.findViewById(R.id.textView_scheduled_time);
            day = itemView.findViewById(R.id.textView_date);
            dayOfWeek = itemView.findViewById(R.id.textView_weekday);
            listener = newListener;
        }

        @Override
        public void onClick(View v) {
            listener.onScheduleSelected(mDataset.get(getAdapterPosition()));
        }
    }


    public void update(ArrayList<Day> days) {
        mDataset = days;
        notifyDataSetChanged();
        if(mDataset.size()>1){
        }
    }
}
