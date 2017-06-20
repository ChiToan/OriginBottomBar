package com.example.s165097.originbottombar;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class DayPagerFragmentAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 7;
    private String tabTitles[] = new String[] { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
    private Context context;

    public DayPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
//        this.context = context;

    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentDay.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}