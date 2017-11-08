package com.example.myestein.iotdaylightalarm;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MainActivity extends AppCompatActivity {

    static String MQTTHOST ="tcp://m10.cloudmqtt.com:12342";
    static String USERNAME ="fegdyhsu";
    static String PASSWORD ="0tXvpJ4VtFpP";
    String topicStr = "daylight";

    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;
    int pHour;
    int pMinute;
    int Music;
    int calHour;
    int calMinute;
    int realTime;
    Button date;
    Button time;
    TextView dateTxt;
    TextView timeTxt;
    TextView alarmsetText;
    NumberPicker ltp;

    ImageButton func1;
    ImageButton func2;
    ImageButton func3;
    ImageButton func4;
    String func;
    String LTimer;
    String Alarm;
    String hour_string;
    String minute_string;
    Button setime;
    Button setalarm;
    Button resetalarm;
    Switch music;

    //To make alarm manager
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    Context context;
    PendingIntent pending_intent;
    MqttAndroidClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this,"connected!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity.this,"connection failed", Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        this.context = this;
        final Calendar c = Calendar.getInstance();
        // initialize alarm manager
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        final Intent my_intent = new Intent(this.context, com.example.myestein.iotdaylightalarm.Alarm_Receiver.class);

        dateTxt = (TextView) findViewById(R.id.DateView);
        timeTxt = (TextView) findViewById(R.id.TimeView);
        alarmsetText = (TextView) findViewById(R.id.AlarmSetText);

        func1 = (ImageButton) findViewById(R.id.f1);
        func2 = (ImageButton) findViewById(R.id.f2);
        func3 = (ImageButton) findViewById(R.id.f3);
        func4 = (ImageButton) findViewById(R.id.f4);

        func1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                func = "1";
                Toast.makeText(MainActivity.this, "The function " + func + " has been picked.", Toast.LENGTH_LONG).show();
            }
        });
        func2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                func = "2";
                Toast.makeText(MainActivity.this, "The function " + func + " has been picked.", Toast.LENGTH_LONG).show();
            }
        });
        func3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                func = "3";
                Toast.makeText(MainActivity.this, "The function " + func + " has been picked.", Toast.LENGTH_LONG).show();
            }
        });
        func4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                func = "4";
                Toast.makeText(MainActivity.this, "The function " + func + " has been picked.", Toast.LENGTH_LONG).show();
            }
        });
        ltp = (NumberPicker) findViewById(R.id.EnlightenmentTime);
        ltp.setMinValue(0);
        ltp.setMaxValue(30);
        ltp.setWrapSelectorWheel(false);
        setime = (Button) findViewById(R.id.setTime);
        setime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "The Enlightenment Time is " + ltp.getValue(), Toast.LENGTH_LONG).show();
                if (ltp.getValue() < 10) {
                    LTimer = "0" + String.valueOf(ltp.getValue());
                }
                else {
                    LTimer = String.valueOf(ltp.getValue());
                }
            }
        });
        date = (Button) findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateTxt.setText("Date :" + dayOfMonth + "/" + (monthOfYear +1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        time = (Button) findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHour = c.get(Calendar.HOUR);
                mMinute = c.get(Calendar.MINUTE);
                TimePickerDialog tpd = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeTxt.setText("Time :" + hourOfDay + ":" + minute);
                        hour_string = String.valueOf(hourOfDay);
                        minute_string = String.valueOf(minute);
                        pHour = hourOfDay;
                        pMinute = minute;
                    }
                }, mHour, mMinute, false);
                tpd.show();
            }
        });

        music = (Switch) findViewById(R.id.music);
        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    Music = 1;
                    Toast.makeText(MainActivity.this, "Music On!! ", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Music = 0;
                    Toast.makeText(MainActivity.this, "Music Off!! ", Toast.LENGTH_LONG).show();
                }
            }
        });

        setalarm = (Button) findViewById(R.id.AlarmSet);
        setalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alarm = "1";
                if (Music == 1) {
                    alarmsetText.setText(" Alarm Set to : " + hour_string + ":" + minute_string + " with music!");
                    final int realHour = c.get(Calendar.HOUR_OF_DAY);
                    final int realMinute = c.get(Calendar.MINUTE);
                    int difHour = pHour - realHour;
                    if (difHour != 0) {
                        calHour = difHour * 60;
                    }
                    else {
                        calHour = 0;
                    }
                    int difMinute = pMinute - realMinute;
                    calMinute = difMinute + calHour;
                    realTime = (calMinute * 60) * 1000;
                    for (int i = 1; i<=realTime; i++);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //put in extra string into my_intent
                            //tells the clock that you pressed the "alarm on" button
                            my_intent.putExtra("extra", "alarm on");


                            //create a pending intent that delays the intent
                            //until the specified calendar time
                            pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0,
                                    my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                            //set the alarm manager
                            alarm_manager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                                    pending_intent);

                            String topic = topicStr;
                            String message = "$" + Alarm + func + LTimer;
                            try {
                                client.publish(topic, message.getBytes(), 0, false);
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }
                    }, realTime);


                }
                else if (Music == 0) {
                    alarmsetText.setText(" Alarm Set to : " + hour_string + ":" + minute_string + " without music!");
                    final int realHour = c.get(Calendar.HOUR_OF_DAY);
                    final int realMinute = c.get(Calendar.MINUTE);
                    int difHour = pHour - realHour;
                    if (difHour != 0) {
                        calHour = difHour * 60;
                    }
                    else {
                        calHour = 0;
                    }
                    int difMinute = pMinute - realMinute;
                    calMinute = difMinute + calHour;
                    realTime = (calMinute * 60) * 1000;
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            my_intent.putExtra("extra", "alarm off");
                            pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0,
                                    my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarm_manager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                                    pending_intent);
                            String topic = topicStr;
                            String message = "$" + Alarm + func + LTimer;
                            try {
                                client.publish(topic, message.getBytes(), 0, false);
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }
                    }, realTime);
                }

            }
        });

        resetalarm = (Button) findViewById(R.id.AlarmReset);
        resetalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alarm = "0";
                alarmsetText.setText("Alarm Stop!! You can reset the alarm again! ");

                alarm_manager.cancel(pending_intent);

                //put extra string to my_intent
                //tells the clock that you pressed the "alarm off" button
                my_intent.putExtra("extra", "alarm off");
                ltp.setValue(0);
                realTime = 0;
                //stop the ringtone
                sendBroadcast(my_intent);
                String topic = topicStr;
                String message = "$" + Alarm + func + LTimer;
                try {
                    client.publish(topic, message.getBytes(), 0,false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
}
}