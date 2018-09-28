package com.example.eyoon.tmoealarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.media.MediaPlayer;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {

        // TODO: EYYY Make some noise!
        Intent goToAlarmActiveActivityIntent = new Intent(context, AlarmActiveActivity.class);

        // Relay the AlarmActivity's intent to the alarm
        // TODO: Does this actually work?
        int alarmToneIDExtra = intent.getIntExtra(AlarmActivity.ALARM_TONE_ID, -1);
        float volumeLevel = intent.getFloatExtra(AlarmActivity.ALARM_VOLUME_ID, -1f);
        goToAlarmActiveActivityIntent.putExtra(AlarmActivity.ALARM_TONE_ID, alarmToneIDExtra);
        goToAlarmActiveActivityIntent.putExtra(AlarmActivity.ALARM_VOLUME_ID, volumeLevel);

        context.startActivity(goToAlarmActiveActivityIntent);
    }
}
