package com.example.user.getthroughapp;


import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    private ChatFragment chatFragment;
    private AlarmFragment alarmFragment;
    private DepressionFragment depressionFragment;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mMainFrame = findViewById(R.id.main_frame);
        mMainNav = findViewById(R.id.main_nav);
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chatFragment = new ChatFragment();
        alarmFragment = new AlarmFragment();
        depressionFragment = new DepressionFragment();

        setFragment(chatFragment);

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.nav_chat:
                        setFragment(chatFragment);
                        return true;

                    case R.id.nav_medSched:
                        setFragment(alarmFragment);
                        return true;

                    case R.id.nav_depBar:
                        setFragment(depressionFragment);
                        return true;

                        default:
                            return false;

                }
            }


        });

        dailyQuoteTest();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dailyQuoteTest() {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        int lastDay = settings.getInt("day", 1);

        if(lastDay!=currentDay){
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("day", currentDay);
            editor.commit();
            //code that will be started only once a day
            String[] arrayOfStrings = this.getResources().getStringArray(R.array.dailyQuotes);
            String rndQuotes = arrayOfStrings[new Random().nextInt(arrayOfStrings.length)];
            dailyQuotes(rndQuotes);
        }
    }

    private void setFragment(Fragment fragment) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();

    }

    private void dailyQuotes(String quote){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(HomeActivity.this).inflate(R.layout.daily_quotes_layout,null);
        TextView quoteCon = v.findViewById(R.id.dailyQuotes);

        quoteCon.setText(quote);

        if(quote.length()>90)
            quoteCon.setTextSize(15);

        builder.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setView(v);
        builder.show();
    }

//    private void testQuote(){
//        Date now = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(now);
//        int testTime = calendar.get(Calendar.HOUR_OF_DAY);
//        //Toast.makeText(this,testTime+"",Toast.LENGTH_LONG).show();
//        String[] arrayOfStrings = this.getResources().getStringArray(R.array.dailyQuotes);
//        String rndQuotes = arrayOfStrings[new Random().nextInt(arrayOfStrings.length)];
//
//        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        editor = sharedPref.edit();
//        editor.putInt("dayPassed", 00);
//        editor.commit();
//
//
//        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor mEditor = mPreferences.edit();
//
//        Boolean seen = mPreferences.getBoolean("isSeen", false);
//        int pass = mPreferences.getInt("dayPassed", testTime);
//
//
//
//        if(seen.equals(false)){
//            dailyQuotes(rndQuotes);
//            editor.putBoolean("isSeen", true);
//            editor.commit();
//        }
//        if(pass==00){
//            editor.putBoolean("isSeen", false);
//            return;
//        }
//    }
}
