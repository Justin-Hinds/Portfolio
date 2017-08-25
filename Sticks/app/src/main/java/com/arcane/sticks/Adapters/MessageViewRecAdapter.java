package com.arcane.sticks.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arcane.sticks.models.Message;
import com.arcane.sticks.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.provider.Settings.System.DATE_FORMAT;


public class MessageViewRecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Message> mDataset;
    public MessageViewRecAdapter(ArrayList myData){//noinspection unchecked
        mDataset = myData;}
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;


        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_sent_item, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_received_item, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;

//        // create a new view
//        View v = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.comments_list_item, parent, false);
//        // set the view's size, margins, paddings and layout parameters
//        MessageViewRecAdapter.ViewHolder vh = new MessageViewRecAdapter.ViewHolder(v,mDataset);
//        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = mDataset.get(position);
        //formatting Date with time information
        Date today = new Date(message.getTime());
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd HH:SS");
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mTextView;
        ArrayList<Message> mDataset = new ArrayList<>();
        public ViewHolder(View itemView,  ArrayList<Message> messages) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.message_text);
        }

    }
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

            messageText = (TextView) itemView.findViewById(R.id.message_text);
            timeText = (TextView) itemView.findViewById(R.id.time_text);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        final TextView messageText;
        final TextView timeText;
        //ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.message_text);
            timeText = (TextView) itemView.findViewById(R.id.time_text);
            //profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }
        void bind(Message message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
        }

    }
}
