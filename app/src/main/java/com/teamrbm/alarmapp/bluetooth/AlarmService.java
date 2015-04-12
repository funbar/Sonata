package com.teamrbm.alarmapp.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.widget.Toast;

import com.teamrbm.alarmapp.helper.C;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class AlarmService extends Service {

    private final IBinder mBinder = new BluetoothBinder();

    /**
     *  The main bluetooth adapter
     */
    private static BluetoothAdapter mBluetoothAdapter = null;

    private static BluetoothSocket mBluetoothSocket = null;
    private static OutputStream outStream = null;

    private static AlarmService mThis = null;

    private static long mAlarmTime;

    private Handler mAlarmHandler;

    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-address of Bluetooth module (you must edit this line)
    private static String mAddress = "20:15:03:04:13:32";

    @Override
    public void onCreate() {

        super.onCreate();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        createConnection();

        Log.e("bluetooth2", "Created");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {

        return super.onUnbind(intent);
    }

    public static AlarmService getInstance() {
        return mThis;
    }

    public class BluetoothBinder extends Binder {

        public AlarmService getService() {
            return AlarmService.this;
        }

    }

    public void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth not support", Toast.LENGTH_SHORT).show();
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                Log.d(C.TAG, "...Bluetooth ON...");
            } else {
                Log.d(C.TAG, "Turn bluetooth on");
            }
        }
    }

    public void sendData(String message) {
        byte[] msgBuffer = message.getBytes();

        Log.d(C.TAG, "...Send data: " + message + "...");

        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (mAddress.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
            msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            Log.e(C.TAG, "Bluetooth not support");
        }
    }

    public void createConnection() {
//        Set up a pointer to the remote node using it's address.
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mAddress);

//        Two things are needed to make a connection:
//           A MAC address, which we got above.
//           A Service ID or UUID.  In this case we are using the
//             UUID for SPP.

        try {
            mBluetoothSocket = createBluetoothSocket(device);
        } catch (IOException e1) {
            Log.e(C.TAG, "In onResume() and socket create failed: " + e1.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        mBluetoothAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(C.TAG, "...Connecting...");
        try {
            mBluetoothSocket.connect();
            Log.d(C.TAG, "...Connection ok...");
        } catch (IOException e) {
            try {
                mBluetoothSocket.close();
            } catch (IOException e2) {
                Log.e(C.TAG, "In onResume() and socket create failed: ");
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(C.TAG, "...Create Socket...");

        try {
            outStream = mBluetoothSocket.getOutputStream();
        } catch (IOException e) {
            Log.e(C.TAG, "In onResume() and socket create failed: ");
        }
    }


    public BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(C.TAG, "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    public void setAlarmTime(long ms)
    {
        mAlarmTime = ms;

        final long alarmTime = ms;

        Date d = new Date();

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        long current_ms = d.getTime();
        final long alarm_ms = ms + year + month + day;

        long diff_ms = alarm_ms - current_ms;

        Log.e(C.TAG, "DIFF: " + diff_ms);

        mAlarmHandler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                sendData("1");
            }
        };

        mAlarmHandler.postDelayed(r, diff_ms);

    }
}
