package com.arcane.tournantscheduling.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arcane.tournantscheduling.R;

import java.util.ArrayList;


public class SectionRecyclerAdapter extends RecyclerView.Adapter<SectionRecyclerAdapter.ViewHolder>{
    private final Context mContext;
    private ArrayList<String> mDataset;
    private String scheduleDate;
    private long scheduledDateNumber;
    private OnSectionSelectedListener mListener;
    public SectionRecyclerAdapter(ArrayList<String> myData, long dateNumber, String date, Context context){
        mContext = context;
        mDataset = myData;
        scheduleDate = date;
        scheduledDateNumber = dateNumber;
    }
    public void setOnSectionSelectedListener(OnSectionSelectedListener listener){
        mListener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_item_section,parent,false);

        return new ViewHolder(v,mDataset,mListener, scheduleDate, scheduledDateNumber);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.sectionName.setText(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private static final String TAG = "VIEWHOLDER" ;
        TextView sectionName;
        OnSectionSelectedListener listener;
        String date;
        long dateNumber;
        public ViewHolder(View itemView, ArrayList<String> name, OnSectionSelectedListener myListener,String newDate, long newDateNumber ) {
            super(itemView);
            itemView.setOnClickListener(this);
            sectionName = itemView.findViewById(R.id.section_name);
            listener = myListener;
            date = newDate;
            dateNumber = newDateNumber;
        }

        @Override
        public void onClick(View view) {
            //Log.d("SECTION NAME", sectionName.getText().toString());
            listener.onSectionSelected(sectionName.getText().toString(), date,dateNumber );
        }
    }

    public  interface OnSectionSelectedListener{
        void onSectionSelected(String section, String date, long dateNumber);
    }

}
