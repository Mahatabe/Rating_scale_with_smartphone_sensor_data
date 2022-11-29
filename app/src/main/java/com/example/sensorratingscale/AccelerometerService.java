package com.example.sensorratingscale;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class AccelerometerService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor mySensor;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    private void setContentView(int activity_main) {
    }




    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, mySensor, 600000000,600000000);

        //Log.d("Accel","Running");
        //here u should make your service foreground so it will keep working even if app closed
        return Service.START_STICKY;

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Constants.acc_x = event.values[0];
            Constants.acc_y = event.values[1];
            Constants.acc_z = event.values[2];

            Log.d("AccelerometerData",Constants.acc_x +" "+ Constants.acc_y +" "+ Constants.acc_z);




        }
    }
}
