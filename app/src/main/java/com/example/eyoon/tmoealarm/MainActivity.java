package com.example.eyoon.tmoealarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String ALARM_HOUR = "com.example.eyoon.tmoealarm.MainActivity.ALARM_HOUR";
    public static final String ALARM_MINUTE = "com.example.eyoon.tmoealarm.MainActivity.ALARM_MINUTE";
    public static final String ALARM_AMPM = "com.example.eyoon.tmoealarm.MainActivity.ALARM_AMPM";

    public static final int AM_ID = R.string.AM;
    public static final int PM_ID = R.string.PM;
    public static final String AM = "AM"; // TODO: How to get from resource files?
    public static final String PM = "PM"; // TODO: How to get from resource files?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /**
     * Start the alarm activity with the alarm's data.
     */
    public void createNewAlarm(View view)
    {
        Intent createAlarmIntent = new Intent(this, AlarmActivity.class);
        Spinner hourSpinner = findViewById(R.id.hour_spinner);
        Spinner minuteSpinner = findViewById(R.id.minute_spinner);
        Spinner ampmSpinner = findViewById(R.id.ampm_spinner);

        String hourText = hourSpinner.getSelectedItem().toString();
        String minuteText = minuteSpinner.getSelectedItem().toString();
        String ampm = ampmSpinner.getSelectedItem().toString();

        int hour = Integer.parseInt(hourText);
        int minute = Integer.parseInt(minuteText);
        int ampmID;

        if(ampm.equals(AM))
        {
            ampmID = AM_ID;
        }
        else
        {
            ampmID = PM_ID;
        }

        createAlarmIntent.putExtra(this.ALARM_HOUR, hour);
        createAlarmIntent.putExtra(this.ALARM_MINUTE, minute);
        createAlarmIntent.putExtra(this.ALARM_AMPM, ampmID);
        startActivity(createAlarmIntent);
    }
}
