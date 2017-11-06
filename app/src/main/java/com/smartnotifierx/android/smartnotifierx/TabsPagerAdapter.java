package com.smartnotifierx.android.smartnotifierx;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.smartnotifierx.android.smartnotifierx.CallsFragment;
import com.smartnotifierx.android.smartnotifierx.TasksFragment;
/**
 * Created by sv300_000 on 31-Oct-17.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;
    public TabsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm); this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int index) {
        switch (index) {
            case 0:
                return new CallsFragment();
            case 1:
                return new TasksFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
