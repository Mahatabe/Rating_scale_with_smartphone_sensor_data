package com.example.sensorratingscale;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.gms.location.LocationResult;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sensorratingscale.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;


public class MainActivity extends AppCompatActivity {


    private AppBarConfiguration mAppBarConfiguration;

    /* code from mainactivity */

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    public static int i=0;
    Handler handler1= new Handler(Looper.getMainLooper());
    Handler handler2= new Handler(Looper.getMainLooper());
    Runnable runnable1,runnable2;


    private void init (int firebaseHandler, int sqliteHandler){
        //Log.d("init22","handeller");


        LocationResult locationResult = null;
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    MainActivity.this,

                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        } else {
            startLocationService();
            //DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
            //dataBaseHelper.addOne();
        }
        startAccelerometerService();
        startGyroscopeService();

        DataBaseHelper dataBaseHelpersensor = new DataBaseHelper(MainActivity.this);

        //sqlite handler to insert data into sqlite after particular time period
        runnable1 = new Runnable() {
            @Override
            public void run() {

                dataBaseHelpersensor.addsensordata();
                Toast.makeText(MainActivity.this, "Executing from Runner and Data inserted", Toast.LENGTH_SHORT).show();
                handler1.postDelayed(this, Constants.sqliteHandler);
            }
        };
        handler1.postDelayed(runnable1,Constants.sqliteHandler);

        //firebase handler to insert data into firebase from sqlite after particular time period
        runnable2 = new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                dataBaseHelpersensor.getsensordata();

                //  Toast.makeText(MainActivity2.this, "Executing from Runner and Data inserted to Firebase", Toast.LENGTH_SHORT).show();

                handler2.postDelayed(this,Constants.firebaseHandler);
            }
        };
        handler2.postDelayed(runnable2,Constants.firebaseHandler);
    }

    TextView text;
    ImageView image;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("init6","start");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);

        text = headerLayout.findViewById(R.id.nav_text);
        image = headerLayout.findViewById(R.id.nav_image);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startLocationService();
            }else {
                Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private boolean isLocationServiceRunning(){

        Log.d("init9","location");
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null){
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName())){
                    if (service.foreground){
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }
    private void startLocationService(){

        Log.d("init2","running");
        if (!isLocationServiceRunning()){
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this, "Location Service Started", Toast.LENGTH_SHORT).show();
        }

    }
    private void startAccelerometerService(){

        Log.d("init3","accelo");

        Intent intent = new Intent(getApplicationContext(), AccelerometerService.class);
        //intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
        startService(intent);
        //Toast.makeText(this, "Accelerometer Service Started", Toast.LENGTH_SHORT).show();
    }
    private void startGyroscopeService(){

        Log.d("init4","gyro");

        Intent intent = new Intent(getApplicationContext(), GyroscopeService.class);
        //intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
        startService(intent);
        //Toast.makeText(this, "Gyroscope Service Started", Toast.LENGTH_SHORT).show();
    }



    private void stopLocationService(){
        if (!isLocationServiceRunning()){
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            startService(intent);
            // Toast.makeText(this, "Location Service Stopped", Toast.LENGTH_SHORT).show();
        }
    }

    //onDestroy function for killing the process after removing the app from the background
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    //function for fetching handler values from firebase
    public void fromFirebaseHandler(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("CONSTANT");


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int firebaseHandler,sqliteHandler,gpsSetInterval,gpsSetFastestInterval;

                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                firebaseHandler= Integer.parseInt((dataSnapshot.child("firebaseHandler").getValue().toString()));
                sqliteHandler= Integer.parseInt((dataSnapshot.child("sqliteHandler").getValue().toString()));
                gpsSetInterval= Integer.parseInt((dataSnapshot.child("gpsSetInterval").getValue().toString()));
                gpsSetFastestInterval= Integer.parseInt((dataSnapshot.child("gpsSetFastestInterval").getValue().toString()));

                Constants.gpsSetInterval=gpsSetInterval;
                Constants.gpsSetFastestInterval=gpsSetFastestInterval;
                Constants.firebaseHandler=firebaseHandler;
                Constants.sqliteHandler = sqliteHandler;
                init(firebaseHandler,sqliteHandler);
                // Log.d("ZZZZ", "Value is: " +Constants.firebaseHandler );
                // Log.d("ZZZZ2", "Value is: " +Constants.sqliteHandler );

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w("ZZZZ", "Failed to read value.", error.toException());
            }
        });
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            text.append(name + "\n");
            text.append(email);

        }
    }

    public void onClick(View v) {
        Random generator = new Random();
        int number = generator.nextInt(3) + 1;
        Class activity = null;
        switch(number) {
            case 1:
                activity = SliderActivity.class;
                break;
            case 2:
                activity = NumericActivity.class;
                break;
            default:
                activity = DescriptiveActivity.class;
                break;
        }
        Intent intent = new Intent(getBaseContext(), activity);
        startActivity(intent);
    }


    public void onClick2(View view)
    {
        Toast.makeText(getApplicationContext(),"Thank you for choosing us!",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this,StarActivity.class);
        startActivity(intent);
    }

    public void onClick3(View view)
    {
        Toast.makeText(getApplicationContext(),"Thank you for choosing us!",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this,EmojiActivity.class);
        startActivity(intent);
    }

    /*

    public void onClick3(View view)
    {
        Toast.makeText(getApplicationContext(),"Thank you for choosing us!",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this,DescriptiveActivity.class);
        startActivity(intent);
    }
*/
}