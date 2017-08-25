package com.arcane.sticks.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.arcane.sticks.frags.ChatFrag;
import com.arcane.sticks.frags.MainBoardFrag;
import com.arcane.sticks.frags.PlayersFrag;
import com.arcane.sticks.R;


public class CustomPagerAdapter extends FragmentPagerAdapter {
    private final Context mContext;
    public CustomPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new PlayersFrag();
            case 1:
                return new MainBoardFrag();
            case 2:
                return new ChatFrag();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return mContext.getString(R.string.players);
            case 1:
                return mContext.getString(R.string.posts);
            case 2:
                return mContext.getString(R.string.chat);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
