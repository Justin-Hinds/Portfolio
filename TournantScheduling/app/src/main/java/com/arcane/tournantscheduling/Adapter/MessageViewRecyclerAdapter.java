package com.arcane.tournantscheduling.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.arcane.tournantscheduling.Models.Message;
import com.arcane.tournantscheduling.Models.Staff;
import com.arcane.tournantscheduling.R;
import com.arcane.tournantscheduling.Utils.DataManager;
import com.arcane.tournantscheduling.ViewModels.RosterViewModel;
import com.google.firebase.auth.FirebaseAuth;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.provider.Settings.System.DATE_FORMAT;


public class MessageViewRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Message> mDataset;
    FragmentActivity mContext;
    RosterViewModel rosterViewModel;
    Staff currentuser;
    DataManager dataManager = new DataManager();
    int backgroundColor;

    public MessageViewRecyclerAdapter(ArrayList myData, FragmentActivity context){//noinspection unchecked
        mDataset = myData;
        mContext = context;
     rosterViewModel = ViewModelProviders.of(mContext).get(RosterViewModel.class);
     currentuser = rosterViewModel.getCurrentUser();
    }
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;


        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = mDataset.get(position);
        //formatting Date with time information
        Date today = new Date(message.getTime());
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd HH:SS");
        SimpleDateFormat format = (SimpleDateFormat) SimpleDateFormat.getTimeInstance();
        //Log.d("TIMESTAMP", format.format(today));
        String date = DATE_FORMAT.format(today);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                ((SentMessageHolder)holder).timeText.setText(date);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
                ((ReceivedMessageHolder)holder).timeText.setText(date);

        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        final TextView mTextView;
//        ArrayList<Message> mDataset = new ArrayList<>();
//        public ViewHolder(View itemView,  ArrayList<Message> messages) {
//            super(itemView);
//            mTextView = itemView.findViewById(R.id.message_text);
//        }
//    }

    public void update(ArrayList<Message> messages){
        mDataset = messages;
        notifyDataSetChanged();

    }

    public int getItemViewType(int position) {
        Message message = mDataset.get(position);

        if (message.getSender().equals(user)) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }


    private class SentMessageHolder extends RecyclerView.ViewHolder {
        final TextView messageText;
        final TextView timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.message_text);
            timeText = itemView.findViewById(R.id.time_text);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteMessagePrompt(mDataset.get(getAdapterPosition()),getAdapterPosition(), v);
                    backgroundColor = v.getDrawingCacheBackgroundColor();
                    v.setBackgroundColor(Color.BLUE);
                    Log.d("Delete at", getAdapterPosition() + "");
                    return true;
                }
            });
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        final TextView messageText;
        final TextView timeText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.message_text);
            timeText = itemView.findViewById(R.id.time_text);
            itemView.setOnLongClickListener(this);
        }
        void bind(Message message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
        }

        @Override
        public boolean onLongClick(View v) {
            deleteMessagePrompt(mDataset.get(getAdapterPosition()),getAdapterPosition(),v);
            backgroundColor = v.getDrawingCacheBackgroundColor();
            v.setBackgroundColor(Color.BLUE);
            Log.d("Delete at", getAdapterPosition() + "");
            return true;
        }
    }

    private void deleteMessagePrompt(Message message, int position,View view){
        new AlertDialog.Builder(mContext)
                .setTitle("Delete Message")
                .setMessage("Are you sure you want to delete this message?" )
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataManager.deleteMessage(mDataset.get(position),currentuser);
                        mDataset.remove(position);
                        notifyItemRemoved(position);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        view.setBackgroundColor(backgroundColor);
                        dialog.dismiss();
                    }
                }).show();
    }
}
