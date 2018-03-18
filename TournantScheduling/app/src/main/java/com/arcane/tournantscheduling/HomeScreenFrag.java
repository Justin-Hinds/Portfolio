package com.arcane.tournantscheduling;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HomeScreenFrag  extends Fragment {
    public static HomeScreenFrag newInstance(){return new HomeScreenFrag();}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home_screen,container,false);




        return root;
    }
}
