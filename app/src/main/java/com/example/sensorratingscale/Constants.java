package com.example.sensorratingscale;

public class Constants {
    static final int LOCATION_SERVICE_ID = 175 ;
    static final String ACTION_START_LOCATION_SERVICE = "startLocationService";
    static final String ACTION_STOP_LOCATION_SERVICE = "stopLocationService";

    static double latitude = 0.000000;
    static double longitude = 0.000000;

    static double acc_x = 0.000000;
    static double acc_y = 0.000000;
    static double acc_z = 0.000000;

    static double gyro_x = 0.000000;
    static double gyro_y = 0.000000;
    static double gyro_z = 0.000000;


    static long gpsSetInterval = 0000000000;
    static long gpsSetFastestInterval = 0000000000;

    static long sqliteHandler = 10;
    static long firebaseHandler = 120;






    //static final String ACTION_START_ACCELEROMETER_SERVICE = "onStartCommand";

}
