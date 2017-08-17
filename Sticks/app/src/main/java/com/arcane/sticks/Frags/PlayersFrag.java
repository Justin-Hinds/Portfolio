package com.arcane.sticks.Frags;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcane.sticks.Models.MainBoardRecyclerAdapter;
import com.arcane.sticks.Models.PlayersRecyclerAdapter;
import com.arcane.sticks.R;

import java.util.ArrayList;


public class PlayersFrag extends Fragment {
    private RecyclerView mRecyclerView;
    private PlayersRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList myDataset = new ArrayList();
    PlayersRecyclerAdapter.OnPlayerSelectedListener mListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.players_frag_layout,container,false);

        myDataset = new ArrayList();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.rec_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new PlayersRecyclerAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnPlayerInteraction(mListener);
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getListenerFromContext(context);
    }

    private void getListenerFromContext(Context context) {
        if (context instanceof PlayersRecyclerAdapter.OnPlayerSelectedListener) {
            mListener = (PlayersRecyclerAdapter.OnPlayerSelectedListener) context;
        } else {
            throw new ClassCastException("Containing activity must " +
                    "implement OnPersonInteractionListener");
        }
    }
}
