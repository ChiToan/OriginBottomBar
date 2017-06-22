package com.example.s165097.originbottombar;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;


public class FragmentWeek extends Fragment {
//    SectionsPagerAdapter mSectionsPagerAdapter;
//    ViewPager mViewPager;
    TabLayout tabLayout;
    ViewPager container;


    public static FragmentWeek newInstance() {
        return new FragmentWeek();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.week_fragment_activity, container, false);



        ViewPager viewPager = (ViewPager) view.findViewById(R.id.container);
        /** Important: Must use the child FragmentManager or you will see side effects. */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
//
//        mViewPager = (ViewPager) view.findViewById(R.id.container);
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//        tabLayout.setupWithViewPager(mViewPager);


        tabLayout = (TabLayout) view.findViewById(R.id.tabbar);
        tabLayout.setupWithViewPager(viewPager);
//        container = (ViewPager) view.findViewById(R.id.container);

//        tabLayout.addTab(tabLayout.newTab().setText("Monday"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tuesday"));
//        tabLayout.addTab(tabLayout.newTab().setText("Wednesday"));
//        tabLayout.addTab(tabLayout.newTab().setText("Thursday"));
//        tabLayout.addTab(tabLayout.newTab().setText("Friday"));
//        tabLayout.addTab(tabLayout.newTab().setText("Saturday"));
//        tabLayout.addTab(tabLayout.newTab().setText("Sunday"));

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_time);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Dikzak", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                newSwitch(view);
            }
        });




        return view;
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
        final int PAGE_COUNT = 7;
        private String tabTitles[] = new String[] { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putInt(FragmentDay.POSITION_KEY, position);
            return FragmentDay.newInstance(args);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

    }

    private void newSwitch(View view){
        final View fView = view;
        final Dialog d = new Dialog(this.getActivity());
        d.setTitle("New Switch");
        d.setContentView(R.layout.add_switch_fragment);
//        final android.widget.Switch swDayNight = (android.widget.Switch) d.findViewById(R.id.swDayNight);
//
        final TimePicker picker = (TimePicker) d.findViewById(R.id.timePicker);
        Button btnSave = (Button) d.findViewById(R.id.saveButton);
//        Button btnDelete = (Button) d.findViewById(R.id.btnDelete);
//        btnDelete.setWidth(0); // make it essentially invisible.
//
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String strTime = String.valueOf(picker.getHour()) + ":" + String.valueOf(picker.getMinute());
//                String swType = (swDayNight.isChecked()) ? "night" : "day";
//                if(fiveOfEither().equals(swType)){
//                    Snackbar.make(fView, "Failed to save: Only 5 switches of each type allowed!", Snackbar.LENGTH_LONG)
//                            .setAction("", null).show();
//                    d.dismiss();
//                    return;
//                }
//                String item = "Switch to " + swType + " temperature\nat " + GeneralHelper.correctTime(strTime);
//
//                lItems.add(item);
//                listAdapter.clear();
//                listAdapter.addAll(lItems);
//                listAdapter.notifyDataSetChanged();
//                updateAndSaveSchedule();
//                d.dismiss();
//            }
//        });
        picker.setIs24HourView(true);
        picker.setHour(10);
        picker.setMinute(10);

        d.show();
    }
//    public static class FragmentDay extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        public FragmentDay() {
//        }
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static FragmentDay newInstance(int sectionNumber) {
//            FragmentDay fragment = new FragmentDay();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.week_fragment_content, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.test, getArguments().getInt(ARG_SECTION_NUMBER)));
//            return rootView;
//        }
//    }
//    private class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//        SectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            // getItem is called to instantiate the fragment for the given page.
//            // Return a PlaceholderFragment (defined as a static inner class below).
//            return FragmentDay.newInstance(position + 1);
//        }
//
//        @Override
//        public int getCount() {
//            // Show 3 total pages.
//            return 3;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "SECTION 1";
//                case 1:
//                    return "SECTION 2";
//                case 2:
//                    return "SECTION 3";
//            }
//            return null;
//        }
//    }

}
