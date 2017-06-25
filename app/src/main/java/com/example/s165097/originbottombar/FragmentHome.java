package com.example.s165097.originbottombar;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.triggertrap.seekarc.SeekArc;

import static java.lang.Thread.currentThread;

public class FragmentHome extends Fragment {

    int vtemp;
    int ctemp;


    TextView temp;
    SeekBar seekBar;
    TextView timedate;
    SeekArc seekArc;
    SeekArc curArc;
    TextView curtemp;
    Switch switchProgram;
    ImageView flame;

    public static FragmentHome newInstance() {
        FragmentHome fragmentHome = new FragmentHome();
        //save the temp values:
        return fragmentHome;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        switchProgram = (Switch) view.findViewById(R.id.switch_program);
//        toggleButton = (ToggleButton) view.findViewById(R.id.toggleButton);
        timedate = (TextView) view.findViewById(R.id.timeDate);

        //initial values from server dit zorgt wel voor blank fragment
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    double targetTemperature = (Double.parseDouble(HeatingSystem.get("targetTemperature")) * 10) - 50;
                    vtemp = (int) targetTemperature;
                    double currentTemperature = (Double.parseDouble(HeatingSystem.get("currentTemperature")) * 10) - 50;
                    ctemp = (int) currentTemperature;
                    final String weekProgramState = HeatingSystem.get("weekProgramState");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                seekBar.setProgress(vtemp);
                                temp.setText((vtemp + 50) / 10.0 + " \u2103");
                                if (weekProgramState.equals("on")) {
//                                    toggleButton.setChecked(true);
                                    switchProgram.setChecked(true);
                                } else if (weekProgramState.equals("off")) {
                                    switchProgram.setChecked(false);
//                                    toggleButton.setChecked(false);
                                }
                                curArc.setProgress(ctemp);
                                curtemp.setText((ctemp + 50) / 10.0 + " \u2103");
                                showFlame();

                            } catch (Exception e) {

                            }
                        }
                    });

                } catch (Exception e) {
                    System.err.println("Error from getdata " + e);
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!currentThread().isInterrupted()) {
                        Thread.sleep(1000);
                        ctemp = (int) (Double.parseDouble(HeatingSystem.get("currentTemperature")) * 10) - 50;
                        final String getTimeDate = (HeatingSystem.get("day") + " " + HeatingSystem.get("time"));
                        vtemp = (int) (Double.parseDouble(HeatingSystem.get("targetTemperature"))*10)-50;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                curArc.setProgress(ctemp);
                                curtemp.setText((ctemp + 50) / 10.0 + " \u2103");
                                showFlame();
                                timedate.setText(getResources().getString(R.string.lastupdate) + getTimeDate);
                                seekBar.setProgress(vtemp);
                            }
                        });
                    }
                } catch (Exception e) {

                }
            }
        }).start();


        ImageView bPlus = (ImageView) view.findViewById(R.id.bPlus);
        ImageView bMinus = (ImageView) view.findViewById(R.id.bMinus);



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
                            Thread.sleep(100);
                        } catch (Exception e) {
                            System.err.println("Error from getdata " + e);
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

        switchProgram.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
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
                        }
                    }
                }).start();
            }
        });


        return view;
    }

    void showFlame() {
        ImageView flame = (ImageView) getView().findViewById(R.id.icon_flame);
        ImageView snow = (ImageView) getView().findViewById(R.id.icon_snow);
        ImageView temp = (ImageView) getView().findViewById(R.id.icon_temp);

        if (vtemp > ctemp) {
//            f.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            flame.setVisibility(View.VISIBLE);
            snow.setVisibility(View.INVISIBLE);
            temp.setVisibility(View.INVISIBLE);
        }
        if (vtemp < ctemp) {
            flame.setVisibility(View.INVISIBLE);
            snow.setVisibility(View.VISIBLE);
            temp.setVisibility(View.INVISIBLE);
//            f.setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_ATOP);
        }
        if (vtemp == ctemp) {
            flame.setVisibility(View.INVISIBLE);
            snow.setVisibility(View.INVISIBLE);
            temp.setVisibility(View.VISIBLE);
//            f.setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
        }
    }



}
