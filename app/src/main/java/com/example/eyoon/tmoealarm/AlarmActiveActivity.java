package com.example.eyoon.tmoealarm;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AlarmActiveActivity extends AppCompatActivity {

    private static MediaPlayer alarmPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_active);

        Intent alarmIntent = getIntent();

        if(alarmIntent != null)
        {
            if(alarmIntent.hasExtra(AlarmActivity.ALARM_TONE_ID))
            {
                int alarmToneID = alarmIntent.getIntExtra(AlarmActivity.ALARM_TONE_ID, -1);
                float targetPercentage = alarmIntent.getFloatExtra(AlarmActivity.ALARM_VOLUME_ID, -1f);

                if(alarmPlayer != null)
                {
                    stopSound();
                }

                // Set target volume
                AudioManager volumeControl =  (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int maxVolume = volumeControl.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int targetVolume = (int) (maxVolume * targetPercentage);
                volumeControl.setStreamVolume(AudioManager.STREAM_MUSIC, targetVolume,0);

                // set alarmPlayer to start playing
                alarmPlayer = MediaPlayer.create(this, alarmToneID);
                alarmPlayer.setLooping(true);
                alarmPlayer.start();
            }
            else
            {
                TextView view = findViewById(R.id.wake_up_text);
//                view.setText("No alarm extras found in AlarmActiveActivity class.");
                view.setText(R.string.alarm_active_activity_no_extras);
            }
        }
        else
        {
            TextView view = findViewById(R.id.wake_up_text);
//            view.setText("NULL alarm intent in AlarmActiveActivity class.");
            view.setText(R.string.alarm_active_activity_null_intent);
        }
    }

    public void stopAlarmAndGoBackToHome(View view)
    {
        stopAlarm(view);
        Intent goBackToHomeIntent = new Intent(this, MainActivity.class);
        startActivity(goBackToHomeIntent);
    }
    public void stopAlarm(View view)
    {
        stopSound();
        AlarmActivity.cancelAllAlarmsSimplified();
        //TODO: Cancel all phases option!
    }
    private void stopSound()
    {
        alarmPlayer.stop();
        alarmPlayer.release();
        alarmPlayer = null;
    }
}
