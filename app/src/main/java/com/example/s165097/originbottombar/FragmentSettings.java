package com.example.s165097.originbottombar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;



public class FragmentSettings extends Fragment {

    Button getdata, putdata;
    TextView data1, data2;
    String getParam, oldv, newv;
    public static int dayTempVal, nightTempVal;
    SeekBar seekBarDay, seekBarNight;

    public static FragmentSettings newInstance() {
        FragmentSettings fragmentSettings= new FragmentSettings();
        Bundle args = new Bundle();
        args.putInt("dayTempVal", dayTempVal);
        args.putInt("nightTempVal", nightTempVal);
        fragmentSettings.setArguments(args);
        return fragmentSettings;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.settings_fragment, container, false);

        HeatingSystem.BASE_ADDRESS = "http://wwwis.win.tue.nl/2id40-ws/6";
        HeatingSystem.WEEK_PROGRAM_ADDRESS = HeatingSystem.BASE_ADDRESS + "/weekProgram";

        final TextView dayTemp = (TextView)view.findViewById(R.id.day_temp);
        dayTemp.setText((dayTempVal+50)/10.0 + " \u2103");
        ImageButton bPlusDay = (ImageButton)view.findViewById(R.id.bPlusDay);
        ImageButton bMinusDay = (ImageButton)view.findViewById(R.id.bMinusDay);
        bPlusDay.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dayTempVal++;
                seekBarDay.setProgress(dayTempVal);
            }
        }));
        bMinusDay.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dayTempVal--;
                seekBarDay.setProgress(dayTempVal);
            }
        }));
        seekBarDay = (SeekBar)view.findViewById(R.id.seekBarDay);
        seekBarDay.setProgress(dayTempVal);
        seekBarDay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                dayTemp.setText((i+50)/10.0 + " \u2103");
                seekBarDay.setProgress(i);
                dayTempVal = i;
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                dayTempVal = seekBar.getProgress();
            }
        });

        final TextView nightTemp = (TextView)view.findViewById(R.id.night_temp);
        nightTemp.setText((nightTempVal+50)/10.0 + " \u2103");
        ImageButton bPlusNight = (ImageButton)view.findViewById(R.id.bPlusNight);
        ImageButton bMinusNight = (ImageButton)view.findViewById(R.id.bMinusNight);
        bPlusNight.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nightTempVal++;
                seekBarNight.setProgress(nightTempVal);
            }
        }));
        bMinusNight.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nightTempVal--;
                seekBarNight.setProgress(nightTempVal);
            }
        }));
        seekBarNight = (SeekBar)view.findViewById(R.id.seekBarNight);
        seekBarNight.setProgress(nightTempVal);
        seekBarNight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                nightTemp.setText((i+50)/10.0 + " \u2103");
                seekBarNight.setProgress(i);
                nightTempVal = i;
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                nightTempVal = seekBar.getProgress();
            }
        });

        getdata = (Button)view.findViewById(R.id.getdata);
        putdata = (Button)view.findViewById(R.id.putdata);
        data1 = (TextView)view.findViewById(R.id.data1);
        data2 = (TextView)view.findViewById(R.id.data2);

        /* When the user clicks on GET Data button the value of the corresponding parameter is read from the server
        and displayed in TextView data1
         */
        getdata.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "Dikzak", Toast.LENGTH_SHORT).show();
                // TODO Auto-generated method stub
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getParam = "";
                        try {
                            getParam = HeatingSystem.get("currentTemperature");

									HeatingSystem.get("day");
									HeatingSystem.get("time");
									HeatingSystem.get("targetTemperature");
									HeatingSystem.get("dayTemperature");
									HeatingSystem.get("nightTemperature");
									HeatingSystem.get("weekProgramState");

                            data1.post(new Runnable() {
                                @Override
                                public void run() {
                                    data1.setText(getParam);
                                }
                            });
                        } catch (Exception e) {
                            System.err.println("Error from getdata "+e);
                        }
                    }
                }).start();
            }
        });

        /* When the user clicks on PUT Data button the old value of the corresponding parameter is read from the server
        and displayed in TextView data1, the new uploaded value is displayed in TextView data2
         */
        putdata.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            oldv = HeatingSystem.get("targetTemperature");
                            HeatingSystem.put("targetTemperature", "16.0");
                            newv = HeatingSystem.get("targetTemperature");

                            /* Uncomment the following parts to see how to work with the properties of the week program */
                            // Get the week program
                            WeekProgram wpg = HeatingSystem.getWeekProgram();
                            // Set the week program to default
                            wpg.setDefault();

                            wpg.data.get("Monday").set(5, new Switch("day", true, "07:30"));
                            wpg.data.get("Monday").set(1, new Switch("night", true, "08:30"));
                            wpg.data.get("Monday").set(6, new Switch("day", true, "18:00"));
                            wpg.data.get("Monday").set(7, new Switch("day", true, "12:00"));
                            wpg.data.get("Monday").set(8, new Switch("day", true, "18:00"));
                            boolean duplicates = wpg.duplicates(wpg.data.get("Monday"));
                            System.out.println("Duplicates found "+duplicates);

                            //Upload the updated program
                            HeatingSystem.setWeekProgram(wpg);

                            data1.post(new Runnable() {
                                @Override
                                public void run() {
                                    data1.setText(oldv);
                                }
                            });
                            data2.post(new Runnable() {
                                @Override
                                public void run() {
                                    data2.setText(newv);
                                }
                            });
                        } catch (Exception e) {
                            System.err.println("Error from getdata " + e);
                        }
                    }
                }).start();

            }
        });

        return view;
    }
}
