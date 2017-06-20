package com.example.s165097.originbottombar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class FragmentSettings extends Fragment {

    Button getdata, putdata;
    TextView data1, data2;
    String getParam, oldv, newv;

    public static FragmentSettings newInstance() {
        return new FragmentSettings();
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
                            //HeatingSystem.setWeekProgram(wpg);

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
