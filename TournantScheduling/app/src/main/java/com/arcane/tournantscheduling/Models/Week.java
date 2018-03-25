package com.arcane.tournantscheduling.Models;

/**
 * Created by ChefZatoichi on 3/24/18.
 */

public class Week {
    Day mMonday;
    Day mTuesday;
    Day mWednesday;
    Day mThursday;
    Day mFriday;
    Day mSaturday;
    Day mSunday;

    public Week(Day monday, Day tuesday, Day wednesday, Day thursday, Day friday, Day saturday, Day sunday){
        mMonday = monday;
        mTuesday = tuesday;
        mWednesday = wednesday;
        mThursday = thursday;
        mFriday = friday;
        mSaturday = saturday;
        mSunday = sunday;
    }


}
