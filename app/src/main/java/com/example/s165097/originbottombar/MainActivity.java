package com.example.s165097.originbottombar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public static int vtemp;
    public static int ctemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content);
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                selectedFragment = FragmentHome.newInstance();
                                break;
                            case R.id.navigation_program:
                                selectedFragment = FragmentWeek.newInstance();
                                break;
                            case R.id.navigation_settings:
                                selectedFragment = FragmentSettings.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.content, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, FragmentHome.newInstance());
        transaction.commit();
    }


//    private TextView mTextMessage;
//
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
//                    switchToFragmentHome();
//                    return true;
//                case R.id.navigation_program:
//                    mTextMessage.setText(R.string.title_program);
//                    switchToFragmentWeek();
//                    return true;
//                case R.id.navigation_settings:
//                    mTextMessage.setText(R.string.title_settings);
//                    switchToFragmentSettings();
//                    return true;
//            }
//            return false;
//        }
//
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        mTextMessage = (TextView) findViewById(R.id.message);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//    }
//
//    public void switchToFragmentHome() {
//        FragmentManager manager = getSupportFragmentManager();
//        manager.beginTransaction().replace(R.id.home_fragment, new Fragment()).commit();
//    }
//
//    public void switchToFragmentWeek() {
//        FragmentManager manager = getSupportFragmentManager();
//        manager.beginTransaction().replace(R.id.home_fragment, new Fragment()).commit();
//    }
//
//    public void switchToFragmentSettings() {
//        FragmentManager manager = getSupportFragmentManager();
//        manager.beginTransaction().replace(R.id.home_fragment, new Fragment()).commit();
//    }

}