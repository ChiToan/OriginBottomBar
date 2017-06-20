package com.example.s165097.originbottombar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class FragmentDay extends Fragment implements ListView.OnItemClickListener, View.OnClickListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    int position;
    public FloatingActionButton fabAdd;
    ArrayList<String> lItems = new ArrayList<String>();
    private ArrayAdapter<String> listAdapter;

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


        position = getArguments().getInt(POSITION_KEY);
        View view = inflater.inflate(R.layout.week_fragment_content, container, false);
        TextView textView = (TextView) view.findViewById(R.id.section_label);
        textView.setText("Dikzakken staan op dag: " + Integer.toString(position));

        final ListView addedTimes = (ListView) view.findViewById(R.id.added_times);




        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}