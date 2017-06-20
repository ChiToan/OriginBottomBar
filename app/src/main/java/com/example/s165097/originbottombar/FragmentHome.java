package com.example.s165097.originbottombar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.Switch;

import com.triggertrap.seekarc.SeekArc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentHome extends Fragment {

    static int vtemp;
    static int ctemp;


    TextView temp;
    SeekBar seekBar;
    TextView timedate;
    SeekArc seekArc;
    SeekArc curArc;
    TextView curtemp;
    Switch switchProgram;
    ToggleButton toggleButton;

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt("vtemp", vtemp);
//        outState.putInt("ctemp", ctemp);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        vtemp = savedInstanceState.getInt("vtemp");
//        ctemp = savedInstanceState.getInt("ctemp");
//        showFlame();
//    }

    public static FragmentHome newInstance() {
        FragmentHome fragmentHome = new FragmentHome();
        //save the temp values:
        Bundle args = new Bundle();
        args.putInt("vtemp", vtemp);
        args.putInt("ctemp", ctemp);
        fragmentHome.setArguments(args);
        return fragmentHome;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Handler timerHandler;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home_fragment, container, false);

        HeatingSystem.BASE_ADDRESS = "http://wwwis.win.tue.nl/2id40-ws/6";

        temp = (TextView) view.findViewById(R.id.temp);
//        temp.setText((vtemp + 50) / 10.0 + " \u2103");
        curArc = (SeekArc) view.findViewById(R.id.curArc);
//        curArc.setProgress(ctemp);
        curtemp = (TextView) view.findViewById(R.id.curtemp);
//        curtemp.setText((ctemp + 50) / 10.0 + " \u2103");
//        switchProgram = (Switch) view.findViewById(R.id.switch_program);
        toggleButton = (ToggleButton) view.findViewById(R.id.toggleButton);

        //initial values from server dit zorgt wel voor blank fragment
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    double targetTemperature = (Double.parseDouble(HeatingSystem.get("targetTemperature")) * 10) - 50;
                    vtemp = (int) targetTemperature;
                    seekBar.setProgress(vtemp);
                    temp.setText((vtemp + 50) / 10.0 + " \u2103");
                    if (HeatingSystem.get("weekProgramState").equals("on")) {
                        toggleButton.setChecked(true);
//                        switchProgram.setChecked(true);
                    } else if (HeatingSystem.get("weekProgramState").equals("off")) {
//                        switchProgram.setChecked(false);
                        toggleButton.setChecked(false);
                    }

                } catch (Exception e) {
                    System.err.println("Error from getdata " + e);
                }
            }
        }).start();
        timedate = (TextView) view.findViewById(R.id.timeDate);

        ImageView bPlus = (ImageView) view.findViewById(R.id.bPlus);
        ImageView bMinus = (ImageView) view.findViewById(R.id.bMinus);

        final ImageView flame = (ImageView) view.findViewById(R.id.icon_flame);
        if (vtemp > ctemp) {
            flame.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        } else if (vtemp < ctemp/*current temp*/) {
            flame.setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_ATOP);
        } else if (vtemp == ctemp/*current temp*/) {
            flame.setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
        }


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    double currentTemperature = (Double.parseDouble(HeatingSystem.get("currentTemperature")) * 10) - 50;
//                    ctemp = (int) currentTemperature;
//                    curArc.setProgress(ctemp);
//                    curtemp.setText((ctemp + 50) / 10.0 + " \u2103");
//                    showFlame();
//                    timedate.setText(getResources().getString(R.string.lastupdate) + "\n" + HeatingSystem.get("day") + " " + HeatingSystem.get("time"));
//                } catch (Exception e) {
//                    System.err.println("Error from getdata " + e);
//                }
//            }
//        }).start();


        flame.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random r = new Random();
                ctemp = r.nextInt(250);
                curArc.setProgress(ctemp);
                showFlame();
                curtemp.setText((ctemp + 50) / 10.0 + " \u2103");
            }
        }));

        seekArc = (SeekArc) view.findViewById(R.id.seekArc);
        seekArc.setProgress(vtemp);
        seekArc.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(SeekArc seekArc, int i, boolean b) {
                seekBar.setProgress(i);
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {
            }

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {
                vtemp = seekArc.getProgress();
            }
        });


        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        seekBar.setProgress(vtemp);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                temp.setText((i + 50) / 10.0 + " \u2103");
                seekArc.setProgress(i);
                vtemp = i;
                showFlame();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            double targetTemp = (vtemp + 50) / 10.0;
                            HeatingSystem.put("targetTemperature", Double.toString(targetTemp));
                        } catch (Exception e) {
                            System.err.println("Error from getdata " + e);
                            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                vtemp = seekBar.getProgress();
                showFlame();
            }
        });
        bPlus.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vtemp++;
                seekBar.setProgress(vtemp);
            }
        }));
        bMinus.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vtemp--;
                seekBar.setProgress(vtemp);
            }
        }));

        toggleButton.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (isChecked) {
                                HeatingSystem.put("weekProgramState", "on");
                            } else {
                                HeatingSystem.put("weekProgramState", "off");
                            }
                        } catch (Exception e) {
                            System.err.println("Error from getdata " + e);
                            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();
            }
        });


        return view;
    }

    void showFlame() {
        ImageView flame = (ImageView) getView().findViewById(R.id.icon_flame);

        if (vtemp > ctemp) {
            flame.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        }
        if (vtemp < ctemp) {
            flame.setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_ATOP);
        }
        if (vtemp == ctemp) {
            flame.setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
        }
    }


}
