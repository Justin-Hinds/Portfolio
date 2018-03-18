package com.arcane.tournantscheduling;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class MessagesFrag extends Fragment {


    public static MessagesFrag newInstance() {
        return new MessagesFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_messages,container,false);




        return root;
    }

}
