package com.example.s165097.originbottombar;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
    int dayTempVal, nightTempVal;
    public static int savedDayTempVal, savedNightTempVal;
    SeekBar seekBarDay, seekBarNight;

    public static FragmentSettings newInstance() {
        FragmentSettings fragmentSettings = new FragmentSettings();
        Bundle args = new Bundle();
        args.putInt("savedDayTempVal", savedDayTempVal);
        args.putInt("savedNightTempVal", savedNightTempVal);
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

        final TextView dayTemp = (TextView) view.findViewById(R.id.day_temp);
        final TextView nightTemp = (TextView) view.findViewById(R.id.night_temp);
        final TextView savedDayTemp = (TextView) view.findViewById(R.id.day_text);
        final TextView savedNightTemp = (TextView) view.findViewById(R.id.night_text);
        final Button saveDay = (Button) view.findViewById(R.id.saveDay);
        final Button saveNight = (Button) view.findViewById(R.id.saveNight);
        ImageButton bPlusDay = (ImageButton) view.findViewById(R.id.bPlusDay);
        ImageButton bPlusNight = (ImageButton) view.findViewById(R.id.bPlusNight);
        ImageButton bMinusDay = (ImageButton) view.findViewById(R.id.bMinusDay);
        ImageButton bMinusNight = (ImageButton) view.findViewById(R.id.bMinusNight);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    savedDayTempVal = (int) (Double.parseDouble(HeatingSystem.get("dayTemperature")) * 10) - 50;
                    seekBarDay.setProgress(savedDayTempVal);

                } catch (Exception e) {
//                    Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).start();

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                savedDayTemp.setText(getString(R.string.daytemp));
                savedNightTemp.setText(getString(R.string.nighttemp));

                try {
                    savedDayTempVal = (int) (Double.parseDouble(HeatingSystem.get("dayTemperature")) * 10) - 50;
                    savedNightTempVal = (int) (Double.parseDouble(HeatingSystem.get("nightTemperature")) * 10) - 50;
                    seekBarNight.setProgress(savedNightTempVal);
                    seekBarDay.setProgress(savedDayTempVal);


                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).start();

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
        seekBarDay = (SeekBar) view.findViewById(R.id.seekBarDay);
        seekBarDay.setProgress(savedDayTempVal);
        seekBarDay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                dayTemp.setText((i + 50) / 10.0 + " \u2103");
                seekBarDay.setProgress(i);
                dayTempVal = i;
                setButtonColor(saveDay, savedDayTempVal, dayTempVal);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                dayTempVal = seekBar.getProgress();
                setButtonColor(saveDay, savedDayTempVal, dayTempVal);
            }
        });


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
        seekBarNight = (SeekBar) view.findViewById(R.id.seekBarNight);
        seekBarNight.setProgress(savedNightTempVal);
        seekBarNight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                nightTemp.setText((i + 50) / 10.0 + " \u2103");
                seekBarNight.setProgress(i);
                nightTempVal = i;
                setButtonColor(saveNight, savedNightTempVal, nightTempVal);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                nightTempVal = seekBar.getProgress();
                setButtonColor(saveNight, savedNightTempVal, nightTempVal);
            }
        });

        saveDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HeatingSystem.put("dayTemperature", Double.toString((dayTempVal + 50) / 10.0));
                            savedDayTemp.setText(getString(R.string.daytemp) + " " + (dayTempVal + 50) / 10.0 + " \u2103");
                            setButtonColor(saveDay,1,1);
                            savedDayTempVal = dayTempVal;
//                            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
//                            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();

            }
        });
        saveNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HeatingSystem.put("nightTemperature", Double.toString((nightTempVal + 50) / 10.0));
                            savedNightTemp.setText(getString(R.string.nighttemp) + " " + (nightTempVal + 50) / 10.0 + " \u2103");
                            setButtonColor(saveNight,1,1);
                            savedNightTempVal = nightTempVal;
//                            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
//                            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();
            }
        });


        getdata = (Button) view.findViewById(R.id.getdata);
        putdata = (Button) view.findViewById(R.id.putdata);
        data1 = (TextView) view.findViewById(R.id.data1);
        data2 = (TextView) view.findViewById(R.id.data2);

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
                            System.err.println("Error from getdata " + e);
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
                            System.out.println("Duplicates found " + duplicates);

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

    void setButtonColor(Button b, int saved, int progress) {
        if (saved == progress) {
            b.setEnabled(false);
        } else {
            b.setEnabled(true);
        }
    }
}
