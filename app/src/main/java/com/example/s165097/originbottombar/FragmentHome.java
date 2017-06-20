package com.example.s165097.originbottombar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.triggertrap.seekarc.SeekArc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class FragmentHome extends Fragment {

    static int vtemp;
    static int ctemp;


    TextView temp;
    SeekBar seekBar;
    TextView timedate;
    SeekArc seekArc;
    SeekArc curArc;
    TextView curtemp;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home_fragment, container, false);

        ImageView bPlus = (ImageView) view.findViewById(R.id.bPlus);
        ImageView bMinus = (ImageView) view.findViewById(R.id.bMinus);
        ImageView flameTest = (ImageView) view.findViewById(R.id.icon_flame);
        temp = (TextView) view.findViewById(R.id.temp);
        temp.setText((vtemp+50)/10.0 + " \u2103");

        curArc = (SeekArc) view.findViewById(R.id.curArc);
        curArc.setProgress(ctemp);
        curtemp = (TextView) view.findViewById(R.id.curtemp);
        curtemp.setText((ctemp+50)/10.0 + " \u2103");

        DateFormat df = new SimpleDateFormat("EEEE HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        timedate = (TextView) view.findViewById(R.id.timeDate);
        timedate.setText(getResources().getString(R.string.lastupdate) + "\n" + date);

        flameTest.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random r = new Random();
                ctemp = r.nextInt(250);
                curArc.setProgress(ctemp);
                showFlame();
                curtemp.setText((ctemp+50)/10.0 + " \u2103");
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
                temp.setText((i+50)/10.0 + " \u2103");
                seekArc.setProgress(i);
                vtemp = i;
                showFlame();
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


        return view;

    }

    void showFlame() {
        ImageView flame = (ImageView) getView().findViewById(R.id.icon_flame);

        if (vtemp > ctemp) {
            flame.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        }
        if (vtemp < ctemp/*current temp*/) {
            flame.setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_ATOP);
        }
        if (vtemp == ctemp/*current temp*/) {
            flame.setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public static int getThemeAccentColor(final Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        return value.data;
    }

    public static int getThemePrimaryColor(final Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
        return value.data;

    }


}
