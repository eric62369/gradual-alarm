package com.example.eyoon.tmoealarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmActivity extends AppCompatActivity {

    // TODO: 4 Alarms?
    private static AlarmManager alarmManager;

    private static PendingIntent pendingAlarmIntent;
    private static PendingIntent phase1PendingIntent;
    private static PendingIntent phase2PendingIntent;
    private static PendingIntent phase3PendingIntent;

    public static final String ALARM_TONE_ID = "com.example.eyoon.tmoealarm.AlarmActivity.ALARM";
    public static final String ALARM_VOLUME_ID = "com.example.eyoon.tmoealarm.AlarmActivity.VOLUME";

    private static final int ALARM_ID = 0;
    private static final int PHASE1_ID = 1;
    private static final int PHASE2_ID = 2;
    private static final int PHASE3_ID = 3;

    private static final int PHASE_1_MINUTES_BEFORE = 60;
    private static final int PHASE_2_MINUTES_BEFORE = 30;
    private static final int PHASE_3_MINUTES_BEFORE = 10;

    private static final float PHASE_1_VOLUME = 0.4f;
    private static final float PHASE_2_VOLUME = 0.4f;
    private static final float PHASE_3_VOLUME = 0.5f;
    private static final float ALARM_WAKEUP_VOLUME = 0.6f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Intent alarmDataIntent = getIntent();

        if(alarmDataIntent != null)
        {
            parseAndSetAlarm(alarmDataIntent);
        }
    }
    @Override
    protected void onPause()
    {
        super.onPause();
//        cancelAllAlarms(); // TODO: Kinda bad solution, probably can't repeat alarms at all.
    }

    public void cancelAllAlarms()
    {
        if(alarmManager != null)
        {
            alarmManager.cancel(pendingAlarmIntent);
            alarmManager.cancel(phase1PendingIntent);
            alarmManager.cancel(phase2PendingIntent);
            alarmManager.cancel(phase3PendingIntent);

            Toast alarmCancelledToast = Toast.makeText(this, R.string.alarm_cancelled, Toast.LENGTH_SHORT);
            alarmCancelledToast.show();

            Intent goToHomeIntent = new Intent(this, MainActivity.class);
            startActivity(goToHomeIntent);
        }
    }
    public static void cancelAllAlarmsSimplified()
    {
        if(alarmManager != null)
        {
            alarmManager.cancel(pendingAlarmIntent);
            alarmManager.cancel(phase1PendingIntent);
            alarmManager.cancel(phase2PendingIntent);
            alarmManager.cancel(phase3PendingIntent);
        }
    }
    public void cancelAllAlarmsOnClick(View view)
    {
        cancelAllAlarms();
    }
    private void parseAndSetAlarm(Intent alarmDataIntent)
    {
        int hour = alarmDataIntent.getIntExtra(MainActivity.ALARM_HOUR, -1);
        int minute = alarmDataIntent.getIntExtra(MainActivity.ALARM_MINUTE, -1);
        int ampm = alarmDataIntent.getIntExtra(MainActivity.ALARM_AMPM, -1);
        boolean isAM = (ampm == MainActivity.AM_ID);

        setAlarm(hour, minute, isAM);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setAlarm(int hour, int minute, boolean isAM)
    {
        // Change the text on the XML
        setOnScreenText(hour, minute, isAM);

        // Get the system's alarm manager
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Create intents for the alarm to create
        Intent alarmReceiverIntent = new Intent(this, AlarmReceiver.class);
        Intent phase1ReceiverIntent = new Intent(this, AlarmReceiver.class);
        Intent phase2ReceiverIntent = new Intent(this, AlarmReceiver.class);
        Intent phase3ReceiverIntent = new Intent(this, AlarmReceiver.class);

        // Add the respective music media R to each intent
        alarmReceiverIntent.putExtra(ALARM_TONE_ID, R.raw.wakeup);
        phase1ReceiverIntent.putExtra(ALARM_TONE_ID, R.raw.phase1);
        phase2ReceiverIntent.putExtra(ALARM_TONE_ID, R.raw.phase2);
        phase3ReceiverIntent.putExtra(ALARM_TONE_ID, R.raw.phase3);
        // Add the respective volume for each intent
        alarmReceiverIntent.putExtra(ALARM_VOLUME_ID, ALARM_WAKEUP_VOLUME);
        phase1ReceiverIntent.putExtra(ALARM_VOLUME_ID, PHASE_1_VOLUME);
        phase2ReceiverIntent.putExtra(ALARM_VOLUME_ID, PHASE_2_VOLUME);
        phase3ReceiverIntent.putExtra(ALARM_VOLUME_ID, PHASE_3_VOLUME);

        // Get pending broadcast intents for the respective alarms
        pendingAlarmIntent = PendingIntent.getBroadcast(this, ALARM_ID, alarmReceiverIntent, PendingIntent.FLAG_ONE_SHOT);
        phase1PendingIntent = PendingIntent.getBroadcast(this, PHASE1_ID, phase1ReceiverIntent, PendingIntent.FLAG_ONE_SHOT);
        phase2PendingIntent = PendingIntent.getBroadcast(this, PHASE2_ID, phase2ReceiverIntent, PendingIntent.FLAG_ONE_SHOT);
        phase3PendingIntent = PendingIntent.getBroadcast(this, PHASE3_ID, phase3ReceiverIntent, PendingIntent.FLAG_ONE_SHOT);

        // Get alarm trigger times for all alarms
        long triggerTime = calculateAlarmTimeMilliseconds(hour, minute, isAM);
        long phase1Time = triggerTime - (PHASE_1_MINUTES_BEFORE * 60 * 1000);
        long phase2Time = triggerTime - (PHASE_2_MINUTES_BEFORE * 60 * 1000);
        long phase3Time = triggerTime - (PHASE_3_MINUTES_BEFORE * 60 * 1000);

        // Set all alarms
        // TODO: Try an Elapsed_RT_WAKEUP?
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingAlarmIntent);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, phase1Time, phase1PendingIntent);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, phase2Time, phase2PendingIntent);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, phase3Time, phase3PendingIntent);
//        alarmManager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime() + 5 * 1000, pendingAlarmIntent); // WORKS!
    }

    private void setOnScreenText(int hour, int minute, boolean isAM)
    {
        TextView alarmText = findViewById(R.id.alarm_time_text);
        String minuteText = "" + minute;

        // Make minutes look like clock time.
        if(minuteText.length() < 2)
        {
            minuteText = "0" + minute;
        }

        String alarmTimeText = hour + " : " + minuteText + " ";
        if(isAM)
        {
            alarmTimeText += MainActivity.AM;
        }
        else
        {
            alarmTimeText += MainActivity.PM;
        }

        alarmText.setText(alarmTimeText);
    }
    private long calculateAlarmTimeMilliseconds(int hour, int minute, boolean isAM)
    {
        if(hour == 12)
        {
            hour -= 12;
        }
        if(!isAM)
        {
            hour += 12;
        }

        Calendar referenceNowCalendar = Calendar.getInstance();
        Calendar alarmTimeCalendar = Calendar.getInstance();
        alarmTimeCalendar.setTimeInMillis(System.currentTimeMillis());

        alarmTimeCalendar.set(Calendar.HOUR_OF_DAY, hour);
        alarmTimeCalendar.set(Calendar.MINUTE, minute);
        alarmTimeCalendar.set(Calendar.SECOND, 0);

        // Is the time already past?
        if(alarmTimeCalendar.before(referenceNowCalendar))
        {
            alarmTimeCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return alarmTimeCalendar.getTimeInMillis();
//        if(!isAM)
//        {
//            hour += 12;
//        }
//        long timeInMilliseconds = 0;
//        // minutes add
//        timeInMilliseconds += (minute * 60 * 1000);
//        // hours add
//        timeInMilliseconds += (hour * 60 * 60 * 1000);
//        return timeInMilliseconds;
    }
}
