package com.example.s165097.originbottombar;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;


public class FragmentWeek extends Fragment {
    //    SectionsPagerAdapter mSectionsPagerAdapter;
//    ViewPager mViewPager;
    TabLayout tabLayout;
    ViewPager container;
    private ArrayAdapter<String> listAdapter;
    ArrayList<Switch> switchList = new ArrayList<>();
    ArrayList<String> lItems;
    static String tabTitles[] = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};


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


        HeatingSystem.BASE_ADDRESS = "http://wwwis.win.tue.nl/2id40-ws/6";
        HeatingSystem.WEEK_PROGRAM_ADDRESS = HeatingSystem.BASE_ADDRESS + "/weekProgram";

        lItems = new ArrayList<String>();
        listAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.week_fragment_activity);
        listAdapter.addAll(lItems);


        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.container);
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
//                final int[] freepos = {0};
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            WeekProgram wpg = HeatingSystem.getWeekProgram();
//                            switchList = wpg.data.get(tabTitles[position]);
//                            if (!switchList.get(0).getState()) {
//                                freepos[0] = 1;
//                            }
//                        } catch (Exception e) {
//                            System.err.println("Error from getdata " + e);
//                        }
//                    }
//                });
//                if (freepos[0] == 1) {
                newSwitch(view, viewPager.getCurrentItem());
//                } else {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.noswitch), Toast.LENGTH_SHORT).show();
//                }
            }
        });


        return view;
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        private MyAdapter(FragmentManager fm) {
            super(fm);
        }

        final int PAGE_COUNT = 7;

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

    private void newSwitch(View view, final int pos) {
        final View fView = view;
        final Dialog d = new Dialog(this.getActivity());

        d.setTitle("New Switch");
        d.setContentView(R.layout.add_switch_fragment);
        final android.widget.Switch dayNightSwitch = (android.widget.Switch) d.findViewById(R.id.dayNightSwitch);

        final TimePicker picker = (TimePicker) d.findViewById(R.id.timePicker);
        Button btnSave = (Button) d.findViewById(R.id.saveButton);

        btnSave.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                final String strTime = String.valueOf(picker.getHour()) + ":" + String.valueOf(picker.getMinute());
                final String swType = (dayNightSwitch.isChecked()) ? "night" : "day";
                final int[] freepos = {0};

                final int[] switchpos = {10};

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WeekProgram wpg = HeatingSystem.getWeekProgram();
                            switchList = wpg.data.get(tabTitles[pos]);
                            for (int i = 0; i < 6; i++) {
                                if (switchList.get(i).getType().equals("night") && !switchList.get(i).getState() && swType.equals("night")) {
                                    switchpos[0] = i;
                                    freepos[0] = 1;
                                    i = 6;
                                } else if (switchList.get(i).getType().equals("day") && !switchList.get(i).getState() && swType.equals("day")) {
                                    switchpos[0] = i;
                                    freepos[0] = 1;
                                    i = 6;
                                }
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (freepos[0] == 0) {
                                        Toast.makeText(getActivity(), "No " + swType + " switches left", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            System.err.println("Error from getdata " + e);
                        }
                    }
                }).start();

//                Toast.makeText(getContext(), testString, Toast.LENGTH_SHORT).show();
                //updateAndSaveSchedule();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WeekProgram wpg = HeatingSystem.getWeekProgram();
                            wpg.data.get(tabTitles[pos]).set(switchpos[0], new Switch(swType, true, strTime));
                            HeatingSystem.setWeekProgram(wpg);
                        } catch (Exception e) {
                            System.err.println("Error from getdata " + e);
                        }
                    }
                }).start();
                d.dismiss();
            }
        });
        picker.setIs24HourView(true);
        picker.setHour(10);
        picker.setMinute(10);

        d.show();
    }

}
