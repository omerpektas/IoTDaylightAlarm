package com.example.myestein.iotdaylightalarm;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by myestein on 24.02.2017.
 */

public class RingtonePlayingService extends Service {


    MediaPlayer media_song;
    int startId;
    boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);


        //fetch the extra string values
        String state = intent.getExtras().getString("extra");

        Log.e("Ringtone state:extra is", state);

        //this converts the extra strings from the intent
        //to start IDs, values 0 or 1
        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                media_song = MediaPlayer.create(this, R.raw.alarmclock);
                media_song.start();
                break;
            case "alarm off":
                startId = 0;
                Log.e("Ringtone extra is ", state);
                media_song.stop();
                break;
            default:
                startId = 0;
                media_song.stop();
                break;
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        // Tell the user we stopped.
        Toast.makeText(this, "on Destroy called", Toast.LENGTH_SHORT).show();
    }


    }


