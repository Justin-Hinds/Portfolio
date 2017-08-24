package com.arcane.sticks.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arcane.sticks.Models.Player;
import com.arcane.sticks.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by ChefZatoichi on 8/21/17.
 */

public class ChatLogRecAdapter extends RecyclerView.Adapter <ChatLogRecAdapter.ViewHolder>{


    ArrayList<Player> mDataset;
    ChatLogRecAdapter.OnPlayerSelectedListener mListener;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    public ChatLogRecAdapter(ArrayList myData){mDataset = myData;}
    public interface OnPlayerSelectedListener{
        void onChatPlayerSelected(Player player);
    }
    public  void setOnPlayerInteraction(final ChatLogRecAdapter.OnPlayerSelectedListener listener){
        mListener = listener;
    }
    @Override
    public ChatLogRecAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_log_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //Log.i(TAG, " CreatViewHolder Dataset: " + mDataset);
        ChatLogRecAdapter.ViewHolder vh = new ChatLogRecAdapter.ViewHolder(v,mListener,mDataset);
        return vh;    }



    @Override
    public void onBindViewHolder(final ChatLogRecAdapter.ViewHolder holder, int position) {
        Player player = mDataset.get(position);
        Log.d("MDATA: ", player.getName());
        holder.mTextView.setText(player.getName());
        DatabaseReference ref = database.getReference().child("Users").child(player.getName());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // Log.i("SNAP: ", dataSnapshot.toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTextView;
        ChatLogRecAdapter.OnPlayerSelectedListener mListener;
        ArrayList<Player> mDataset = new ArrayList<>();
        public ViewHolder(View itemView, ChatLogRecAdapter.OnPlayerSelectedListener listener, ArrayList<Player> players) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTextView = (TextView) itemView.findViewById(R.id.player_name);
            mListener = listener;
            mDataset = players;

        }

        @Override
        public void onClick(View v) {
            Log.i("POSITION", getAdapterPosition() + "");
            Log.i("DATA", mDataset.toString() + "");

            mListener.onChatPlayerSelected(mDataset.get(getAdapterPosition()));
        }
    }
    public void update(ArrayList<Player> players){
        mDataset =  players;
        notifyDataSetChanged();
    }

}
