package com.example.sensorratingscale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String SENSORGPS_LATITUDE = "SENSORGPS_LATITUDE";
    public static final String SENSORGPS_LONGITUDE = "SENSORGPS_LONGITUDE";

    public static final String ACCELEROMETER_X = "ACCELEROMETER_X";
    public static final String ACCELEROMETER_Y = "ACCELEROMETER_Y";
    public static final String ACCELEROMETER_Z = "ACCELEROMETER_Z";

    public static final String GYROSCOPE_X = "GYROSCOPE_X";
    public static final String GYROSCOPE_Y = "GYROSCOPE_Y";

    public static final String GYROSCOPE_Z = "GYROSCOPE_Z";

    public static final String DATE_TIME = "DATE_TIME";

    public static final String  SENSORDATA =" SENSORDATA";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "sensor.db", null, 4);
    }

    //function for creating the table  in sqlite
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableSensor="CREATE TABLE " + SENSORDATA + " (" + SENSORGPS_LATITUDE + " REAL , "
                + SENSORGPS_LONGITUDE + " REAL," + ACCELEROMETER_X + " REAL," + ACCELEROMETER_Y + " REAL ,"
                + ACCELEROMETER_Z + " REAL," + GYROSCOPE_X + " REAL,"
                + GYROSCOPE_Y + " REAL," + GYROSCOPE_Z  +"REAL,"
                + DATE_TIME + " REAL)" ;

          Log.d("DATABASE",createTableSensor);

        db.execSQL(createTableSensor);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //function for inserting value in the sqlite database

    public boolean addsensordata() {
        SQLiteDatabase dbAccelerometer = this.getWritableDatabase();

        ContentValues contentValuessesnordata = new ContentValues();

        contentValuessesnordata.put(SENSORGPS_LATITUDE, Constants.latitude);
        contentValuessesnordata.put(SENSORGPS_LONGITUDE, Constants.longitude);

        contentValuessesnordata.put(ACCELEROMETER_X, Constants.acc_x);
        contentValuessesnordata.put(ACCELEROMETER_Y, Constants.acc_y);
        contentValuessesnordata.put(ACCELEROMETER_Z, Constants.acc_z);

        contentValuessesnordata.put(GYROSCOPE_X, Constants.gyro_x);
        contentValuessesnordata.put(GYROSCOPE_Y, Constants.gyro_y);
        contentValuessesnordata.put(GYROSCOPE_Z, Constants.gyro_z);

        contentValuessesnordata.put(DATE_TIME, java.text.DateFormat.getDateTimeInstance().format(new Date()));

        long insertsensordata = dbAccelerometer.insert( SENSORDATA, null, contentValuessesnordata);

        if (insertsensordata == -1) {
            return false;
        } else {
            return true;
        }


    }

    //function for pushing the data from sqlite to firebase


    public ArrayList getsensordata() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> array_list = new ArrayList<String>();
        Cursor cursor = db.rawQuery( "select * from "+SENSORDATA, null );

        if (cursor.moveToFirst()) {
            do {
                String gyroX,gyroY,gyroZ, goingLatitude,goingLongitude,goingdatetime,goingX,goingY,goingZ;
                goingLatitude=cursor.getString(cursor.getColumnIndexOrThrow("SENSORGPS_LATITUDE"));
                goingLongitude=cursor.getString(cursor.getColumnIndexOrThrow("SENSORGPS_LONGITUDE"));

                goingX=cursor.getString(cursor.getColumnIndexOrThrow("ACCELEROMETER_X"));
                goingY=cursor.getString(cursor.getColumnIndexOrThrow("ACCELEROMETER_Y"));
                goingZ=cursor.getString(cursor.getColumnIndexOrThrow("ACCELEROMETER_Z"));

                gyroX=cursor.getString(cursor.getColumnIndexOrThrow("GYROSCOPE_X"));
                gyroY=cursor.getString(cursor.getColumnIndexOrThrow("GYROSCOPE_Y"));
                gyroZ=cursor.getString(cursor.getColumnIndexOrThrow("GYROSCOPE_Z"));

                goingdatetime=cursor.getString(cursor.getColumnIndexOrThrow("DATE_TIME"));


                Log.d("FIREBASE",goingLatitude+" "+goingLongitude);
                // Write a message to the database
                String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                Log.d("Userid:" ,uid);

                FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference myRef = database.getReference("SENSORDATA/USERS/" +uid+ "/SENSORDATA");

                //myRef.setValue(going);

                HashMap<String, Object> result = new HashMap<>();

                result.put("SENSORGPS_LATITUDE", goingLatitude);
                result.put("SENSORGPS_LONGITUDE", goingLongitude);

                result.put("ACCELEROMETER_X", goingX);
                result.put("ACCELEROMETER_Y", goingY);
                result.put("ACCELEROMETER_Z", goingZ);

                result.put("GYROSCOPE_X", gyroX);
                result.put("GYROSCOPE_Y", gyroY);
                result.put("GYROSCOPE_Z", gyroZ);

                result.put("Date_time", goingdatetime);

                myRef.push().setValue(result);


            } while (cursor.moveToNext());
            deleteAllsensordata();
        }
        return null;
    }

    //function for deleting the data from sqlite

    public void deleteAllsensordata(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ SENSORDATA );
        Log.d("SENSORTABLE","deleted");
        db.close();

    }
}
