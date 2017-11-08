package com.example.myestein.iotdaylightalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by myestein on 02.03.2017.
 */

public class Alarm_Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("We are in the receiver.", "Yes!");

        //fetch extra strings from the intent
        String get_your_string = intent.getExtras().getString("extra");

        Log.e("What is the key? ", get_your_string);

        Intent service_intent;
        service_intent = new Intent(context, RingtonePlayingService.class);

        //pass the extra string from the Main Activity to the Ringtone Playing Service
        service_intent.putExtra("extra", get_your_string);

        //start the ringtone service
        context.startService(service_intent);

    }
}
