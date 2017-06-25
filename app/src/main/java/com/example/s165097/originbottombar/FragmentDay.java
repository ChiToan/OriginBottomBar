package com.example.s165097.originbottombar;

import android.app.Dialog;
import android.os.Bundle;
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
import java.util.Collections;
import java.util.concurrent.TimeUnit;


public class FragmentDay extends Fragment implements ListView.OnItemClickListener, View.OnClickListener {
    public static final String POSITION_KEY = "FragmentPositionKey";
    static String tabTitles[] = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    int position;
    ArrayList<Switch> rawList = new ArrayList<>();
    ArrayList<String> switchesList = new ArrayList<>();
    private ArrayAdapter<String> listAdapter;
    ListView addedTimes;
    TextView sectionLabel;

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
        sectionLabel = (TextView) view.findViewById(R.id.section_label);

        listAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, R.id.tvSwitchTitle);

        addedTimes = (ListView) view.findViewById(R.id.added_times);
        addedTimes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                editSwitch(pos, position);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    while (!currentThread().isInterrupted()) {
//                        Thread.sleep(500);
                    WeekProgram wpg = HeatingSystem.getWeekProgram();
                    rawList = wpg.data.get(tabTitles[position]);
                    switchesList.clear();
                    for (int i = 9; i > -1; i--) {
                        if (rawList.get(i).getState()) {
                            if (rawList.get(i).getType().equals("day")) {
                                switchesList.add(rawList.get(i).getTime() + "\t\t\t\t\t\t\t" + getEmojiByUnicode(0x2600) + "\t\t\tDay");
                            } else {
                                switchesList.add(rawList.get(i).getTime() + "\t\t\t\t\t\t\t" + getEmojiByUnicode(0x1F319) + "\t\t\tNight");
                            }

                        } else {
                            i = -1;
                        }
                    }
                    if (!switchesList.isEmpty()) {
                        Collections.reverse(switchesList);
                        listAdapter.addAll(switchesList);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addedTimes.setAdapter(listAdapter);
                                sectionLabel.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sectionLabel.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                } catch (Exception e) {
                    System.err.println("Error from fragmentDay " + e);
                }
            }
        }).start();

        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    private void editSwitch(final int listpos, final int daypos) {
        final Dialog d = new Dialog(this.getActivity());

        d.setTitle("Edit Switch");
        d.setContentView(R.layout.edit_switch_fragment);

        final TimePicker picker = (TimePicker) d.findViewById(R.id.timePicker);
        Button btnSave = (Button) d.findViewById(R.id.saveButton);
        Button btnRemove = (Button) d.findViewById((R.id.removeButton));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String strTime = String.format("%02d", picker.getHour()) + ":" + String.valueOf(picker.getMinute());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WeekProgram wpg = HeatingSystem.getWeekProgram();
                            String swType = wpg.data.get(tabTitles[daypos]).get(10 - switchesList.size() + listpos).getType();
                            wpg.data.get(tabTitles[daypos]).set(10 - switchesList.size() + listpos, new Switch(swType, true, strTime));
                            HeatingSystem.setWeekProgram(wpg);
                            switchesList.remove(listpos);
                            if (swType.equals("day")){
                                switchesList.add(strTime + "\t\t\t\t\t\t\t" + getEmojiByUnicode(0x2600) + "\t\t\tDay");
                            } else {
                                switchesList.add(strTime + "\t\t\t\t\t\t\t" + getEmojiByUnicode(0x1F319) + "\t\t\tNight");
                            }
                            Collections.sort(switchesList);
                            Thread.sleep(500);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            System.err.println("Error from daySave " + e);
                        }
                    }
                }).start();
                listAdapter.clear();
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                listAdapter.addAll(switchesList);
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
                            String swType = wpg.data.get(tabTitles[daypos]).get(10 - switchesList.size() + listpos).getType();
                            wpg.data.get(tabTitles[daypos]).set(10 - switchesList.size() + listpos, new Switch(swType, false, "00:00"));
                            HeatingSystem.setWeekProgram(wpg);
                            switchesList.remove(listpos);
                            Collections.sort(switchesList);
                            Thread.sleep(500);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.removed), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            System.err.println("Error from dayRemove " + e);
                        }
                    }
                }).start();
                listAdapter.clear();
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                listAdapter.addAll(switchesList);
                if (switchesList.isEmpty()){
                    sectionLabel.setVisibility(View.VISIBLE);
                }

                d.dismiss();
            }
        });

        picker.setIs24HourView(true);
        picker.setHour(10);
        picker.setMinute(10);

        d.show();
    }

    public String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }
}
