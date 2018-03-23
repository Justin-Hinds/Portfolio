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
import java.util.zip.Inflater;


public class SectionRecyclerAdapter extends RecyclerView.Adapter<SectionRecyclerAdapter.ViewHolder>{
    private final Context mContext;
    private ArrayList<String> mDataset;
    private OnSectionSelectedListener mListener;
    public SectionRecyclerAdapter(ArrayList<String> myData, Context context){
        mContext = context;
        mDataset = myData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_item_roster,parent,false);

        return new ViewHolder(v,mDataset);
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

        public ViewHolder(View itemView, ArrayList<String> name ) {
            super(itemView);
            itemView.setOnClickListener(this);
            sectionName = itemView.findViewById(R.id.textview_staff_name);
        }

        @Override
        public void onClick(View view) {
            Log.d("SECTION NAME", sectionName.getText().toString());
        }
    }

    public static interface OnSectionSelectedListener{

    }

}
