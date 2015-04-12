package com.teamrbm.alarmapp.application;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.teamrbm.alarmapp.bluetooth.AlarmService;
import com.teamrbm.alarmapp.helper.C;

/**
 * Created by brandon.burton on 4/10/15.
 */
public class AlarmApplication extends Application
{
    //are we bound to the service
    private boolean isBound = false;

    //our listening service
    private AlarmService mShadowService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            mShadowService = ((AlarmService.BluetoothBinder)service).getService();

            Log.e(C.TAG, "Application connected to service.");

            //Toast.makeText(this, "Activity connected to service.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            doUnbindService();

            mShadowService = null;

            //Toast.makeText(this, "Activity disconnected from service.", Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    public void onCreate() {

        super.onCreate();

        doBindService();

    }

    @Override
    public void onTerminate() {

//        if (mShadowService != null)
//            stopService(mShadowService);

//        mShadowReceiver.stopScan();

//        unregisterReceiver(mShadowReceiver);
        unbindService(mConnection);

        Log.e(C.TAG, "Terminated");

        super.onTerminate();

    }

    private void doBindService() {

        boolean start = false;

        Intent i = new Intent(getApplicationContext(), AlarmService.class);
        startService(i);
        //the service is bound
        start = bindService(new Intent(this, AlarmService.class), mConnection, Context.BIND_AUTO_CREATE);

        if (!start) {
            Toast.makeText(this, "Bind to BluetoothLeService failed", Toast.LENGTH_SHORT).show();
            //finish();
        }

        //Toast.makeText(SSConnectActivity.this, "Bound from activity", Toast.LENGTH_SHORT).show();

        //our service bound
        isBound = true;

    }

    private void doUnbindService() {

        if (isBound) {
            unbindService(mConnection);
            isBound = false;
            //Toast.makeText(SSConnectActivity.this, "Unbound from activity", Toast.LENGTH_SHORT).show();
        }
    }
}
