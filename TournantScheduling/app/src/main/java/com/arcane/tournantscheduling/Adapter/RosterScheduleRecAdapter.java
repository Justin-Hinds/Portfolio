package com.arcane.tournantscheduling.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.R;

import java.util.ArrayList;



public class RosterScheduleRecAdapter extends RecyclerView.Adapter<RosterScheduleRecAdapter.ViewHolder> {

    private ArrayList<Staff> mDataset;
    private ArrayList<Staff> mSecondDataset = new ArrayList<>();
    private Context mContext;
    private OnScheduleSelectedListener mListener;

    public RosterScheduleRecAdapter(ArrayList myData, Context context) {
        mContext = context;
        //noinspection unchecked
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
        return new ViewHolder(v,mDataset);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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
