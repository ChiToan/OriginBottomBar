package com.example.s165097.originbottombar;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;


public class FragmentDay extends Fragment implements ListView.OnItemClickListener, View.OnClickListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    int position;
    ArrayList<Switch> rawList = new ArrayList<>();
    ArrayList<String> switchesList = new ArrayList<>();
    private ArrayAdapter<String> listAdapter;
    static String tabTitles[] = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    public static final String POSITION_KEY = "FragmentPositionKey";
    private static final String ARG_SECTION_NUMBER = "section_number";

    public FragmentDay() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentDay newInstance(Bundle args) {
        FragmentDay fragment = new FragmentDay();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        HeatingSystem.BASE_ADDRESS = "http://wwwis.win.tue.nl/2id40-ws/6";
        HeatingSystem.WEEK_PROGRAM_ADDRESS = HeatingSystem.BASE_ADDRESS + "/weekProgram";
        position = getArguments().getInt(POSITION_KEY);
        final View view = inflater.inflate(R.layout.week_fragment_content, container, false);
        TextView sectionLabel = (TextView) view.findViewById(R.id.section_label);

        final ListView addedTimes = (ListView) view.findViewById(R.id.added_times);
        addedTimes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editSwitch(view, position, position);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    WeekProgram wpg = HeatingSystem.getWeekProgram();
                    rawList = wpg.data.get(tabTitles[position]);
                    for (int i = 9; i > -1; i--) {
                        if (rawList.get(i).getState()) {
                            switchesList.add("Switch to " + rawList.get(i).getType() + " temperature at " + rawList.get(i).getTime());
                        } else {
                            i = -1;
                        }
                    }

                } catch (Exception e) {
                    System.err.println("Error from getdata " + e);
                }
            }
        }).start();
        if (!switchesList.isEmpty()) {
            listAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.day_row_view, R.id.textView);
            listAdapter.addAll(switchesList);
            addedTimes.setAdapter(listAdapter);
            switchesList.clear();
        } else {
            sectionLabel.setText("No switches are set");
        }
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    private void editSwitch(View view, final int listpos, final int daypos) {
        final View fView = view;
        final Dialog d = new Dialog(this.getActivity());

        d.setTitle("Edit Switch");
        d.setContentView(R.layout.edit_switch_fragment);

        final TimePicker picker = (TimePicker) d.findViewById(R.id.timePicker);
        Button btnSave = (Button) d.findViewById(R.id.saveButton);
        Button btnRemove = (Button) d.findViewById((R.id.removeButton));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String strTime = String.valueOf(picker.getHour()) + ":" + String.valueOf(picker.getMinute());


//                Toast.makeText(getContext(), testString, Toast.LENGTH_SHORT).show();
                //updateAndSaveSchedule();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WeekProgram wpg = HeatingSystem.getWeekProgram();
                            String swType = wpg.data.get(tabTitles[daypos]).get(9-listpos).getType();
                            wpg.data.get(tabTitles[daypos]).set(9-listpos, new Switch(swType, true, strTime));
                            HeatingSystem.setWeekProgram(wpg);
                        } catch (Exception e) {
                            System.err.println("Error from getdata " + e);
                        }
                    }
                }).start();
                d.dismiss();
            }
        });
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WeekProgram wpg = HeatingSystem.getWeekProgram();
                            String swType = wpg.data.get(tabTitles[daypos]).get(9-listpos).getType();
                            wpg.data.get(tabTitles[daypos]).set(9-listpos, new Switch(swType, false, "00:00"));
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