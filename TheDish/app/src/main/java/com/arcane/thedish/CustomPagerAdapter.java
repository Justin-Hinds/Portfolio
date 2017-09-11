package com.arcane.thedish;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



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
                return new UsersFrag();
            case 1:
                return new MainBoardFrag();

            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return mContext.getString(R.string.users);
            case 1:
                return mContext.getString(R.string.posts);

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
