package com.teamrbm.alarmapp.activity;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.teamrbm.alarmapp.R;
import com.teamrbm.alarmapp.bluetooth.AlarmService;
import com.teamrbm.alarmapp.helper.C;

public class VoiceActivity extends Activity {

    Button btnSnooze, btnVoiceOff, btnTime;

    TimePicker mTimePicker;

    DatePicker mDatePicker;

    //private BluetoothAdapter btAdapter = null;
    //private BluetoothSocket btSocket = null;
    //private OutputStream outStream = null;

    private boolean isBound = false;

    private boolean isRegisterReceived = false;

    private static final int SPEECH_REQUEST_CODE = 0;

    private static AlarmService mAlarmService;

    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Other UUID
    //private static final UUID MY_UUID = UUID.fromString("0000111f-0000-1000-8000-00805F9B34FB");
    //private static final UUID MY_UUID = UUID.fromString("00001112-0000-1000-8000-00805F9B34FB");
    //private static final UUID MY_UUID = UUID.fromString("0000110a-0000-1000-8000-00805F9B34FB");

    // MAC-address of Bluetooth module (you must edit this line)
    // private static String address = "20:15:03:04:13:32";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btnSnooze = (Button) findViewById(R.id.btnOn);

        btnVoiceOff = (Button) findViewById(R.id.btnOff);

        btnTime = (Button) findViewById(R.id.btnSet);

        mDatePicker = (DatePicker) findViewById(R.id.datePicker2);

        mTimePicker = (TimePicker) findViewById(R.id.timePicker2);

        doBindService();

        //btAdapter = BluetoothAdapter.getDefaultAdapter();

    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(C.TAG, "...onResume - try connect...");

        // Set up a pointer to the remote node using it's address.
        //BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.

//        try {
//            btSocket = createBluetoothSocket(device);
//        } catch (IOException e1) {
//            errorExit("Fatal Error", "In onResume() and socket create failed: " + e1.getMessage() + ".");
//        }
//
//        // Discovery is resource intensive.  Make sure it isn't going on
//        // when you attempt to connect and pass your message.
//        btAdapter.cancelDiscovery();
//
//        // Establish the connection.  This will block until it connects.
//        Log.d(C.TAG, "...Connecting...");
//        try {
//            btSocket.connect();
//            Log.d(C.TAG, "...Connection ok...");
//        } catch (IOException e) {
//            try {
//                btSocket.close();
//            } catch (IOException e2) {
//                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
//            }
//        }
//
//        // Create a data stream so we can talk to server.
//        Log.d(C.TAG, "...Create Socket...");
//
//        try {
//            outStream = btSocket.getOutputStream();
//        } catch (IOException e) {
//            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
//        }
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//
//        Log.d(C.TAG, "...In onPause()...");
//
//        if (outStream != null) {
//            try {
//                outStream.flush();
//            } catch (IOException e) {
//                errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
//            }
//        }
//
//        try     {
//            btSocket.close();
//        } catch (IOException e2) {
//            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
//        }
//    }

    @Override
    public void onPause() {

//        if (mBluetooth != null) {
//            if (mBluetooth.isDiscovering()) {
//                mBluetooth.cancelDiscovery();
//                //mBluetooth.DLeScan(this);
//            }
//        }

        super.onPause();

    }

    @Override
    protected void onStop() {

        if (isRegisterReceived) {
            isRegisterReceived = false;
        }

        super.onStop();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        doUnbindService();

    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            try
            {
                mAlarmService = ((AlarmService.BluetoothBinder)service).getService();

                Toast.makeText(getApplicationContext(), "Service connected", Toast.LENGTH_SHORT).show();

                isRegisterReceived = true;

                mAlarmService.checkBTState();

//                btnSnooze.setOnClickListener(new OnClickListener() {
//                    public void onClick(View v) {
//
//                        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//                        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
//                                "Prompt");
//                        try {
//                            startActivityForResult(intent, SPEECH_REQUEST_CODE);
//                        } catch (ActivityNotFoundException a) {
//                            Toast.makeText(getApplicationContext(),
//                                    "Not supported",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });

                btnSnooze.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        mAlarmService.sendData("0");
                        Toast.makeText(getBaseContext(), "Turn on LED", Toast.LENGTH_SHORT).show();
                    }
                });


                btnVoiceOff.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {

                        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                                "Prompt");
                        try {
                            startActivityForResult(intent, SPEECH_REQUEST_CODE);
                        } catch (ActivityNotFoundException a) {
                            Toast.makeText(getApplicationContext(),
                                    "Not supported",
                                    Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getBaseContext(), "Turn off LED", Toast.LENGTH_SHORT).show();
                    }
                });

                btnTime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth(),
                                mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute(), 0);

                        long startTime = calendar.getTimeInMillis();

                        if (mAlarmService != null)
                            mAlarmService.setAlarmTime(startTime);
                    }
                });

            }
            catch (NullPointerException n)
            {
                Toast.makeText(VoiceActivity.this, "Activity cannot connect to service.", Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(VoiceActivity.this, "Activity connected to service.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            mAlarmService = null;

            Toast.makeText(VoiceActivity.this, "Activity disconnected from service.", Toast.LENGTH_SHORT).show();

        }
    };

    private void doBindService() {

        //the service is bound
        bindService(new Intent(VoiceActivity.this, AlarmService.class), mConnection, Context.BIND_AUTO_CREATE);

        isBound = true;

    }

    private void doUnbindService() {

        if (isBound) {
            unbindService(mConnection);
            isBound = false;
        }
    }

    // This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {

            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            Toast.makeText(getApplicationContext(), spokenText, Toast.LENGTH_SHORT).show();

            if (spokenText.equals("off")) {
                mAlarmService.sendData("2");
                mAlarmService.setAlarmTime(0);
            }
            // Do something with spokenText
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



}